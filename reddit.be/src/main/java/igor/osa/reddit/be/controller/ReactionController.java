package igor.osa.reddit.be.controller;

import igor.osa.reddit.be.dto.PostDTO;
import igor.osa.reddit.be.dto.ReactionCountDTO;
import igor.osa.reddit.be.dto.ReactionDTO;
import igor.osa.reddit.be.model.Post;
import igor.osa.reddit.be.model.Reaction;
import igor.osa.reddit.be.service.PostService;
import igor.osa.reddit.be.service.ReactionService;
import org.apache.coyote.Response;
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
    private ReactionService reactionService;

    @GetMapping("/{id}")
    public ResponseEntity<ReactionCountDTO> get(@PathVariable("id") Integer id, @RequestParam("user") String user) {
        Post post = postService.get(id);
        ReactionCountDTO reactionCountDTO = reactionService.calculateReactions(post, user);
        return new ResponseEntity<ReactionCountDTO>(reactionCountDTO, HttpStatus.OK);
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