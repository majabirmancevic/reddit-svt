package com.ftn.RedditClone.controller;

import com.ftn.RedditClone.exceptions.SpringRedditException;
import com.ftn.RedditClone.mapper.CommentMapper;
import com.ftn.RedditClone.model.entity.Comment;
import com.ftn.RedditClone.model.entity.dto.CommentDTO;
import com.ftn.RedditClone.repository.CommentRepository;
import com.ftn.RedditClone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {

    @Autowired
    CommentService commentService;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentRepository commentRepository;

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.save(commentDto));
    }

    @GetMapping("byPost/{postId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsFromPost(postId));
    }

    @GetMapping("byUser/{userName}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForUser(@PathVariable String userName){
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsFromUser(userName));
    }

    @GetMapping("{id}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable Long id) {
        return ResponseEntity.status(OK)
                .body(commentService.getComment(id));
    }

    @GetMapping("allByParent/{id}")
    public ResponseEntity<List<CommentDTO>> getAllFromParentComment(@PathVariable Long id){
        return ResponseEntity.status(OK)
                .body(commentService.getAllFromParentId(id));
    }


    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new SpringRedditException(id.toString()));

        if(comment != null){
            commentRepository.deleteCurrent(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "edit/{id}")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentDTO commentDTO, @PathVariable Long id){

        Comment comment = commentService.findComment(id);

        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(commentDTO.getText() != comment.getText() && commentDTO.getText() != "" ) {
            comment.setText(commentDTO.getText());
        }

        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentMapper.mapToDto(comment));
    }
}
