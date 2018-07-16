package com.tuigroup.codewars.data.mapper;

import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.remote.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserMapper {

    @Inject
    public UserMapper() {

    }

    public UserEntity mapFromApiToEntity(User user) {
        UserEntity userEntity = new UserEntity(
                user.getUsername(),
                user.getName(),
                user.getHonor(),
                user.getClan(),
                user.getLeaderboardPosition()
        );
        return userEntity;
    }
}
