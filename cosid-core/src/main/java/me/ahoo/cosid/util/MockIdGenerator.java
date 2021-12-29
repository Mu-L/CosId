/*
 * Copyright [2021-2021] [ahoo wang <ahoowang@qq.com> (https://github.com/Ahoo-Wang)].
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.ahoo.cosid.util;

import me.ahoo.cosid.IdGenerator;
import me.ahoo.cosid.StringIdGenerator;
import me.ahoo.cosid.converter.PrefixIdConverter;
import me.ahoo.cosid.converter.Radix62IdConverter;
import me.ahoo.cosid.snowflake.MillisecondSnowflakeId;

/**
 * @author ahoo wang
 */
public class MockIdGenerator extends StringIdGenerator {

    private static final String TEST_PREFIX = "test_";

    public static final IdGenerator INSTANCE = new MockIdGenerator(TEST_PREFIX);

    public MockIdGenerator(String prefix) {
        super(new MillisecondSnowflakeId(1,0), new PrefixIdConverter(prefix, Radix62IdConverter.INSTANCE));
    }
}