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

import net.kuujo.vertigo.component.Component;

import org.vertx.java.core.json.JsonObject;

/**
 * A message feeder.
 *
 * @author Jordan Halterman
 * @param <T> The feeder type
 */
public interface Feeder<T extends Feeder<T>> extends Component<T> {

  /**
   * Sets the maximum number of messages that can be pending in the network
   * from this feeder.
   *
   * @param maxSize
   *   The maximum feed queue size.
   * @return
   *   The called feeder instance.
   */
  public T setMaxQueueSize(long maxSize);

  /**
   * Gets the maximum number of messages that can be pending in the network
   * from this feeder.
   *
   * @return
   *   The called feeder instance.
   */
  public long getMaxQueueSize();

  /**
   * Indicates whether the feed queue is full.
   *
   * @return
   *   Indicates whether the feed queue is full.
   */
  public boolean queueFull();

  /**
   * Sets auto retry on the feeder.
   *
   * @param autoRetry
   *   Indicates whether to automatically retry sending failed messages.
   * @return
   *   The called feeder instance.
   */
  public T setAutoRetry(boolean autoRetry);

  /**
   * Gets the auto retry setting for the feeder.
   *
   * @return
   *   Indicates whether the feeder will automatically retry sending failed messages.
   */
  public boolean isAutoRetry();

  /**
   * Emits data from the feeder.
   *
   * @param data
   *   The data to emit.
   * @return
   *   The unique message identifier for the emitted message.
   */
  public String emit(JsonObject data);

  /**
   * Emits data from the feeder.
   *
   * @param data
   *   The data to emit.
   * @param tag
   *   A tag to apply to the emitted message.
   * @return
   *   The unique message identifier for the emitted message.
   */
  public String emit(JsonObject data, String tag);

}
