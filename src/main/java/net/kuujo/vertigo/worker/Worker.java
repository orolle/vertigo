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
package net.kuujo.vertigo.worker;

import net.kuujo.vertigo.component.Component;
import net.kuujo.vertigo.message.JsonMessage;

import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;

/**
 * A worker component.
 *
 * Workers are components that both receive and emit messages. Messages arrive
 * in the form of {@link JsonMessage} instances. Messages should always be either
 * acked or failed. Failing to ack or fail a message may result in the message
 * timing out depending on the network configuration. Since network configurations
 * are abstracted from component implementations, it is important that workers
 * always ack or fail messages. Note that messages should be acked or failed
 * after any child messages have been emitted from the worker.
 *
 * @author Jordan Halterman
 */
public interface Worker<T extends Worker<T>> extends Component<T> {

  /**
   * Sets a worker data handler.
   *
   * @param handler
   *   A message handler. This handler will be called for each message received
   *   by the worker.
   * @return 
   *   The called worker instance.
   */
  T messageHandler(Handler<JsonMessage> handler);

  /**
   * Emits data from the worker.
   *
   * @param data
   *   The data to emit.
   * @return
   *   The emitted message identifier.
   */
  String emit(JsonObject data);

  /**
   * Emits data from the worker with a tag.
   *
   * @param data
   *   The data to emit.
   * @param tag
   *   A tag to apply to the message.
   * @return
   *   The emitted message identifier.
   */
  String emit(JsonObject data, String tag);

  /**
   * Emits child data from the worker.
   *
   * @param data
   *   The data to emit.
   * @param parent
   *   The parent message.
   * @return
   *   The emitted message identifier.
   */
  String emit(JsonObject data, JsonMessage parent);

  /**
   * Emits child data from the worker with a tag.
   *
   * @param data
   *   The data to emit.
   * @param tag
   *   A tag to apply to the message.
   * @param parent
   *   The parent message.
   * @return
   *   The emitted message identifier.
   */
  String emit(JsonObject data, String tag, JsonMessage parent);

  /**
   * Acknowledges processing of a message.
   *
   * @param message
   *   The message to ack.
   */
  void ack(JsonMessage message);

  /**
   * Fails processing of a message.
   *
   * @param message
   *   The message to fail.
   */
  void fail(JsonMessage message);

}
