package igor.osa.reddit.be.service;

import igor.osa.reddit.be.dto.ReactionCountDTO;
import igor.osa.reddit.be.dto.ReactionDTO;
import igor.osa.reddit.be.model.Comment;
import igor.osa.reddit.be.model.Post;
import igor.osa.reddit.be.model.Reaction;
import igor.osa.reddit.be.model.User;
import igor.osa.reddit.be.model.enums.ReactionType;
import igor.osa.reddit.be.repository.CommentRepository;
import igor.osa.reddit.be.repository.PostRepository;
import igor.osa.reddit.be.repository.ReactionRepository;
import igor.osa.reddit.be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    public ReactionCountDTO calculateReactions(Post post, String username) {
        List<Reaction> allReactions = post.getReactions();
        List<Reaction> upvotes = new ArrayList<>();
        List<Reaction> downvotes = new ArrayList<>();

        ReactionCountDTO reactionCountDTO = new ReactionCountDTO();
        for (Reaction reaction : allReactions) {
            if (reaction.getUser().getUsername().equals(username)) {
                reactionCountDTO.setType(String.valueOf(reaction.getType()));
            }
            if (reaction.getType().equals(ReactionType.UPVOTE)) {
                upvotes.add(reaction);
            } else if (reaction.getType().equals(ReactionType.DOWNVOTE)) {
                downvotes.add(reaction);
            }
        }
        reactionCountDTO.setCount(upvotes.size() - downvotes.size());
        return reactionCountDTO;
    }

    public Reaction create(ReactionDTO dto) {
        Reaction reaction = convertToEntity(dto);
        if (reaction.getPost() != null) {
            Reaction existing = reactionRepository.findByUserAndPost(reaction.getUser(), reaction.getPost());
            if (existing != null) {
                existing.setType(reaction.getType());
                existing.setTimeStamp(LocalDate.now());
                reactionRepository.save(existing);
            }
            else {
                reactionRepository.save(reaction);
            }
        } else if (reaction.getComment() != null) {
            // TODO
        }
        return reaction;
    }

    public Reaction convertToEntity(ReactionDTO reactionDTO) {
        Reaction reaction = new Reaction();
        reaction.setType(reactionDTO.getType());
        reaction.setTimeStamp(LocalDate.now());
        if(reactionDTO.getUser() != null) {
            User user = userRepository.findByUsername(reactionDTO.getUser());
            reaction.setUser(user);
        }
        if(reactionDTO.getPostId() != null) {
            Post post = postRepository.findById(reactionDTO.getPostId()).orElse(null);
            reaction.setPost(post);
        }
        if(reactionDTO.getCommentId() != null) {
            Comment comment = commentRepository.findById(reactionDTO.getCommentId()).orElse(null);
            reaction.setComment(comment);
        }
        return reaction;
    }


    public ReactionDTO convertToDTO(Reaction reaction) {
        ReactionDTO dto = new ReactionDTO();
        dto.setType(reaction.getType());
        dto.setTimeStamp(LocalDate.now());
        if(reaction.getUser() != null) {
            dto.setUser(reaction.getUser().getUsername());
        }
        if(reaction.getPost() != null) {
            dto.setPostId(reaction.getPost().getId());
        }
        if(reaction.getComment() != null) {
            dto.setCommentId(reaction.getComment().getId());
        }
        return dto;
    }
}