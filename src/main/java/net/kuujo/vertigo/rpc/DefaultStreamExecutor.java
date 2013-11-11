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

import net.kuujo.vertigo.context.InstanceContext;
import net.kuujo.vertigo.message.JsonMessage;

import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.platform.Container;

/**
 * A default {@link StreamExecutor} implementation.
 *
 * @author Jordan Halterman
 */
public class DefaultStreamExecutor extends AbstractExecutor<StreamExecutor> implements StreamExecutor {
  private Handler<Void> drainHandler;
  private boolean paused;

  protected DefaultStreamExecutor(Vertx vertx, Container container, InstanceContext context) {
    super(vertx, container, context);
  }

  @Override
  public StreamExecutor drainHandler(Handler<Void> drainHandler) {
    this.drainHandler = drainHandler;
    return this;
  }

  /**
   * Executes a message.
   */
  protected String doExecute(JsonMessage message, Future<JsonMessage> resultFuture) {
    String returnValue = super.doExecute(message, resultFuture);
    if (queueFull()) {
      paused = true;
    }
    return returnValue;
  }

  /**
   * Called when a message has been acked.
   */
  protected void doAck(JsonMessage message) {
    super.doAck(message);
    checkPause();
  }

  /**
   * Called when a message has been failed.
   */
  protected void doFail(JsonMessage message) {
    super.doFail(message);
    checkPause();
  }

  /**
   * Checks whether the executor should be unpaused.
   */
  private void checkPause() {
    if (paused && !queueFull()) {
      paused = false;
      if (drainHandler != null) {
        drainHandler.handle(null);
      }
    }
  }

}
