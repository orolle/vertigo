/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.vertigo.auditor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

/**
 * Observes component message trees and manages ack/fail.
 *
 * @author Jordan Halterman
 */
public class Auditor extends BusModBase implements Handler<Message<JsonObject>> {
  private String address;
  private String broadcastAddress;
  private boolean enabled;
  private long expire = 30000;
  private long delay = 0;
  private Map<String, Node> nodes = new HashMap<>();
  private Map<Long, Timer> timers = new HashMap<>();

  public static final String ADDRESS = "address";
  public static final String BROADCAST = "broadcast";
  public static final String ENABLED = "enabled";
  public static final String EXPIRE = "expire";
  public static final String DELAY = "delay";

  @Override
  public void start() {
    super.start();
    address = getMandatoryStringConfig("address");
    broadcastAddress = getMandatoryStringConfig("broadcast");
    enabled = getOptionalBooleanConfig("enabled", true);
    expire = getOptionalLongConfig("expire", expire);
    delay = getOptionalLongConfig("delay", delay);
    eb.registerHandler(address, this);
  }

  @Override
  public void handle(Message<JsonObject> message) {
    String action = getMandatoryString("action", message);
    switch (action) {
      case "create":
        doCreate(message);
        break;
      case "fork":
        doFork(message);
        break;
      case "ack":
        doAck(message);
        break;
      case "fail":
        doFail(message);
        break;
      default:
        sendError(message, String.format("Invalid action %s.", action));
        break;
    }
  }

  private Handler<Node> ackHandler = new Handler<Node>() {
    @Override
    public void handle(Node node) {
      Timer timer = node.timer;
      if (timer != null) {
        timer.ids.remove(node.id);
      }
      clearRoot(node);
      eb.publish(broadcastAddress, new JsonObject().putString("action", "ack").putString("id", node.id));
    }
  };

  private Handler<Node> failHandler = new Handler<Node>() {
    @Override
    public void handle(Node node) {
      Timer timer = node.timer;
      if (timer != null) {
        timer.ids.add(node.id);
      }
      clearRoot(node);
      eb.publish(broadcastAddress, new JsonObject().putString("action", "fail").putString("id", node.id).putString("message", node.failureMessage));
    }
  };

  /**
   * Creates a message.
   */
  private void doCreate(Message<JsonObject> message) {
    String id = getMandatoryString("id", message);
    if (enabled) {
       // We simply add ack and fail handlers to the root node. If a descendant
      // node is failed then it will bubble up to the root. Once all child nodes
      // are acked the parent will be acked which will also bubble up to the root.
      Timer timer = getCurrentTimer();
      timer.ids.add(id);
      Node node = new Root(id, timer, vertx, delay);
      node.ackHandler(ackHandler);
      node.failHandler(failHandler);
      nodes.put(node.id, node);
    }
    // If acking is disabled then immediately ack the message back to its source.
    else {
      eb.publish(broadcastAddress, new JsonObject().putString("action", "ack").putString("id", id));
    }
  }

  /**
   * Expires a node by ID.
   */
  private void expire(String id) {
    // Simply clear the expired node. It should be up to the feeder to timeout
    // the node if it failed. This is simply an added mechanism to prevent memory leaks.
    if (nodes.containsKey(id)) {
      nodes.get(id).fail();
    }
  }

  /**
   * Clears an entire message tree from storage.
   */
  private void clearRoot(Node node) {
    clearNode(node);
  }

  /**
   * Clears all children of a node from storage.
   */
  private void clearNode(Node node) {
    if (node.children.size() > 0) {
      for (Node child : node.children) {
        clearNode(child);
      }
      if (nodes.containsKey(node.id)) {
        nodes.remove(node.id);
      }
    }
    else if (nodes.containsKey(node.id)) {
      nodes.remove(node.id);
    }
  }

  /**
   * Creates a message fork.
   */
  private void doFork(Message<JsonObject> message) {
    String parentId = getMandatoryString("parent", message);
    if (nodes.containsKey(parentId)) {
      String id = getMandatoryString("id", message);
      Node node = new Node(id, vertx, delay);
      nodes.get(parentId).addChild(node);
      nodes.put(id, node);
    }
  }

  /**
   * Acks a message.
   */
  private void doAck(Message<JsonObject> message) {
    String id = getMandatoryString("id", message);
    if (nodes.containsKey(id)) {
      nodes.get(id).ack();
    }
  }

  /**
   * Fails a message.
   */
  private void doFail(Message<JsonObject> message) {
    String id = getMandatoryString("id", message);
    String failMessage = message.body().getString("message");
    if (nodes.containsKey(id)) {
      nodes.get(id).fail(failMessage);
    }
  }

  /**
   * An expire timer.
   */
  private class Timer {
    private final long endTime;
    private final Set<String> ids = new HashSet<String>();

