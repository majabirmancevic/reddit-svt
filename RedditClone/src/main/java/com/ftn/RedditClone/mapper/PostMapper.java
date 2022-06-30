package com.ftn.RedditClone.mapper;

import com.ftn.RedditClone.model.entity.*;
import com.ftn.RedditClone.model.entity.dto.PostRequest;
import com.ftn.RedditClone.model.entity.dto.PostResponse;
import com.ftn.RedditClone.repository.CommentRepository;
import com.ftn.RedditClone.repository.ReactionRepository;
import com.ftn.RedditClone.security.TokenUtils;
import com.ftn.RedditClone.service.UserService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static com.ftn.RedditClone.model.entity.ReactionType.DOWNVOTE;
import static com.ftn.RedditClone.model.entity.ReactionType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract  class PostMapper {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    TokenUtils tokenUtils;
    @Autowired
    ReactionRepository reactionRepository;
    @Autowired
    UserService userService;

    @Mapping(target = "id", source = "postRequest.postId")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "text", source = "postRequest.text")
    @Mapping(target = "title", source = "postRequest.postName")
    @Mapping(target = "community", source = "community")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "reactionCount", constant = "1")
    public abstract Post map(PostRequest postRequest, Community community, User user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "postName", source = "title")
    @Mapping(target = "communityName", source = "community.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreationDate().toEpochDay());
    }

    private boolean checkVoteType(Post post, ReactionType reactionType) {
        if (tokenUtils.isLoggedIn()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails) principal).getUsername();
            User user = userService.findByUsername(username);

            Optional<Reaction> reactionForPostByUser =
                    reactionRepository.findTopByPostAndUserOrderByIdDesc(post, user);
            return reactionForPostByUser.filter(reaction -> reaction.getType().equals(reactionType))
                    .isPresent();
        }
        return false;
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }
}