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
package net.kuujo.vertigo.rpc;

import net.kuujo.vertigo.Vertigo;
import net.kuujo.vertigo.message.JsonMessage;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/**
 * An executor verticle.
 *
 * This class should be extended to implement a Java executor verticle.
 * It is an alternative method to defining executors.
 *
 * @author Jordan Halterman
 */
public class ExecutorVerticle extends Verticle {
  private Vertigo vertigo;
  protected StreamExecutor executor;

  @Override
  public void start(final Future<Void> future) {
    vertigo = new Vertigo(this);
    executor = vertigo.createStreamExecutor();
    executor.start(new Handler<AsyncResult<StreamExecutor>>() {
      @Override
      public void handle(AsyncResult<StreamExecutor> result) {
        if (result.failed()) {
          future.setFailure(result.cause());
        }
        else {
          ExecutorVerticle.super.start(future);
        }
      }
    });
  }

  /**
   * Executes the network.
   *
   * @param data
   *   The execution data.
   * @param resultHandler
   *   An asynchronous handler to be invoked with the execution result.
   * @return
   *   A unique message correlation identifier.
   */
  protected String execute(JsonObject data, Handler<AsyncResult<JsonMessage>> resultHandler) {
    return executor.execute(data, resultHandler);
  }

  /**
   * Executes the network.
   *
   * @param data
   *   The execution data.
   * @param tag
   *   A tag to apply to the output message.
   * @param resultHandler
   *   An asynchronous handler to be invoked with the execution result.
   * @return
   *   A unique message correlation identifier.
   */
  protected String execute(JsonObject data, String tag, Handler<AsyncResult<JsonMessage>> resultHandler) {
    return executor.execute(data, tag, resultHandler);
  }

}
