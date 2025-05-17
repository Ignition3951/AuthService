package com.utk.authservice.service;

import com.utk.authservice.dto.UserInfoDto;
import com.utk.authservice.entities.UserInfo;
import com.utk.authservice.exception.UserDetailsNotValid;
import com.utk.authservice.repositories.UserRepository;
import com.utk.authservice.util.Messages;
import com.utk.authservice.util.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userRepository.findByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new CustomUserDetails(userInfo);
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto) {
        return userRepository.findByUsername(userInfoDto.getUserName());
    }

    public Boolean signUp(UserInfoDto userInfoDto) throws UserDetailsNotValid{
        if (!ValidationUtil.isValidUserDetails(userInfoDto)) {
            throw new UserDetailsNotValid(Messages.INVALID_USER_DETAILS);
        }
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if (Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))) {
            return false;
        }
        String userId = UUID.randomUUID().toString();
        userRepository.save(new UserInfo(userId,
                userInfoDto.getUserName(),
                userInfoDto.getPassword(),
                new HashSet<>()));
        return true;
    }
}
