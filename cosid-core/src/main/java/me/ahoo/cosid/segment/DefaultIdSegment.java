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

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ahoo wang
 */
public class DefaultIdSegment implements IdSegment {

    public static final DefaultIdSegment OVERFLOW = new DefaultIdSegment(IdSegment.SEQUENCE_OVERFLOW, 0, 0, TIME_TO_LIVE_FOREVER);

    /**
     * include
     */
    private final long maxId;
    private final long offset;
    private final int step;
    private final AtomicLong sequence;
    private final long fetchTime;
    private final long ttl;

    public DefaultIdSegment(long maxId, int step) {
        this(maxId, step, System.currentTimeMillis(), TIME_TO_LIVE_FOREVER);
    }

    public DefaultIdSegment(long maxId, int step, long fetchTime, long ttl) {
        this.maxId = maxId;
        this.step = step;
        this.offset = maxId - step;
        this.sequence = new AtomicLong(offset);
        this.fetchTime = fetchTime;
        this.ttl = ttl;
    }

    @Override
    public long getFetchTime() {
        return fetchTime;
    }

    @Override
    public long getTtl() {
        return ttl;
    }

    @Override
    public long getMaxId() {
        return maxId;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public long getSequence() {
        return sequence.get();
    }

    @Override
    public int getStep() {
        return step;
    }

    @Override
    public long incrementAndGet() {
        if (isOverflow()) {
            return SEQUENCE_OVERFLOW;
        }

        final long nextSeq = sequence.incrementAndGet();

        if (isOverflow(nextSeq)) {
            return SEQUENCE_OVERFLOW;
        }
        return nextSeq;
    }

    @Override
    public String toString() {
        return "DefaultIdSegment{" +
                "maxId=" + maxId +
                ", offset=" + offset +
                ", step=" + step +
                ", sequence=" + sequence +
                ", fetchTime=" + fetchTime +
                ", ttl=" + ttl +
                '}';
    }
}