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
package net.kuujo.vertigo.input.filter;

import net.kuujo.vertigo.output.condition.Condition;
import net.kuujo.vertigo.output.condition.FieldCondition;

import org.vertx.java.core.json.JsonObject;

/**
 * A field filter.
 *
 * @author Jordan Halterman
 */
public class FieldFilter implements Filter {
  private JsonObject definition = new JsonObject();

  public FieldFilter() {
    definition = new JsonObject();
  }

  public FieldFilter(String fieldName, Object value) {
    definition.putString("field", fieldName).putValue("value", value);
  }

  /**
   * Sets the filter field.
   *
   * @param fieldName
   *   The filter field name.
   * @return
   *   The called filter instance.
   */
  public FieldFilter setField(String fieldName) {
    definition.putString("field", fieldName);
    return this;
  }

  /**
   * Sets the filter value.
   *
   * @param value
   *   The filter field value.
   * @return
   *   The called filter instance.
   */
  public FieldFilter setValue(Object value) {
    definition.putValue("value", value);
    return this;
  }

  @Override
  public JsonObject getState() {
    return definition;
  }

  @Override
  public void setState(JsonObject state) {
    definition = state;
  }

  @Override
  public Condition createCondition() {
    return new FieldCondition(definition.getString("field"), definition.getValue("value"));
  }

}
