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

import java.util.HashMap;
import java.util.Map;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.impl.DefaultFutureResult;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

import net.kuujo.vertigo.component.ComponentBase;
import net.kuujo.vertigo.context.InstanceContext;
import net.kuujo.vertigo.message.JsonMessage;
import net.kuujo.vertigo.runtime.FailureException;
import net.kuujo.vertigo.runtime.TimeoutException;

/**
 * An abstract executor implementation.
 *
 * @author Jordan Halterman
 *
 * @param <T> The executor type
 */
public abstract class AbstractExecutor<T extends Executor<T>> extends ComponentBase<T> implements Executor<T> {
  protected boolean autoRetry;
  private static final long DEFAULT_REPLY_TIMEOUT = 30000;
  protected long resultTimeout = DEFAULT_REPLY_TIMEOUT;
  private static final long DEFAULT_MAX_QUEUE_SIZE = 1000;
  protected long maxQueueSize = DEFAULT_MAX_QUEUE_SIZE;
  protected long queueSize;
  private Map<String, QueuedItem> items = new HashMap<String, QueuedItem>();

  protected AbstractExecutor(Vertx vertx, Container container, InstanceContext context) {
    super(vertx, container, context);
  }

  private Handler<JsonMessage> messageHandler = new Handler<JsonMessage>() {
    @Override
    public void handle(JsonMessage message) {
      doReceive(message);
    }
  };

  private Handler<JsonMessage> ackHandler = new Handler<JsonMessage>() {
    @Override
    public void handle(JsonMessage message) {
      doAck(message);
    }
  };

  private Handler<JsonMessage> failHandler = new Handler<JsonMessage>() {
    @Override
    public void handle(JsonMessage message) {
      doFail(message);
    }
  };

  private class QueuedItem {
    private final JsonMessage message;
    private final Future<JsonMessage> future;
    private final Long timerId;
    private JsonMessage result = null;
    public QueuedItem(JsonMessage message, Future<JsonMessage> future, long timerId) {
      this.message = message;
      this.future = future;
      this.timerId = timerId;
    }
  }

  @Override
  public T start(Handler<AsyncResult<T>> doneHandler) {
    input.messageHandler(messageHandler);
    output.ackHandler(ackHandler);
    output.failHandler(failHandler);
    return super.start(doneHandler);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T setAutoRetry(boolean autoRetry) {
    this.autoRetry = autoRetry;
    return (T) this;
  }

  @Override
  public boolean isAutoRetry() {
    return autoRetry;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T setResultTimeout(long timeout) {
    this.resultTimeout = timeout;
    return (T) this;
  }

  @Override
  public long getResultTimeout() {
    return resultTimeout;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T setMaxQueueSize(long maxSize) {
    this.maxQueueSize = maxSize;
    return (T) this;
  }

  @Override
  public long getMaxQueueSize() {
    return maxQueueSize;
  }

  @Override
  public boolean queueFull() {
    return queueSize >= maxQueueSize;
  }

  @Override
  public String execute(JsonObject data, Handler<AsyncResult<JsonMessage>> resultHandler) {
    return doExecute(createMessage(data), new DefaultFutureResult<JsonMessage>().setHandler(resultHandler));
  }

  @Override
  public String execute(JsonObject data, String tag, Handler<AsyncResult<JsonMessage>> resultHandler) {
    return doExecute(createMessage(data, tag), new DefaultFutureResult<JsonMessage>().setHandler(resultHandler));
  }

  /**
   * Executes a message.
   */
  protected String doExecute(JsonMessage message, Future<JsonMessage> resultFuture) {
    final String id = message.id();
    queueSize++;
    items.put(id, new QueuedItem(message, resultFuture, vertx.setTimer(resultTimeout, new Handler<Long>() {
      @Override
      public void handle(Long timerId) {
        if (items.containsKey(id)) {
          items.remove(id).future.setFailure(new TimeoutException("Execution timed out."));
        }
      }
    })));
    return output.emit(message);
  }

  /**
   * Called when a message has been received.
   */
  protected void doReceive(JsonMessage message) {
    input.ack(message);
    String sourceId = message.ancestor();
    if (items.containsKey(sourceId)) {
      items.get(sourceId).result = message;
    }
  }

  /**
   * Called when a message has been acked.
   */
  protected void doAck(JsonMessage message) {
    queueSize--;
    String id = message.id();
    if (items.containsKey(id)) {
      QueuedItem item = items.remove(id);
      if (item.timerId > 0) {
        vertx.cancelTimer(item.timerId);
      }
      if (item.result != null) {
        item.future.setResult(item.result);
      }
    }
  }

  /**
   * Called when a message has been failed.
   */
  protected void doFail(JsonMessage message) {
    queueSize--;
    String id = message.id();
    if (items.containsKey(id)) {
      QueuedItem item = items.remove(id);
      if (autoRetry) {
        output.emit(item.message);
        queueSize++;
      }
      else {
        item.future.setFailure(new FailureException("Execution failed."));
      }
    }
  }

}
