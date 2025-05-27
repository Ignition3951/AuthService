package com.utk.authservice.service;

import com.utk.authservice.dto.UserInfoDto;
import com.utk.authservice.entities.UserInfo;
import com.utk.authservice.exception.UserDetailsNotValid;
import com.utk.authservice.kafka.producer.MessageProducer;
import com.utk.authservice.repositories.UserRepository;
import com.utk.authservice.util.Messages;
import com.utk.authservice.util.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@NoArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageProducer messageProducer;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;


    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MessageProducer messageProducer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageProducer = messageProducer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userRepository.findByUserName(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new CustomUserDetails(userInfo);
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto) {
        return userRepository.findByUserName(userInfoDto.getUserName());
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
        userInfoDto.setUserId(userId);
        messageProducer.sendMessage(groupId,userInfoDto);
        userRepository.save(new UserInfo(userId,
                userInfoDto.getUserName(),
                userInfoDto.getPassword(),
                new HashSet<>()));
        return true;
    }
}
