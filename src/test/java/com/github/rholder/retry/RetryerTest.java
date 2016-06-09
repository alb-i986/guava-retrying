/*
 * Copyright 2012-2015 Ray Holder
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.rholder.retry;

import com.google.common.base.Predicates;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class RetryerTest {

    @Rule
    private ExpectedException expectedException = ExpectedException.none();

    private final Retryer<Object> sut = new Retryer<Object>(
            StopStrategies.stopAfterAttempt(2),
            WaitStrategies.noWait(),
            Predicates.<Attempt<Object>>alwaysFalse());

    @Test
    public void givenBlockThrowingAssertionError() throws Exception {
        sut.call(Callables.throwing(new AssertionError()));
    }

    @Test(expected = Error.class)
    public void givenBlockThrowingError() throws Exception {
        sut.call(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                throw new Error();
            }
        });
    }

    @Test
    public void givenBlockThrowingException() throws Exception {
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(CoreMatchers.);

        sut.call(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                throw new Exception("my exception");
            }
        });
    }

    private static class Callables {

        public static Callable throwing(final Throwable e) {
            return new Callable() {
                @Override
                public Object call() throws Exception {
                    throw e;
                }
            };
        }
    }
}