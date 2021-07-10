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

package me.ahoo.cosid.jdbc;

import me.ahoo.cosid.jdbc.state.*;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Threads;

/**
 * @author ahoo wang
 */
public class MySqlIdBenchmark {

    @Benchmark
    @Threads(2)
    public long step_1(SegmentIdState segmentIdState) {
        return segmentIdState.segmentId.generate();
    }
    @Benchmark
    public long step_100(SegmentId100State segmentId100State) {
        return segmentId100State.segmentId.generate();
    }
    @Benchmark
    public long step_1000(SegmentId1000State segmentId1000State) {
        return segmentId1000State.segmentId.generate();
    }

}