package igor.osa.reddit.be.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igor.osa.reddit.be.dto.CommentDTO;
import igor.osa.reddit.be.dto.ReactionDTO;
import igor.osa.reddit.be.model.Comment;
import igor.osa.reddit.be.model.Post;
import igor.osa.reddit.be.model.Reaction;
import igor.osa.reddit.be.model.Report;
import igor.osa.reddit.be.model.enums.ReactionType;
import igor.osa.reddit.be.repository.CommentRepository;
import igor.osa.reddit.be.repository.PostRepository;
import igor.osa.reddit.be.repository.ReportRepository;
import igor.osa.reddit.be.repository.UserRepository;

@Service
public class CommentService {
	
	private static final Logger LOGGER = LogManager.getLogger(CommentService.class);
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private ReactionService reactionService;
	
	@Autowired
	private ModelMapper mapper;
	
	public List<CommentDTO> getAll() {
		List<Comment> comments = commentRepository.findAll();
		return comments.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}
	
	public Comment get(Integer id) {
		Comment comment = commentRepository.findById(id).orElse(null);
		if(comment == null) {
			LOGGER.error("Comment with id: {} doesn't exist!", id);
		}
		return comment;
	}
	
	public Comment create(CommentDTO dto) {
		Comment comment = convertToEntity(dto);
		comment.setTimestamp(LocalDate.now());
		Comment saved = commentRepository.save(comment);
		List<Reaction> reactions = new ArrayList<>();
		Reaction autoUpvote = reactionService.create(new ReactionDTO(null, ReactionType.UPVOTE, saved.getUser().getUsername(), null, saved.getId(), LocalDate.now()));
		reactions.add(autoUpvote);
		comment.setReactions(reactions);
		commentRepository.save(saved);
		LOGGER.info("Successfully created comment: {}", comment);
		return comment;
	}
	
	public Comment update(CommentDTO commentDTO) {
			return create(commentDTO);
	}
	
	public void delete(Comment comment) {
		commentRepository.delete(comment);
		LOGGER.info("Successfully deleted comment with id: {}", comment.getId());
	}
	
	public List<CommentDTO> getAllByPost(Post post) {
		List<Comment> comments = commentRepository.findByPost(post);
		List<CommentDTO> dtos = new ArrayList<>();
		for (Comment comment : comments) {
			dtos.add(convertToDTO(comment));
		}
		return dtos;
	}
	
	public Comment getByParent(Integer id) {
		Comment comment = get(id);
		List<Comment> childComments = commentRepository.findByComment(comment);
		return childComments.get(0);
	}
	
	public List<CommentDTO> sortComments(List<CommentDTO> comments, String sortBy) {
		if (sortBy.equals("newest")) {
			comments.sort(Comparator.comparing(CommentDTO::getTimestamp).reversed());
		}
		else if (sortBy.equals("oldest")) {
			comments.sort(Comparator.comparing(CommentDTO::getTimestamp));
		}
		else if (sortBy.equals("topRated")) {
			comments.sort(Comparator.comparing(CommentDTO::getKarma).reversed());
		}
		else if (sortBy.equals("lowestRated")) {
			comments.sort(Comparator.comparing(CommentDTO::getKarma));
		}
		else if (sortBy.equals("hot")) {
			long now = System.currentTimeMillis();
			for (CommentDTO dto : comments) {
				Long timeDiff = now - dto.getTimestamp().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
				Long hot = dto.getKarma().longValue() / timeDiff;
				dto.setHotFactor(hot.intValue());
			}
			comments.sort(Comparator.comparing(CommentDTO::getHotFactor).reversed());
		}
		return comments;
	}
	
	//CONVERSIONS
	
	public CommentDTO convertToDTO(Comment comment) {
		CommentDTO commentDTO = mapper.map(comment, CommentDTO.class);
		commentDTO.setUserId(comment.getUser().getId());
		commentDTO.setAuthor(comment.getUser().getUsername());
		if (comment.getUser().getDisplayName() != null) {
			commentDTO.setAuthorDisplayName(comment.getUser().getDisplayName());
		}
		commentDTO.setPostId(comment.getId());
		List<Reaction> allReactions = comment.getReactions();
        if (allReactions != null) {
        	List<Reaction> upvotes = new ArrayList<>();
            List<Reaction> downvotes = new ArrayList<>();
        	for (Reaction reaction : allReactions) {
                if (reaction.getType().equals(ReactionType.UPVOTE)) {
                    upvotes.add(reaction);
                } else if (reaction.getType().equals(ReactionType.DOWNVOTE)) {
                    downvotes.add(reaction);
                }
            }
    		commentDTO.setKarma(upvotes.size() - downvotes.size());
        }
		List<Integer> reportIds = new ArrayList<>();
		for(Report report : comment.getReports()) {
			reportIds.add(report.getId());
		}
		commentDTO.setReportIds(reportIds);
		return commentDTO;
	}
	
	public Comment convertToEntity(CommentDTO commentDTO) {
		Comment comment = mapper.map(commentDTO, Comment.class);
		if (commentDTO.getUserId() != null) {
			comment.setUser(userRepository.findById(commentDTO.getUserId()).orElse(null));
		} else {
			comment.setUser(userRepository.findByUsername(commentDTO.getAuthor()));
		}
		if (commentDTO.getPostId() != null) {
			comment.setPost(postRepository.findById(commentDTO.getPostId()).orElse(null));
		}
		if (commentDTO.getParentId() != null) {
			comment.setComment(commentRepository.findById(commentDTO.getParentId()).orElse(null));
		}
		List<Report> reports = new ArrayList<>();
		if(commentDTO.getReportIds() != null) {
			for (Integer reportId : commentDTO.getReportIds()) {
				reports.add(reportRepository.findById(reportId).orElse(null));
			}
		}
		comment.setReports(reports);
		return comment;
	}
	
	public List<CommentDTO> convertListToDTO(List<Comment> comments) {
		List<CommentDTO> dtos = new ArrayList<CommentDTO>();
		for (Comment comment : comments) {
			dtos.add(convertToDTO(comment));
		}
		return dtos;
	}
}