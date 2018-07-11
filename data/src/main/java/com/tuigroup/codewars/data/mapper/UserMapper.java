package com.tuigroup.codewars.data.mapper;

import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.remote.model.User;

public class UserMapper {

    public static UserEntity mapFromApiToEntity(User user) {
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
