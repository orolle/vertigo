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
import org.vertx.java.core.streams.ReadStream;
import org.vertx.java.core.streams.WriteStream;

/**
 * A stream feeder.
 *
 * The stream feeder is designed to integrate with Vert.x {@link ReadStream}
 * APIs by mimicking the {@link WriteStream} interface.
 *
 * @author Jordan Halterman
 */
public interface StreamFeeder extends Feeder<StreamFeeder> {

  /**
   * Sets a full handler on the feeder.
   *
   * The full handler will be called when the feed queue becomes full.
   *
   * @param handler
   *   A handler to be invoked when the feed queue is full.
   * @return
   *   The called feeder instance.
   */
  StreamFeeder fullHandler(Handler<Void> handler);

  /**
   * Sets a drain handler on the feeder.
   *
   * The drain handler will be called when the feed queue is available to
   * receive new messages.
   *
   * @param handler
   *   A handler to be invoked when a full feed queue is emptied.
   * @return
   *   The called feeder instance.
   */
  StreamFeeder drainHandler(Handler<Void> handler);

}
