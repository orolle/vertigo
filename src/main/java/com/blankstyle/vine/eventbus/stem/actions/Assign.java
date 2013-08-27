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
package com.blankstyle.vine.eventbus.stem.actions;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.JsonObject;

import com.blankstyle.vine.context.StemContext;
import com.blankstyle.vine.eventbus.Action;
import com.blankstyle.vine.eventbus.Argument;
import com.blankstyle.vine.eventbus.ArgumentsDefinition;
import com.blankstyle.vine.eventbus.AsynchronousAction;
import com.blankstyle.vine.eventbus.ReliableEventBus;

/**
 * Assign worker action.
 *
 * @author Jordan Halterman
 */
public class Assign extends Action<StemContext> implements AsynchronousAction<Void> {

  public static final String NAME = "assign";

  private static ArgumentsDefinition args = new ArgumentsDefinition() {{
    addArgument(new Argument<JsonObject>() {
      @Override
      public String name() {
        return "seed";
      }
      @Override
      public boolean isValid(JsonObject value) {
        return value instanceof JsonObject;
      }
    });
  }};

  public Assign(Vertx vertx, ReliableEventBus eventBus, StemContext context) {
    super(vertx, eventBus, context);
  }

  @Override
  public ArgumentsDefinition getArgumentsDefinition() {
    return args;
  }

  @Override
  public void execute(Object[] args, Handler<AsyncResult<Object>> resultHandler) {
    
  }

}
