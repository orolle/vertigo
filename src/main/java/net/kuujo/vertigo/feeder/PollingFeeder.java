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

import org.vertx.java.core.Handler;

/**
 * A polling feeder.
 *
 * The polling feeder reads input from code by periodically polling a handler
 * for new data. If the feed handler fails to emit new data when polled, the
 * feeder will reschedule the next feed for a period in the near future.
 *
 * @author Jordan Halterman
 */
public interface PollingFeeder extends Feeder<PollingFeeder> {

  /**
   * Sets the feed delay.
   *
   * The feed delay indicates the interval at which the feeder will attempt to
   * poll the feed handler for new data.
   *
   * @param delay
   *   The empty feed delay.
   * @return
   *   The called feeder instance.
   */
  PollingFeeder setFeedDelay(long delay);

  /**
   * Gets the feed delay.
   *
   * The feed delay indicates the interval at which the feeder will attempt to
   * poll the feed handler for new data.
   *
   * @return
   *   The empty feed delay.
   */
  long getFeedDelay();

  /**
   * Registers a feed handler on the feeder. The feed handler is called each time
   * a new message should be emitted from the feeder. This allows the feeder
   * implementation to control the flow of data emanating from the feeder.
   *
   * @param feedHandler
   *   The feed handler.
   * @return
   *   The called feeder instance.
   */
  public PollingFeeder feedHandler(Handler<PollingFeeder> feedHandler);

  /**
   * Sets an ack handler on the feeder.
   *
   * @param ackHandler
   *   A handler to be called when a message is acked. The handler will be called
   *   with the unique message identifier for the message that was acked.
   * @return
   *   The called feeder instance.
   */
  public PollingFeeder ackHandler(Handler<String> ackHandler);

  /**
   * Sets a fail handler on the feeder.
   *
   * @param failHandler
   *   A handler to be called when a message is failed. The handler will be called
   *   with the unique message identifier for the message that was failed.
   * @return
   *   The called feeder instance.
   */
  public PollingFeeder failHandler(Handler<String> failHandler);

}
