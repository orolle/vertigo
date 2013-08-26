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
package com.blankstyle.vine.messaging;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

/**
 * A reliable communication channel.
 *
 * @author Jordan Halterman
 */
public interface ReliableChannel extends Channel {

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Object message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Object message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Object message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Object message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(JsonObject message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(JsonObject message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(JsonObject message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(JsonObject message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(JsonArray message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(JsonArray message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(JsonArray message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(JsonArray message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Buffer message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Buffer message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Buffer message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Buffer message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(byte[] message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(byte[] message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(byte[] message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(byte[] message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(String message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(String message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(String message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(String message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Integer message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Integer message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Integer message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Integer message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Long message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Long message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Long message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Long message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Float message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Float message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Float message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Float message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Double message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Double message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Double message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Double message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Boolean message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Boolean message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Boolean message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Boolean message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Short message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Short message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Short message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Short message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Character message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Character message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Character message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Character message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Byte message, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Byte message, long timeout, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Byte message, long timeout, boolean retry, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Sends a message through the channel, providing a handler for a return value.
   *
   * @param message
   *   The message to publish.
   * @param timeout
   *   A message timeout.
   * @param retry
   *   A boolean indicating whether the message should be resent if a timeout occurs.
   * @param attempts
   *   The maximum number of retry attempts before an exception is thrown.
   * @param resultHandler
   *   A handler to be invoked when message receipt is acknowledged.
   */
  public void publish(Byte message, long timeout, boolean retry, int attempts, Handler<AsyncResult<Void>> resultHandler);

}