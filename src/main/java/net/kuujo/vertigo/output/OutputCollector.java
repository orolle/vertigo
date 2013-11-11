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
package net.kuujo.vertigo.output;

import net.kuujo.vertigo.message.JsonMessage;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;

/**
 * An output collector.
 *
 * The output collector is the primary interface for emitting new messages from
 * a component. When a new component instance is started, the output collector
 * registers an event bus handler at the component address. This is the address
 * at which other components publish listen requests. When a new listen request
 * is received, the output collector sets up an output {@link Channel} and any
 * new messages emitted from the component will be sent to the new channel as well.
 *
 * @author Jordan Halterman
 */
public interface OutputCollector {

  /**
   * Returns the output address. This should be identical to a component address.
   *
   * @return
   *   The output address.
   */
  String getAddress();

  /**
   * Emits a message to all output channels.
   *
   * @param message
   *   The message to emit.
   * @return
   *   The unique output message correlation identifier. This identifier can be
   *   used to correlate new messages with the emitted message.
   */
  String emit(JsonMessage message);

  /**
   * Sets an ack handler on the output collector.
   *
   * This handler will be called with the correlation identifier of the message
   * that was acked once a message completes processing.
   *
   * @param handler
   *   A handler to be invoked when an ack message is received. 
   * @return
   *   The called output collector instance.
   */
  OutputCollector ackHandler(Handler<JsonMessage> handler);

  /**
   * Sets a fail handler on the output collector.
   *
   * This handler will be called with the correlation identifier of the message
   * that was failed. Not that even if a descendant of the output message was
   * failed, all parent and ancestor messages are failed as well.
   *
   * @param handler
   *   A handler to be invoked when a fail message is received.
   * @return
   *   The called output collector instance.
   */
  OutputCollector failHandler(Handler<JsonMessage> handler);

  /**
   * Starts the output collector.
   *
   * @return
   *   The called output collector instance.
   */
  OutputCollector start();

  /**
   * Starts the output collector.
   *
   * @param doneHandler
   *   An asynchronous handler to be invoked once the collector is started.
   * @return
   *   The called output collector instance.
   */
  OutputCollector start(Handler<AsyncResult<Void>> doneHandler);

  /**
   * Stops the output collector.
   */
  void stop();

  /**
   * Stops the output collector.
   *
   * @param doneHandler
   *   An asynchronous handler to be invoked once the collector is stopped.
   */
  void stop(Handler<AsyncResult<Void>> doneHandler);

}
