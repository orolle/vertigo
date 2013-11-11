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
 * A default {@link BasicFeeder} implementation.
 *
 * @author Jordan Halterman
 */
public class DefaultBasicFeeder extends AbstractFeeder<BasicFeeder> implements BasicFeeder {
  private Handler<JsonMessage> ackHandler = new Handler<JsonMessage>() {
    @Override
    public void handle(JsonMessage message) {
      dequeue(message.id());
    }
  };

  private Handler<JsonMessage> failHandler = new Handler<JsonMessage>() {
    @Override
    public void handle(JsonMessage message) {
      if (autoRetry) {
        output.emit(message);
      }
      else {
        dequeue(message.id());
      }
    }
  };

  public DefaultBasicFeeder(Vertx vertx, Container container, InstanceContext context) {
    super(vertx, container, context);
  }

  @Override
  public BasicFeeder start(Handler<AsyncResult<BasicFeeder>> doneHandler) {
    final Future<BasicFeeder> future = new DefaultFutureResult<BasicFeeder>().setHandler(doneHandler);
    return super.start(new Handler<AsyncResult<BasicFeeder>>() {
      @Override
      public void handle(AsyncResult<BasicFeeder> result) {
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
  public String emit(JsonObject data) {
    return enqueue(output.emit(createMessage(data)));
  }

  @Override
  public String emit(JsonObject data, String tag) {
    return enqueue(output.emit(createMessage(data, tag)));
  }

}
