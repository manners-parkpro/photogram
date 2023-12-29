package com.practice.service.auth;

import com.practice.domain.dto.UserDto;
import com.practice.domain.entity.User;
import com.practice.exception.AlreadyEntity;
import com.practice.exception.InvalidPropertyException;
import com.practice.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long save(UserDto dto) throws AlreadyEntity, InvalidParameterException {

        if (dto == null)
            throw new InvalidParameterException();

        Optional<User> optUser = repository.findByUsernameIgnoreCaseOrEmailIgnoreCase(dto.getUsername(), dto.getEmail());

        if (optUser.isPresent())
            throw new AlreadyEntity("AlreadyEntity");

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (StringUtils.isNotBlank(dto.getPhone()))
            user.setPhone(dto.getPhone());

        if (dto.getActiveYn() != null)
            user.setActiveYn(dto.getActiveYn());

        if (dto.getDormantYn() != null)
            user.setDormantYn(dto.getDormantYn());

        if (dto.getDeleteYn() != null)
            user.setActiveYn(dto.getDeleteYn());

        user.setLastLoginAt(LocalDateTime.now());

        repository.save(user);

        return user.getId();
    }
}
