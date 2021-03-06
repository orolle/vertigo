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
package net.kuujo.vertigo.output.condition;

import net.kuujo.vertigo.message.JsonMessage;
import net.kuujo.vertigo.serializer.Serializable;

/**
 * An output condition.
 *
 * An output condition is the counterpart of the input filter. Once an input is
 * used to subscribe to the output of another component, any filters within the
 * input become output conditions. Conditions are used to validate whether any
 * given message is interesting to the target component.
 *
 * @author Jordan Halterman
 */
public interface Condition extends Serializable {

  /**
   * Returns a boolean indicating whether the given message is valid.
   *
   * @param message
   *   The message to validate.
   * @return
   *   Indicates whether the given message is valid.
   */
  boolean isValid(JsonMessage message);

}
