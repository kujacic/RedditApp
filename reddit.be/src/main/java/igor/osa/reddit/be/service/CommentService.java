package igor.osa.reddit.be.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igor.osa.reddit.be.dto.CommentDTO;
import igor.osa.reddit.be.model.Comment;
import igor.osa.reddit.be.model.Report;
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
		commentRepository.save(comment);
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
	
	//CONVERSIONS
	
	public CommentDTO convertToDTO(Comment comment) {
		CommentDTO commentDTO = mapper.map(comment, CommentDTO.class);
		commentDTO.setUserId(comment.getUser().getId());
		commentDTO.setPostId(comment.getId());
		List<Integer> reportIds = new ArrayList<>();
		for(Report report : comment.getReports()) {
			reportIds.add(report.getId());
		}
		commentDTO.setReportIds(reportIds);
		return commentDTO;
	}
	
	public Comment convertToEntity(CommentDTO commentDTO) {
		Comment comment = mapper.map(commentDTO, Comment.class);
		comment.setUser(userRepository.findById(commentDTO.getUserId()).orElse(null));
		comment.setPost(postRepository.findById(commentDTO.getPostId()).orElse(null));
		List<Report> reports = new ArrayList<>();
		for (Integer reportId : commentDTO.getReportIds()) {
			reports.add(reportRepository.findById(reportId).orElse(null));
		}
		comment.setReports(reports);
		return comment;
	}
}