package com.utk.authservice.repositories;

import com.utk.authservice.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserInfo,Long> {

    public UserInfo findByUserName(String username);
}
