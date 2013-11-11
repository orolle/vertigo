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
package net.kuujo.vertigo.rpc;

import org.vertx.java.core.Handler;
import org.vertx.java.core.streams.ReadStream;
import org.vertx.java.core.streams.WriteStream;

/**
 * A stream executor.
 *
 * The stream executor is designed to integrate with Vert.x {@link ReadStream}
 * APIs by mimicking the {@link WriteStream} interface.
 *
 * @author Jordan Halterman
 */
public interface StreamExecutor extends Executor<StreamExecutor> {

  /**
   * Sets a drain handler on the executor.
   *
   * The drain handler will be called when the execute queue is available to
   * receive new messages.
   *
   * @param handler
   *   A handler to be invoked when a full execute queue is emptied.
   * @return
   *   The called executor instance.
   */
  StreamExecutor drainHandler(Handler<Void> handler);

}
