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
package net.kuujo.vertigo.feeder;

import java.util.HashMap;
import java.util.Map;

import net.kuujo.vertigo.context.InstanceContext;
import net.kuujo.vertigo.message.JsonMessage;
import net.kuujo.vertigo.runtime.FailureException;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.impl.DefaultFutureResult;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

/**
 * A default {@link StreamFeeder} implementation.
 *
 * @author Jordan Halterman
 */
public class DefaultStreamFeeder extends AbstractFeeder<StreamFeeder> implements StreamFeeder {
  private Handler<Void> drainHandler;
  private boolean paused;
  private Map<String, Future<Void>> ackFutures = new HashMap<String, Future<Void>>();

  private Handler<JsonMessage> ackHandler = new Handler<JsonMessage>() {
    @Override
    public void handle(JsonMessage message) {
      if (ackFutures.containsKey(message.id())) {
        // When dealing with asynchronous ack handlers, we first remove the message
        // from the queue and invoke the handler before checking the drain handler.
        // This is because we need to always guarantee that the feed queue is not
        // full when an ack handler is called to allow it to re-emit the message.
        DefaultStreamFeeder.super.dequeue(message.id());
        ackFutures.remove(message.id()).setResult(null);
        checkPause();
      }
      else {
        dequeue(message.id());
      }
    }
  };

  private Handler<JsonMessage> failHandler = new Handler<JsonMessage>() {
    @Override
    public void handle(JsonMessage message) {
      if (autoRetry) {
        output.emit(message);
      }
      else if (ackFutures.containsKey(message.id())) {
        // When dealing with asynchronous ack handlers, we first remove the message
        // from the queue and invoke the handler before checking the drain handler.
        // This is because we need to always guarantee that the feed queue is not
        // full when an ack handler is called to allow it to re-emit the message.
        DefaultStreamFeeder.super.dequeue(message.id());
        ackFutures.remove(message.id()).setFailure(new FailureException("Processing failed."));
        checkPause();
      }
      else {
        dequeue(message.id());
      }
    }
  };

  public DefaultStreamFeeder(Vertx vertx, Container container, InstanceContext context) {
    super(vertx, container, context);
  }

  @Override
  public StreamFeeder start(Handler<AsyncResult<StreamFeeder>> doneHandler) {
    final Future<StreamFeeder> future = new DefaultFutureResult<StreamFeeder>().setHandler(doneHandler);
    return super.start(new Handler<AsyncResult<StreamFeeder>>() {
      @Override
      public void handle(AsyncResult<StreamFeeder> result) {
        if (result.failed()) {
          future.setFailure(result.cause());
        }
        else {
          output.ackHandler(ackHandler);
          output.failHandler(failHandler);
          future.setResult(result.result());
        }
      }
    });
  }

  @Override
  protected String enqueue(String messageId) {
    super.enqueue(messageId);
    checkPause();
    return messageId;
  }

  @Override
  protected String dequeue(String messageId) {
    super.dequeue(messageId);
    checkPause();
    return messageId;
  }

  /**
   * Checks whether the feeder can be unpaused and the drain handler called.
   */
  private void checkPause() {
    if (queueFull()) {
      paused = true;
    }
    else if (paused) {
      paused = false;
      if (drainHandler != null) {
        drainHandler.handle(null);
      }
    }
  }

  @Override
  public boolean queueFull() {
    return paused;
  }

  @Override
  public StreamFeeder drainHandler(Handler<Void> drainHandler) {
    this.drainHandler = drainHandler;
    return this;
  }

  @Override
  public String emit(JsonObject data) {
    return enqueue(output.emit(createMessage(data)));
  }

  @Override
  public String emit(JsonObject data, String tag) {
    return enqueue(output.emit(createMessage(data, tag)));
  }

  @Override
  public String emit(JsonObject data, Handler<AsyncResult<Void>> ackHandler) {
    String id = enqueue(output.emit(createMessage(data)));
    ackFutures.put(id, new DefaultFutureResult<Void>().setHandler(ackHandler));
    return id;
  }

  @Override
  public String emit(JsonObject data, String tag, Handler<AsyncResult<Void>> ackHandler) {
    String id = enqueue(output.emit(createMessage(data, tag)));
    ackFutures.put(id, new DefaultFutureResult<Void>().setHandler(ackHandler));
    return id;
  }

}
