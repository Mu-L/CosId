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

package me.ahoo.cosid.snowflake.machine;


import me.ahoo.cosid.snowflake.MillisecondSnowflakeId;

import javax.annotation.concurrent.ThreadSafe;

/**
 * @author ahoo wang
 */
@ThreadSafe
public interface MachineIdDistributor {

    default int maxMachineId(int machineBit) {
        return -1 ^ (-1 << machineBit);
    }

    default int totalMachineIds(int machineBit) {
        return maxMachineId(machineBit) + 1;
    }

    /**
     * @param namespace
     * @param machineBit
     * @param instanceId
     * @return
     * @throws MachineIdOverflowException
     */
    int distribute(String namespace, int machineBit, InstanceId instanceId) throws MachineIdOverflowException;

    default int distribute(String namespace, InstanceId instanceId) throws MachineIdOverflowException {
        return distribute(namespace, MillisecondSnowflakeId.DEFAULT_MACHINE_BIT, instanceId);
    }

    void revert(String namespace, InstanceId instanceId) throws MachineIdOverflowException;


}