    private Timer(long endTime) {
      this.endTime = endTime;
    }

    private Timer start() {
      vertx.setTimer(expire, new Handler<Long>() {
        @Override
        public void handle(Long timerId) {
          timers.remove(endTime);
          for (String id : ids) {
            expire(id);
          }
        }
      });
      timers.put(endTime, this);
      return this;
    }

  }

  /**
   * Gets the current timer.
   *
   * Timers are grouped by rounding the expire time to the nearest .1 second.
   * Message timeouts should be lengthy, and this ensures that a timer
   * is not created for every single message emitted from a feeder.
   */
  private Timer getCurrentTimer() {
    long end = Math.round((System.currentTimeMillis() + expire) / 100) * 100;
    if (timers.containsKey(end)) {
      return timers.get(end);
    }
    else {
      return new Timer(end).start();
    }
  }

  /**
   * Represents a single node in a message tree.
   */
  private static class Node {
    private final String id;
    private final Timer timer;
    private final Vertx vertx;
    private final long delay;
    private long delayTimer;
    private final Set<Node> children = new HashSet<>();
    private final Set<Node> complete = new HashSet<>();
    private Handler<Node> ackHandler;
    private Handler<Node> failHandler;
    private boolean ready = true;
    private boolean acked;
    private boolean ack;
    private String failureMessage;
    private boolean locked;

    private Handler<Node> childAckHandler = new Handler<Node>() {
      @Override
      public void handle(Node node) {
        if (!failed()) {
          complete.add(node);
          if (complete.size() == children.size()) {
            ready();
          }
        }
      }
    };

    private Handler<Node> childFailHandler = new Handler<Node>() {
      @Override
      public void handle(Node node) {
        if (!failed()) {
          Node.this.failureMessage = node.failureMessage;
          fail();
        }
      }
    };

    private Node(String id, Vertx vertx, long delay) {
      this.id = id;
      this.timer = null;
      this.vertx = vertx;
      this.delay = delay;
    }

    private Node(String id, Timer timer, Vertx vertx, long delay) {
      this.id = id;
      this.timer = timer;
      this.vertx = vertx;
      this.delay = delay;
    }

    /**
     * Indicates that the message is ready to be acked.
     */
    private void ready() {
      ready = true;
      checkAck();
    }

    /**
     * Acks the message.
     */
    protected final void ack() {
      acked = true; ack = true;
      checkAck();
    }

    /**
     * Fails the message.
     */
    protected final void fail() {
      acked = true; ack = false;
      checkAck();
    }

    /**
     * Fails the message.
     */
    protected final void fail(String message) {
      acked = true; ack = false;
      this.failureMessage = message;
      checkAck();
    }

    /**
     * Indicates whether the message has been failed.
     */
    protected final boolean failed() {
      return acked && !ack;
    }

    /**
     * Checks whether the ack or fail handler can be invoked.
     */
    private void checkAck() {
      if (!locked && acked) {
        // If the message is ready and acked then invoke the ack handler.
        if (ack && ready) {
          // If the auditor is using a delay timer, set the timer. If a child
          // message is added to the tree before the timer expires the timer
          // will be removed.
          if (delay > 0) {
            delayTimer = vertx.setTimer(delay, new Handler<Long>() {
              @Override
              public void handle(Long timerID) {
                locked = true;
                ackHandler.handle(Node.this);
              }
            });
          }
          else {
            locked = true;
            ackHandler.handle(this);
          }
        }
        // If the message was failed then immediately invoke the fail handler.
        else if (!ack) {
          locked = true;
          failHandler.handle(this);
        }
      }
    }

    /**
     * Sets an ack handler on the message.
     */
    private void ackHandler(Handler<Node> handler) {
      this.ackHandler = handler;
    }

    /**
     * Sets a fail handler on the message.
     */
    private void failHandler(Handler<Node> handler) {
      this.failHandler = handler;
    }

    /**
     * Adds a child to the message.
     *
     * @param child
     *   The child message.
     */
    protected void addChild(Node child) {
      children.add(child);
      ready = false;
      child.ackHandler(childAckHandler);
      child.failHandler(childFailHandler);
      if (delay > 0 && delayTimer > 0) {
        vertx.cancelTimer(delayTimer);
        delayTimer = 0;
      }
    }
  }

  /**
   * Represents the root element of a message tree.
   */
  private static class Root extends Node {
    private Root(String id, Vertx vertx, long delay) {
      super(id, vertx, delay);
    }

    private Root(String id, Timer timer, Vertx vertx, long delay) {
      super(id, timer, vertx, delay);
    }

    @Override
    protected void addChild(Node child) {
      super.addChild(child);
      if (!failed()) {
        ack();
      }
    }
  }

}
