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
package net.kuujo.vertigo.message;

import java.util.UUID;

import org.vertx.java.core.json.JsonObject;

/**
 * A default JSON message implementation.
 *
 * @author Jordan Halterman
 */
public class DefaultJsonMessage implements JsonMessage {
  private JsonObject body;

  private static String createUniqueId() {
    return UUID.randomUUID().toString();
  }

  public DefaultJsonMessage() {
  }

  private DefaultJsonMessage(JsonObject body) {
    this.body = body;
  }

  /**
   * Creates a new JSON message.
   *
   * @param body
   *   The message body.
   * @return
   *   A new JSON message instance.
   */
  public static JsonMessage create(JsonObject body, String auditor) {
    return new DefaultJsonMessage(new JsonObject().putString("id", createUniqueId()).putObject("body", body).putString("auditor", auditor));
  }

  /**
   * Creates a new JSON message.
   *
   * @param body
   *   The message body.
   * @param tag
   *   A tag to apply to the message.
   * @return
   *   A new JSON message instance.
   */
  public static JsonMessage create(JsonObject body, String tag, String auditor) {
    return new DefaultJsonMessage(new JsonObject().putString("id", createUniqueId()).putObject("body", body).putString("tag", tag).putString("auditor", auditor));
  }

  /**
   * Creates a new JSON message.
   *
   * @param source
   *   The message source.
   * @param body
   *   The message body.
   * @return
   *   A new JSON message instance.
   */
  public static JsonMessage create(String source, JsonObject body, String auditor) {
    return new DefaultJsonMessage(new JsonObject().putString("id", createUniqueId()).putString("source", source).putObject("body", body).putString("auditor", auditor));
  }

  /**
   * Creates a new JSON message.
   *
   * @param source
   *   The message source.
   * @param body
   *   The message body.
   * @param tag
   *   A tag to apply to the message.
   * @return
   *   A new JSON message instance.
   */
  public static JsonMessage create(String source, JsonObject body, String tag, String auditor) {
    return new DefaultJsonMessage(new JsonObject().putString("id", createUniqueId()).putString("source", source).putObject("body", body).putString("tag", tag).putString("auditor", auditor));
  }

  @Override
  public final String id() {
    return body.getString("id");
  }

  @Override
  public final String source() {
    return body.getString("source");
  }

  @Override
  public final String parent() {
    return body.getString("parent");
  }

  @Override
  public final String ancestor() {
    return body.getString("ancestor");
  }

  @Override
  public final String auditor() {
    return body.getString("auditor");
  }

  @Override
  public final JsonObject body() {
    return body.getObject("body");
  }

  @Override
  public final String tag() {
    return body.getString("tag");
  }

  @Override
  public final JsonMessage createChild() {
    JsonObject newMessage = this.body.copy();
    if (!newMessage.getFieldNames().contains("ancestor")) {
      newMessage.putString("ancestor", this.body.getString("id"));
    }
    newMessage.putString("parent", this.body.getString("id"));
    newMessage.putString("id", createUniqueId());
    return new DefaultJsonMessage(newMessage);
  }

  @Override
  public final JsonMessage createChild(JsonObject body) {
    JsonObject newMessage = this.body.copy();
    if (!newMessage.getFieldNames().contains("ancestor")) {
      newMessage.putString("ancestor", this.body.getString("id"));
    }
    newMessage.putString("parent", this.body.getString("id"));
    newMessage.putString("id", createUniqueId())
      .putObject("body", body);
    return new DefaultJsonMessage(newMessage);
  }

  @Override
  public final JsonMessage createChild(JsonObject body, String tag) {
    JsonObject newMessage = this.body.copy();
    if (!newMessage.getFieldNames().contains("ancestor")) {
      newMessage.putString("ancestor", this.body.getString("id"));
    }
    newMessage.putString("parent", this.body.getString("id"));
    newMessage.putString("id", createUniqueId())
      .putObject("body", body)
      .putString("tag", tag);
    return new DefaultJsonMessage(newMessage);
  }

  @Override
  public final JsonMessage copy() {
    return new DefaultJsonMessage(body.copy().putString("id", createUniqueId()));
  }

  @Override
  public void setState(JsonObject state) {
    body = state;
  }

  @Override
  public JsonObject getState() {
    return body;
  }

}
