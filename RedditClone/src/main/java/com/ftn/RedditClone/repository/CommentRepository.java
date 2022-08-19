package com.ftn.RedditClone.repository;


import com.ftn.RedditClone.model.entity.Comment;
import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findAllByParentId(Long id);
    List<Comment> findAllByUser(User user);
    void deleteById(Long id);
}
