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

import net.kuujo.vertigo.component.ComponentBase;
import net.kuujo.vertigo.context.InstanceContext;

import org.vertx.java.core.Vertx;
import org.vertx.java.platform.Container;

/**
 * An abstract feeder.
 *
 * @author Jordan Halterman
 * @param <T> The feeder type
 */
public abstract class AbstractFeeder<T extends Feeder<T>> extends ComponentBase<T> implements Feeder<T> {
  private static final long DEFAULT_MAX_QUEUE_SIZE = 1000;
  protected long maxQueueSize = DEFAULT_MAX_QUEUE_SIZE;
  protected long queueSize;
  protected boolean autoRetry;

  protected AbstractFeeder(Vertx vertx, Container container, InstanceContext context) {
    super(vertx, container, context);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T setMaxQueueSize(long maxSize) {
    maxQueueSize = maxSize;
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
  @SuppressWarnings("unchecked")
  public T setAutoRetry(boolean autoRetry) {
    this.autoRetry = autoRetry;
    return (T) this;
  }

  @Override
  public boolean isAutoRetry() {
    return autoRetry;
  }

  /**
   * Enqueues a message. Subclasses may override this method to provide alternative behavior.
   */
  protected String enqueue(String messageId) {
    queueSize++;
    return messageId;
  }

  /**
   * Dequeues a message. Subclasses may override this method to provide alternative behavior.
   */
  protected String dequeue(String messageId) {
    queueSize--;
    return messageId;
  }

}
