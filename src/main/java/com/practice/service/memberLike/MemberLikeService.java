package com.practice.service.memberLike;

import com.practice.domain.compositekeys.MemberLikeId;
import com.practice.domain.entity.Attachment;
import com.practice.domain.entity.MemberLike;
import com.practice.domain.entity.User;
import com.practice.exception.NotFoundException;
import com.practice.exception.UserNotFoundException;
import com.practice.repository.AuthRepository;
import com.practice.repository.MemberLikeRepository;
import com.practice.service.AttachmentService;
import com.practice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberLikeService {

    private final UserService userService;
    private final AuthRepository authRepository;
    private final MemberLikeRepository repository;
    private final AttachmentService attachmentService;

    @Transactional
    public void save(User user, Long id) throws UserNotFoundException {
        Attachment attachment = attachmentService.findById(id);

        if (attachment == null ||  user == null)
            throw new UserNotFoundException("UserNotFoundException");

        // Original-Sources
        //boolean alreadyLikes = user.getMemberLikes().stream().anyMatch(m -> m.getAttachment().getId().equals(id));
        boolean alreadyLikes = false;

        if (alreadyLikes) {
            MemberLike memberLike = user.getMemberLikes().stream().filter(m -> m.getAttachment().getId().equals(id)).findFirst().orElseThrow(() -> new UserNotFoundException("UserNotFoundException"));
            user.getMemberLikes().remove(memberLike);
        } else {
            MemberLikeId memberLikeId = setMemberLikeCompositekey(user, id);

            MemberLike memberLike = new MemberLike();
            memberLike.setMemberLikeId(memberLikeId);
            memberLike.setMember(user);
            memberLike.setAttachment(attachment);

            /**  Original-Sources
            user.getMemberLikes().add(memberLike);
            authRepository.save(user);
             **/
            repository.save(memberLike);
        }
    }

    @Transactional
    public void cancel(User user, Long id) throws NotFoundException {
        Attachment attachment = attachmentService.findById(id);

        if (attachment == null ||  user == null)
            throw new UserNotFoundException("UserNotFoundException");

        MemberLike memberLike = user.getMemberLikes().stream().filter(m -> m.getAttachment().getId().equals(id)).findFirst().orElseThrow(() -> new UserNotFoundException("UserNotFoundException"));

        /** Original-Sources
        user.getMemberLikes().remove(memberLike);
        authRepository.save(user);
         **/
        repository.deleteById(memberLike.getMemberLikeId());
    }

    @Transactional(readOnly = true)
    MemberLikeId setMemberLikeCompositekey(User user, Long attachmentId) {
        MemberLikeId id = new MemberLikeId();
        id.setMemberId(user.getId());
        id.setAttachmentId(attachmentId);

        return id;
    }
}
