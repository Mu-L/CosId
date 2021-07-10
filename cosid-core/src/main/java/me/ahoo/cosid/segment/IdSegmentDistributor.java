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

package me.ahoo.cosid.segment;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

import static me.ahoo.cosid.segment.IdSegment.TIME_TO_LIVE_FOREVER;

/**
 * @author ahoo wang
 */
public interface IdSegmentDistributor {
    int DEFAULT_SEGMENTS = 1;
    int DEFAULT_STEP = 100;

    String getNamespace();

    String getName();

    default String getNamespacedName() {
        return getNamespace() + "." + getName();
    }

    int getStep();

    default int getStep(int segments) {
        return getStep() * segments;
    }

    long nextMaxId(int step);

    default long nextMaxId() {
        return nextMaxId(getStep());
    }

    default IdSegment nextIdSegment() {
        return nextIdSegment(TIME_TO_LIVE_FOREVER);
    }

    default IdSegment nextIdSegment(long ttl) {
        final long maxId = nextMaxId();
        return new DefaultIdSegment(maxId, getStep(), System.currentTimeMillis(), ttl);
    }

    default List<IdSegment> nextIdSegment(int segments) {
        return nextIdSegment(segments, TIME_TO_LIVE_FOREVER);
    }

    default List<IdSegment> nextIdSegment(int segments, long ttl) {
        final int totalStep = getStep(segments);
        final long maxId = nextMaxId(totalStep);
        final long offset = maxId - totalStep;
        List<IdSegment> idSegments = new ArrayList<>(segments);
        for (int i = 0; i < segments; i++) {
            long currentMaxId = offset + getStep() * (i + 1);
            DefaultIdSegment segment = new DefaultIdSegment(currentMaxId, getStep(), System.currentTimeMillis(), ttl);
            idSegments.add(segment);
        }
        return idSegments;
    }

    default IdSegmentChain nextIdSegmentChain(IdSegmentChain previousChain) {
        return nextIdSegmentChain(previousChain, TIME_TO_LIVE_FOREVER);
    }

    default IdSegmentChain nextIdSegmentChain(IdSegmentChain previousChain, long ttl) {
        IdSegment nextIdSegment = nextIdSegment(ttl);
        return new IdSegmentChain(previousChain, nextIdSegment);
    }

    default IdSegmentChain nextIdSegmentChain(IdSegmentChain previousChain, int segments) {
        return nextIdSegmentChain(previousChain, segments, TIME_TO_LIVE_FOREVER);
    }

    default IdSegmentChain nextIdSegmentChain(IdSegmentChain previousChain, int segments, long ttl) {
        if (DEFAULT_SEGMENTS == segments) {
            return nextIdSegmentChain(previousChain, ttl);
        }
        List<IdSegment> nextIdSegments = nextIdSegment(segments, ttl);
        IdSegmentChain rootChain = null;
        IdSegmentChain currentChain = null;
        for (IdSegment nextIdSegment : nextIdSegments) {
            if (Objects.isNull(rootChain)) {
                rootChain = new IdSegmentChain(previousChain, nextIdSegment);
                currentChain = rootChain;
                continue;
            }
            currentChain.setNext(new IdSegmentChain(currentChain, nextIdSegment));
            currentChain = currentChain.getNext();
        }
        return rootChain;
    }

    class Atomic implements IdSegmentDistributor {
        private final int step;
        private final String name;
        private final AtomicLong adder = new AtomicLong();

        public Atomic() {
            this(DEFAULT_STEP);
        }

        public Atomic(int step) {
            this.step = step;
            this.name = "atomic__" + UUID.randomUUID();
        }

        @Override
        public String getNamespace() {
            return "__";
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getStep() {
            return step;
        }

        @Override
        public long nextMaxId(int step) {
            return adder.addAndGet(step);
        }

    }

    @VisibleForTesting
    class Mock implements IdSegmentDistributor {
        private final int step;
        private final String name;
        private final long ioWaiting;
        private final AtomicLong adder = new AtomicLong();

        public Mock() {
            this(DEFAULT_STEP, 220000);
        }

        /**
         * @param step 单次获取IdSegment的区间长度
         * @param tps  发号器的TPS，用于模拟网络IO请求的等待时常
         */
        public Mock(int step, int tps) {
            this.step = step;
            this.ioWaiting = TimeUnit.SECONDS.toNanos(1) / tps;
            this.name = "mock__" + UUID.randomUUID();
        }

        @Override
        public String getNamespace() {
            return "__";
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getStep() {
            return step;
        }

        @Override
        public long nextMaxId(int step) {
            LockSupport.parkNanos(ioWaiting);
            return adder.addAndGet(step);
        }

    }
}