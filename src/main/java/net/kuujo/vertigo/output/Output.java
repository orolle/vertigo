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

import java.util.ArrayList;
import java.util.List;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import net.kuujo.vertigo.input.Input;
import net.kuujo.vertigo.input.Listener;
import net.kuujo.vertigo.input.filter.Filter;
import net.kuujo.vertigo.input.grouping.Grouping;
import net.kuujo.vertigo.network.MalformedNetworkException;
import net.kuujo.vertigo.output.condition.Condition;
import net.kuujo.vertigo.output.selector.Selector;
import net.kuujo.vertigo.serializer.Serializable;
import net.kuujo.vertigo.serializer.SerializationException;
import net.kuujo.vertigo.serializer.Serializer;

/**
 * A component output.
 *
 * The output is the polar opposite of the {@link Input}. When a {@link Listener}
 * subscribes to output from a component, its {@link Input} is transformed into
 * an {@link Output}, which indicates the {@link Selector} and {@link Condition}s
 * that are to be used in emitted messages to that input.
 *
 * @author Jordan Halterman
 */
public class Output implements Serializable {
  public static final String ID = "id";
  public static final String COUNT = "count";
  public static final String SELECTOR = "selector";
  public static final String CONDITIONS = "conditions";

  /**
   * Creates an output from input.
   *
   * @param input
   *   The input from which to create an output.
   * @return
   *   A new output instance.
   * @throws MalformedNetworkException 
   */
  public static Output fromInput(Input input) throws MalformedNetworkException {
    JsonObject definition = new JsonObject();
    definition.putString(ID, input.id());
    definition.putNumber(COUNT, input.getCount());

    Grouping grouping = input.getGrouping();
    if (grouping == null) {
      throw new MalformedNetworkException("Invalid input. No input grouping specified.");
    }

    definition.putObject(SELECTOR, Serializer.serialize(grouping.createSelector()));

    JsonArray conditions = new JsonArray();
    for (Filter filter : input.getFilters()) {
      conditions.add(Serializer.serialize(filter.createCondition()));
    }
    definition.putArray(CONDITIONS, conditions);
    return new Output(definition);
  }

  private JsonObject definition;

  public Output() {
  }

  private Output(JsonObject definition) {
    this.definition = definition;
  }

  /**
   * Returns the output id.
   *
   * @return
   *   The output id.
   */
  public String id() {
    return definition.getString(ID);
  }

  /**
   * Returns the output count.
   *
   * @return
   *   The output connection count.
   */
  public int getCount() {
    return definition.getInteger(COUNT, 1);
  }

  /**
   * Returns the output selector.
   *
   * @return
   *   An output selector.
   */
  public Selector getSelector() {
    JsonObject selectorInfo = definition.getObject(SELECTOR);
    try {
      return selectorInfo != null ? Serializer.<Selector>deserialize(selectorInfo) : null;
    }
    catch (SerializationException e) {
      return null;
    }
  }

  /**
   * Returns a list of conditions for the output.
   *
   * @return
   *   A list of output conditions.
   */
  public List<Condition> getConditions() {
    List<Condition> conditions = new ArrayList<Condition>();
    JsonArray conditionInfos = definition.getArray(CONDITIONS);
    if (conditionInfos == null) {
      return conditions;
    }

    for (Object conditionInfo : conditionInfos) {
      try {
        conditions.add(Serializer.<Condition>deserialize((JsonObject) conditionInfo));
      }
      catch (SerializationException e) {
        // Do nothing.
      }
    }
    return conditions;
  }

  @Override
  public JsonObject getState() {
    return definition;
  }

  @Override
  public void setState(JsonObject state) {
    definition = state;
  }

}
