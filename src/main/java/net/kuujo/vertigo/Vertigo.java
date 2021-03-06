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
package net.kuujo.vertigo;

import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;
import org.vertx.java.platform.Verticle;

import net.kuujo.vertigo.context.InstanceContext;
import net.kuujo.vertigo.feeder.BasicFeeder;
import net.kuujo.vertigo.feeder.DefaultBasicFeeder;
import net.kuujo.vertigo.feeder.DefaultPollingFeeder;
import net.kuujo.vertigo.feeder.DefaultStreamFeeder;
import net.kuujo.vertigo.feeder.PollingFeeder;
import net.kuujo.vertigo.feeder.StreamFeeder;
import net.kuujo.vertigo.network.Network;
import net.kuujo.vertigo.rpc.BasicExecutor;
import net.kuujo.vertigo.rpc.DefaultBasicExecutor;
import net.kuujo.vertigo.rpc.DefaultPollingExecutor;
import net.kuujo.vertigo.rpc.DefaultStreamExecutor;
import net.kuujo.vertigo.rpc.PollingExecutor;
import net.kuujo.vertigo.rpc.StreamExecutor;
import net.kuujo.vertigo.worker.BasicWorker;
import net.kuujo.vertigo.worker.DefaultBasicWorker;

/**
 * Primary Vert.igo API.
 *
 * This is the primary API for creating Vertigo objects within component
 * implementations. This should be used to instantiate any feeders, workers, or
 * executors that are used by the component implementation.
 *
 * @author Jordan Halterman
 */
public final class Vertigo {
  private Vertx vertx;
  private Container container;
  private InstanceContext context;

  public Vertigo(Verticle verticle) {
    this(verticle.getVertx(), verticle.getContainer());
  }

  public Vertigo(Vertx vertx, Container container) {
    InstanceContext context = null;
    JsonObject config = container.config();
    if (config != null && config.getFieldNames().contains("__context__")) {
      JsonObject contextInfo = config.getObject("__context__");
      context = InstanceContext.fromJson(contextInfo);
      config.removeField("__context__");
    }
    this.vertx = vertx;
    this.container = container;
    this.context = context;
  }

  /**
   * Indicates whether this verticle was deployed as a component instance.
   *
   * @return
   *  Indicates whether this verticle is a Vertigo component instance.
   */
  public boolean isComponent() {
    return context != null;
  }

  /**
   * Returns the current Vertigo instance context (if any).
   *
   * @return
   *   The current Vertigo instance context.
   */
  public InstanceContext getContext() {
    return context;
  }

  /**
   * Creates a new network.
   *
   * @param address
   *   The network address.
   * @return
   *   A new network instance.
   */
  public Network createNetwork(String address) {
    return new Network(address);
  }

  /**
   * Creates a basic feeder.
   *
   * @return
   *   A new feeder instance.
   */
  public BasicFeeder createFeeder() {
    return createBasicFeeder();
  }

  /**
   * Creates a basic feeder.
   *
   * @return
   *   A new basic feeder instance.
   */
  public BasicFeeder createBasicFeeder() {
    return new DefaultBasicFeeder(vertx, container, context);
  }

  /**
   * Creates a polling feeder.
   *
   * @return
   *   A new poll feeder instance.
   */
  public PollingFeeder createPollingFeeder() {
    return new DefaultPollingFeeder(vertx, container, context);
  }

  /**
   * Creates a stream feeder.
   *
   * @return
   *   A new stream feeder instance.
   */
  public StreamFeeder createStreamFeeder() {
    return new DefaultStreamFeeder(vertx, container, context);
  }

  /**
   * Creates a basic executor.
   *
   * @return
   *   A new basic executor instance.
   */
  public BasicExecutor createExecutor() {
    return createBasicExecutor();
  }

  /**
   * Creates a basic executor.
   *
   * @return
   *   A new basic executor instance.
   */
  public BasicExecutor createBasicExecutor() {
    return new DefaultBasicExecutor(vertx, container, context);
  }

  /**
   * Creates a polling executor.
   *
   * @return
   *   A new polling executor instance.
   */
  public PollingExecutor createPollingExecutor() {
    return new DefaultPollingExecutor(vertx, container, context);
  }

  /**
   * Creates a stream executor.
   *
   * @return
   *   A new stream executor instance.
   */
  public StreamExecutor createStreamExecutor() {
    return new DefaultStreamExecutor(vertx, container, context);
  }

  /**
   * Creates a basic worker.
   *
   * @return
   *   A new worker instance.
   */
  public BasicWorker createWorker() {
    return createBasicWorker();
  }

  /**
   * Creates a basic worker.
   *
   * @return
   *   A new worker instance.
   */
  public BasicWorker createBasicWorker() {
    return new DefaultBasicWorker(vertx, container, context);
  }

}
