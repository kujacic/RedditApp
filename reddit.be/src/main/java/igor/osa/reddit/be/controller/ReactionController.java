package igor.osa.reddit.be.controller;

import igor.osa.reddit.be.dto.ReactionCountDTO;
import igor.osa.reddit.be.dto.ReactionDTO;
import igor.osa.reddit.be.model.Comment;
import igor.osa.reddit.be.model.Post;
import igor.osa.reddit.be.model.Reaction;
import igor.osa.reddit.be.model.User;
import igor.osa.reddit.be.service.CommentService;
import igor.osa.reddit.be.service.PostService;
import igor.osa.reddit.be.service.ReactionService;
import igor.osa.reddit.be.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reaction")
public class ReactionController {

    @Autowired
    private PostService postService;
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private ReactionService reactionService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/post/{id}")
    public ResponseEntity<ReactionCountDTO> getForPost(@PathVariable("id") Integer id, @RequestParam(value="user", required=false) String user) {
        Post post = postService.get(id);
        ReactionCountDTO reactionCountDTO = reactionService.calculateReactions(post, user);
        return new ResponseEntity<ReactionCountDTO>(reactionCountDTO, HttpStatus.OK);
    }
    
    @GetMapping("/comment/{id}")
    public ResponseEntity<ReactionCountDTO> getForComment(@PathVariable("id") Integer id, @RequestParam(value="user", required=false) String user) {
        Comment comment = commentService.get(id);
        ReactionCountDTO reactionCountDTO = reactionService.calculateReactions(comment, user);
        return new ResponseEntity<ReactionCountDTO>(reactionCountDTO, HttpStatus.OK);
    }
    
    @GetMapping("/user")
    public ResponseEntity<Integer> getUserKarma(@RequestParam("user") String username) {
        User user = userService.getByUsername(username);
        Integer karma = reactionService.calculateUserKarma(user);
        return new ResponseEntity<Integer>(karma, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ReactionDTO reactionDTO) {
        Reaction reaction = reactionService.create(reactionDTO);
        if (reaction != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}