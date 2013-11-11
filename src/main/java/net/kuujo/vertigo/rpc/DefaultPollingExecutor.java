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

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.impl.DefaultFutureResult;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

/**
 * A default {@link PollingExecutor} implementation.
 *
 * @author Jordan Halterman
 */
public class DefaultPollingExecutor extends AbstractExecutor<PollingExecutor> implements PollingExecutor {
  private static final long DEFAULT_EXECUTE_DELAY = 100;
  private long executeDelay = DEFAULT_EXECUTE_DELAY;
  private Handler<PollingExecutor> executeHandler;
  private boolean executed;

  public DefaultPollingExecutor(Vertx vertx, Container container, InstanceContext context) {
    super(vertx, container, context);
  }

  @Override
  public PollingExecutor start(Handler<AsyncResult<PollingExecutor>> doneHandler){
    final Future<PollingExecutor> future = new DefaultFutureResult<PollingExecutor>().setHandler(doneHandler);
    return super.start(new Handler<AsyncResult<PollingExecutor>>() {
      @Override
      public void handle(AsyncResult<PollingExecutor> result) {
        if (result.failed()) {
          future.setFailure(result.cause());
        }
        else {
          future.setResult(result.result());
          recursiveExecute();
        }
      }
    });
  }

  private Handler<Long> timerHandler = new Handler<Long>() {
    @Override
    public void handle(Long timerId) {
      recursiveExecute();
    }
  };

  /**
   * Recursively calls the execute handler, rescheduling as necessary.
   */
  private void recursiveExecute() {
    if (executeHandler == null) {
      return;
    }

    // If the execute handler emits a message then continue the iteration, otherwise
    // reschedule the next execute handler call for executeDelay milliseconds.
    executed = true;
    while (!queueFull() && executed) {
      executed = false;
      executeHandler.handle(this);
    }

    vertx.setTimer(executeDelay, timerHandler);
  }

  @Override
  public PollingExecutor setExecuteDelay(long delay) {
    this.executeDelay = delay;
    return this;
  }

  @Override
  public long getExecuteDelay() {
    return executeDelay;
  }

  @Override
  public PollingExecutor executeHandler(Handler<PollingExecutor> executeHandler) {
    this.executeHandler = executeHandler;
    return this;
  }

  @Override
  public String execute(JsonObject data, Handler<AsyncResult<JsonMessage>> resultHandler) {
    executed = true;
    return super.execute(data, resultHandler);
  }

  @Override
  public String execute(JsonObject data, String tag, Handler<AsyncResult<JsonMessage>> resultHandler) {
    executed = true;
    return super.execute(data, tag, resultHandler);
  }

}
