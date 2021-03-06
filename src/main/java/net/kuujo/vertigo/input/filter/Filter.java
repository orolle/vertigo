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
import net.kuujo.vertigo.serializer.Serializable;

/**
 * A message filter.
 *
 * Filters allow inputs to specify the types of messages in which they are
 * interested in receiving. When an input prepares to register with the interesting
 * component's output, the filter will be converted to a {@link Condition}. This
 * condition is used to validate that the input component is interested in a
 * message before it is even sent.
 *
 * @author Jordan Halterman
 */
public interface Filter extends Serializable {

  /**
   * Creates a condition for the filter.
   *
   * @return
   *   An output condition.
   */
  Condition createCondition();

}
