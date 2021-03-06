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
package net.kuujo.vertigo.cluster;

import net.kuujo.vertigo.context.NetworkContext;
import net.kuujo.vertigo.network.Network;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;

/**
 * A Vertigo cluster.
 *
 * The cluster is the primary interface for deploying Vertigo networks. Clusters
 * handle deploying network coordinators which handle deployment and monitoring
 * of network component instances.
 *
 * @author Jordan Halterman
 */
public interface Cluster {

  /**
   * Deploys a network.
   *
   * @param network
   *   The network definition.
   */
  void deploy(Network network);

  /**
   * Deploys a network.
   *
   * @param network
   *   The network definition.
   * @param doneHandler
   *   An asynchronous result handler to be invoked with a network context.
   */
  void deploy(Network network, Handler<AsyncResult<NetworkContext>> doneHandler);

  /**
   * Shuts down a network.
   *
   * @param context
   *   The network context.
   */
  void shutdown(NetworkContext context);

  /**
   * Shuts down a network, awaiting a result.
   *
   * @param context
   *   The network context.
   * @param doneHandler
   *   An asynchronous result handler to be invoked once the shutdown is complete.
   */
  void shutdown(NetworkContext context, Handler<AsyncResult<Void>> doneHandler);

}
