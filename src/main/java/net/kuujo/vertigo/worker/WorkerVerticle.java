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
package net.kuujo.vertigo.worker;

import net.kuujo.vertigo.Vertigo;
import net.kuujo.vertigo.message.JsonMessage;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/**
 * A Java worker verticle.
 *
 * This base class can be extended as an alternative worker implementation for Java
 * components. Simply extend this class and override the handleMessage method.
 *
 * @author Jordan Halterman
 */
public abstract class WorkerVerticle extends Verticle {
  private Vertigo vertigo;
  private BasicWorker worker;

  private Handler<JsonMessage> messageHandler = new Handler<JsonMessage>() {
    @Override
    public void handle(JsonMessage message) {
      handleMessage(message);
    }
  };

  @Override
  public void start(final Future<Void> future) {
    vertigo = new Vertigo(this);
    worker = vertigo.createBasicWorker();
    worker.start(new Handler<AsyncResult<BasicWorker>>() {
      @Override
      public void handle(AsyncResult<BasicWorker> result) {
        if (result.failed()) {
          future.setFailure(result.cause());
        }
        else {
          worker.messageHandler(messageHandler);
          WorkerVerticle.super.start(future);
        }
      }
    });
  }

  /**
   * Handles a message received by the worker.
   *
   * @param message
   *   The received message.
   */
  protected abstract void handleMessage(JsonMessage message);

  /**
   * Emits a new message from the worker.
   *
   * @param data
   *   The data to emit.
   */
  protected void emit(JsonObject data) {
    worker.emit(data);
  }

  /**
   * Emits a new message from the worker.
   *
   * @param data
   *   The data to emit.
   * @param tag
   *   A tag to apply to the new message.
   */
  protected void emit(JsonObject data, String tag) {
    worker.emit(data, tag);
  }

  /**
   * Emits a child message from the worker.
   *
   * @param data
   *   The data to emit.
   * @param parent
   *   The message parent.
   */
  protected void emit(JsonObject data, JsonMessage parent) {
    worker.emit(data, parent);
  }

  /**
   * Emits a child message from the worker.
   *
   * @param data
   *   The data to emit.
   * @param tag
   *   A tag to apply to the message.
   * @param parent
   *   The message parent.
   */
  protected void emit(JsonObject data, String tag, JsonMessage parent) {
    worker.emit(data, tag, parent);
  }

  /**
   * Acknowledges processing of a message.
   *
   * @param message
   *   The message to ack.
   */
  protected void ack(JsonMessage message) {
    worker.ack(message);
  }

  /**
   * Fails processing of a message.
   *
   * @param message
   *   The message to fail.
   */
  protected void fail(JsonMessage message) {
    worker.fail(message);
  }

}
