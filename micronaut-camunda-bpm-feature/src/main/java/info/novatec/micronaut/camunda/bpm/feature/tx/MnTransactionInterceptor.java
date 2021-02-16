/*
 * Copyright 2020-2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.novatec.micronaut.camunda.bpm.feature.tx;

import io.micronaut.transaction.SynchronousTransactionManager;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandInterceptor;

import java.sql.Connection;

import static io.micronaut.transaction.TransactionDefinition.Propagation;
import static io.micronaut.transaction.TransactionDefinition.of;

/**
 * {@link CommandInterceptor} implementation which executes the command encapsulated in a transaction with the given {@link Propagation}.
 *
 * @author Tobias Schäfer
 */
public class MnTransactionInterceptor extends CommandInterceptor {

    protected final SynchronousTransactionManager<Connection> transactionManager;
    protected final Propagation propagation;

    public MnTransactionInterceptor(SynchronousTransactionManager<Connection> transactionManager, Propagation propagation) {
        this.transactionManager = transactionManager;
        this.propagation = propagation;
    }

    @Override
    public <T> T execute(Command<T> command) {
        return transactionManager.execute(of(propagation), transactionStatus -> next.execute(command));
    }
}
