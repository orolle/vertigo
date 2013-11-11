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

import net.kuujo.vertigo.context.InstanceContext;
import net.kuujo.vertigo.message.JsonMessage;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.impl.DefaultFutureResult;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

/**
 * A default {@link PollingFeeder} implementation.
 *
 * @author Jordan Halterman
 */
public class DefaultPollingFeeder extends AbstractFeeder<PollingFeeder> implements PollingFeeder {
  private static final long DEFAULT_FEED_DELAY = 100;
  private long feedDelay = DEFAULT_FEED_DELAY;
  private Handler<PollingFeeder> feedHandler;
  private Handler<String> ackHandler;
  private Handler<String> failHandler;
  private boolean fed;

  private Handler<JsonMessage> internalAckHandler = new Handler<JsonMessage>() {
    @Override
    public void handle(JsonMessage message) {
      dequeue(message.id());
      if (ackHandler != null) {
        ackHandler.handle(message.id());
      }
    }
  };

  private Handler<JsonMessage> internalFailHandler = new Handler<JsonMessage>() {
    @Override
    public void handle(JsonMessage message) {
      if (autoRetry) {
        output.emit(message);
      }
      else if (failHandler != null) {
        dequeue(message.id());
        failHandler.handle(message.id());
      }
    }
  };

  public DefaultPollingFeeder(Vertx vertx, Container container, InstanceContext context) {
    super(vertx, container, context);
  }

  @Override
  public PollingFeeder start(Handler<AsyncResult<PollingFeeder>> doneHandler) {
    final Future<PollingFeeder> future = new DefaultFutureResult<PollingFeeder>().setHandler(doneHandler);
    return super.start(new Handler<AsyncResult<PollingFeeder>>() {
      @Override
      public void handle(AsyncResult<PollingFeeder> result) {
        if (result.failed()) {
          future.setFailure(result.cause());
        }
        else {
          output.ackHandler(internalAckHandler);
          output.failHandler(internalFailHandler);
          future.setResult(result.result());
          recursiveFeed();
        }
      }
    });
  }

  /**
   * Recursively calls the feed handler, rescheduling as necessary.
   */
  private void recursiveFeed() {
    if (feedHandler == null) {
      return;
    }

    // Each time the feed handler is called, reset the 'fed' variable to false.
    // If a feeder emit() method is called during each iteration, the 'fed'
    // variable will be set to true, indicating that the feed was successful.
    // If the feed handle emits a message then continue the iteration, otherwise
    // reschedule the next feed handler call for feedDelay milliseconds.
    fed = true;
    while (!queueFull() && fed) {
      fed = false;
      feedHandler.handle(this);
    }

    vertx.setTimer(feedDelay, timerHandler);
  }

  private Handler<Long> timerHandler = new Handler<Long>() {
    @Override
    public void handle(Long timerId) {
      recursiveFeed();
    }
  };

  @Override
  public PollingFeeder setFeedDelay(long delay) {
    this.feedDelay = delay;
    return this;
  }

  @Override
  public long getFeedDelay() {
    return feedDelay;
  }

  @Override
  public PollingFeeder feedHandler(Handler<PollingFeeder> feedHandler) {
    this.feedHandler = feedHandler;
    return this;
  }

  @Override
  public PollingFeeder ackHandler(Handler<String> ackHandler) {
    this.ackHandler = ackHandler;
    return this;
  }

  @Override
  public PollingFeeder failHandler(Handler<String> failHandler) {
    this.failHandler = failHandler;
    return this;
  }

  @Override
  public String emit(JsonObject data) {
    fed = true;
    return enqueue(output.emit(createMessage(data)));
  }

  @Override
  public String emit(JsonObject data, String tag) {
    fed = true;
    return enqueue(output.emit(createMessage(data, tag)));
  }

}
