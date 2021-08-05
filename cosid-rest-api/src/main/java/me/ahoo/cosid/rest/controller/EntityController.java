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

package me.ahoo.cosid.rest.controller;

import me.ahoo.cosid.rest.entity.FriendlyIdEntity;
import me.ahoo.cosid.rest.entity.LongIdEntity;
import me.ahoo.cosid.rest.repository.EntityRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author ahoo wang
 */
@RestController
@RequestMapping("entities")
public class EntityController {

    private final EntityRepository entityRepository;

    public EntityController(EntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @PostMapping()
    public LongIdEntity create() {
        LongIdEntity entity = new LongIdEntity();
        entityRepository.insert(entity);
        /**
         * {
         *   "id": 208796080181248
         * }
         */
        return entity;
    }

    @PostMapping("/batch")
    public List<FriendlyIdEntity> createBatch() {
        FriendlyIdEntity entity = new FriendlyIdEntity();
        FriendlyIdEntity entity1 = new FriendlyIdEntity();
        FriendlyIdEntity entity2 = new FriendlyIdEntity();
        FriendlyIdEntity entity3 = new FriendlyIdEntity();
        FriendlyIdEntity entity4 = new FriendlyIdEntity();
        List<FriendlyIdEntity> list = Arrays.asList(entity, entity1, entity2, entity3, entity4);
        entityRepository.insertList(list);
        return list;
    }
}
