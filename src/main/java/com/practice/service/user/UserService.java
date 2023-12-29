package com.practice.service.user;

import com.practice.domain.dto.UserDto;
import com.practice.domain.entity.User;
import com.practice.exception.UserNotFoundException;
import com.practice.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.InvalidParameterException;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findById(Long id) throws UserNotFoundException {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("UserNotFoundException"));
    }

    @Transactional
    public Long modifyUser(UserDto dto) throws UserPrincipalNotFoundException, UserNotFoundException {

        if (dto == null)
            throw new InvalidParameterException();

        User user = repository.findById(dto.getId()).orElseThrow(() -> new UserNotFoundException("UserNotFoundException"));

        user.setEmail(dto.getEmail());

        if (StringUtils.isNotBlank(dto.getPassword()))
            user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setIntroDuction(dto.getIntroDuction());
        user.setPhone(dto.getPhone());

        repository.save(user);

        return user.getId();
    }

    @Transactional(readOnly = true)
    public UserDto findByUserToDto(User user, Long id) throws UserNotFoundException {
        boolean isMe = id.equals(user.getId());

        if (isMe)
            return new UserDto(user, true);
        else
            return new UserDto(findById(id), true);
    }
}
