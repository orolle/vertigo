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
package net.kuujo.vertigo.input.grouping;

import net.kuujo.vertigo.output.selector.Selector;
import net.kuujo.vertigo.serializer.Serializable;

/**
 * An input grouping.
 *
 * Input groupings define how messages are to be distributed among multiple
 * instance of a component. When an input prepares to register with the interesting
 * component's output, the grouping will be converted into a {@link Selector}.
 * This selector is used to select specific component instances to which to send
 * each message.
 *
 * @author Jordan Halterman
 */
public interface Grouping extends Serializable {

  /**
   * Creates an output selector from the grouping.
   *
   * @return
   *   An output selector.
   */
  Selector createSelector();

}
