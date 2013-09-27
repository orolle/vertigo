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
package net.kuujo.vevent.node;

import org.vertx.java.core.Handler;

/**
 * A stream feeder.
 *
 * @author Jordan Halterman
 */
public interface StreamFeeder extends Feeder {

  /**
   * Returns a boolean value indicating whether the feed queue is full.
   */
  public boolean feedQueueFull();

  /**
   * Sets a feed queue drain handler.
   *
   * @param handler
   *   The drain handler.
   * @return
   *   The called feeder instance.
   */
  public StreamFeeder drainHandler(Handler<Void> handler);

}
