package com.lvmc.service.impl;

import com.lvmc.entity.MqUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 吕茂陈
 * @date 2022-07-15 14:28
 */
@Service
public class MqUserService {

    private final List<MqUser> users = new ArrayList<>();

    public MqUser register() {
        MqUser user = new MqUser();
        users.add(user);
        return user;
    }

    public List<MqUser> getUsersAfterIdWithLimit(long id, int limit) {
        return users.stream()
                .filter(user -> user.getId() >= id)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
