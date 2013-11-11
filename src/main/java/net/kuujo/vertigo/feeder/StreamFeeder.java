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

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;
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
   * Sets a drain handler on the feeder.
   *
   * @param drainHandler
   *   A handler to be invoked when the feeder is ready to accept new messages.
   * @return
   *   The called feeder instance.
   */
  public StreamFeeder drainHandler(Handler<Void> drainHandler);

  /**
   * Emits data from the feeder.
   *
   * @param data
   *   The data to emit.
   * @param ackHandler
   *   An asynchronous handler to be invoked when the message is acked or failed.
   *   If the message is successfully processed, the handler will be successful.
   *   If the message fails or times out, the handler will be failed.
   * @return
   *   The unique message identifier for the emitted message.
   */
  public String emit(JsonObject data, Handler<AsyncResult<Void>> ackHandler);

  /**
   * Emits data from the feeder.
   *
   * @param data
   *   The data to emit.
   * @param tag
   *   A tag to apply to the emitted message.
   * @param ackHandler
   *   An asynchronous handler to be invoked when the message is acked or failed.
   *   If the message is successfully processed, the handler will be successful.
   *   If the message fails or times out, the handler will be failed.
   * @return
   *   The unique message identifier for the emitted message.
   */
  public String emit(JsonObject data, String tag, Handler<AsyncResult<Void>> ackHandler);

}
