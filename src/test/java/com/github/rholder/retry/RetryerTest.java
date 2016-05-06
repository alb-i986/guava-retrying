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
import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.*;

public class RetryerTest {

    private final Retryer<Object> sut = new Retryer<Object>(
            StopStrategies.stopAfterAttempt(2),
            WaitStrategies.noWait(),
            Predicates.<Attempt<Object>>alwaysFalse());

    @Test
    public void givenBlockThrowingAssertionError() throws Exception {
        sut.call(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                throw new AssertionError();
            }
        });
    }

    @Test(expected = OutOfMemoryError.class)
    public void givenBlockThrowingOOMError() throws Exception {
        sut.call(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                throw new OutOfMemoryError();
            }
        });
    }

}