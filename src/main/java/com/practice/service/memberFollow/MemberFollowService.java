package com.practice.service.memberFollow;

import com.practice.domain.compositekeys.MemberFollowId;
import com.practice.domain.dto.MemberFollowDto;
import com.practice.domain.entity.MemberFollow;
import com.practice.domain.entity.User;
import com.practice.exception.NotFoundException;
import com.practice.exception.UserNotFoundException;
import com.practice.repository.AuthRepository;
import com.practice.repository.MemberFollowRepository;
import com.practice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberFollowService {

    private final UserService userService;
    private final MemberFollowRepository repository;
    private final AuthRepository authRepository;

    @Transactional(readOnly = true)
    public List<MemberFollowDto> getMemberFollows(User user) {
        return repository.findByMember(user).stream().map(MemberFollowDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void save(User user, Long id) throws UserNotFoundException {
        User follower = userService.findById(id);

        if (follower == null ||  user == null)
            throw new UserNotFoundException("UserNotFoundException");

        // Original Sources
        //boolean alreadyFollow = user.getMemberFollowings().stream().anyMatch(m -> m.getFollower().getId().equals(id));
        boolean alreadyFollow = false;

        if (alreadyFollow) {
            MemberFollow memberFollow = user.getMemberFollowings().stream().filter(m -> m.getFollower().getId().equals(id)).findFirst().orElseThrow(() -> new UserNotFoundException("UserNotFoundException"));
            user.getMemberFollowings().remove(memberFollow);
        } else {
            MemberFollowId memberFollowId = setMemberFollowCompositekey(user, id);

            MemberFollow memberFollow = new MemberFollow();
            memberFollow.setMemberFollowId(memberFollowId);
            memberFollow.setMember(user);
            memberFollow.setFollower(follower);

            user.getMemberFollowings().add(memberFollow);
        }

        authRepository.save(user);
    }

    @Transactional
    public void cancel(User user, Long id) throws NotFoundException {
        User follower = userService.findById(id);

        if (follower == null ||  user == null)
            throw new UserNotFoundException("UserNotFoundException");

        MemberFollow memberFollow = user.getMemberFollowings().stream().filter(m -> m.getFollower().getId().equals(id)).findFirst().orElseThrow(() -> new UserNotFoundException("UserNotFoundException"));

        user.getMemberFollowings().remove(memberFollow);

        //repository.deleteById(memberFollow.getMemberFollowId());

        authRepository.save(user);
    }

    @Transactional(readOnly = true)
    MemberFollowId setMemberFollowCompositekey(User user, Long followerId) throws UserNotFoundException {
        MemberFollowId id = new MemberFollowId();
        id.setMemberId(user.getId());
        id.setFollowerId(followerId);

        return id;
    }
}
