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

import net.kuujo.vertigo.Vertigo;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/**
 * A Java feeder verticle.
 *
 * This class can be extended as an alternate method for creating Java feeders.
 * Extend this class as you would any other Java verticle and implement the
 * feedMessage, ack, and fail methods.
 *
 * @author Jordan Halterman
 */
public abstract class FeederVerticle extends Verticle {
  private Vertigo vertigo;
  protected PollingFeeder feeder;

  private Handler<PollingFeeder> feedHandler = new Handler<PollingFeeder>() {
    @Override
    public void handle(PollingFeeder feeder) {
      nextMessage();
    }
  };

  private Handler<String> ackHandler = new Handler<String>() {
    @Override
    public void handle(String messageId) {
      ack(messageId);
    }
  };

  private Handler<String> failHandler = new Handler<String>() {
    @Override
    public void handle(String messageId) {
      fail(messageId);
    }
  };

  @Override
  public void start(final Future<Void> future) {
    vertigo = new Vertigo(this);
    feeder = vertigo.createPollingFeeder();
    feeder.start(new Handler<AsyncResult<PollingFeeder>>() {
      @Override
      public void handle(AsyncResult<PollingFeeder> result) {
        if (result.failed()) {
          future.setFailure(result.cause());
        }
        else {
          feeder.feedHandler(feedHandler);
          feeder.ackHandler(ackHandler);
          feeder.failHandler(failHandler);
          future.setResult(null);
        }
      }
    });
  }

  /**
   * Called to request the next message from the feeder.
   */
  protected abstract void nextMessage();

  /**
   * Emits a message from the feeder.
   *
   * @param data
   *   The data to emit.
   * @return
   *   The new message's unique identifier.
   */
  protected String emit(JsonObject data) {
    return feeder.emit(data);
  }

  /**
   * Emits a message from the feeder.
   *
   * @param data
   *   The data to emit.
   * @param tag
   *   A tag to apply to the new message.
   * @return
   *   The new message's unique identifier.
   */
  protected String emit(JsonObject data, String tag) {
    return feeder.emit(data, tag);
  }

  /**
   * Called when a message has been acked.
   *
   * @param id
   *   The unique message identifier.
   */
  protected abstract void ack(String id);

  /**
   * Called when a message has been failed.
   *
   * @param id
   *   The unique message identifier.
   */
  protected abstract void fail(String id);

}
