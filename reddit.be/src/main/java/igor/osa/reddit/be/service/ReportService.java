package igor.osa.reddit.be.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igor.osa.reddit.be.dto.ReportDTO;
import igor.osa.reddit.be.model.Comment;
import igor.osa.reddit.be.model.Community;
import igor.osa.reddit.be.model.Moderator;
import igor.osa.reddit.be.model.Post;
import igor.osa.reddit.be.model.Report;
import igor.osa.reddit.be.model.enums.ReportReason;
import igor.osa.reddit.be.repository.ModeratorRepository;
import igor.osa.reddit.be.repository.ReportRepository;

@Service
public class ReportService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private ModeratorRepository moderatorRepository;

	
	public Report create(ReportDTO reportDTO) {
		Report report = convertToEntity(reportDTO);
		report.setTimestamp(LocalDate.now());
		reportRepository.save(report);
		return report;
	}
	
	public List<Report> getAllByModerator(String username) {
		Moderator moderator = moderatorRepository.findByUsername(username);
		List<Report> reports = new ArrayList<>();
		
		for (Community community : moderator.getCommunities()) {
			List<Post> posts = community.getPosts();
			for (Post post : posts) {
				reports.addAll(post.getReports());
				for (Comment comment : post.getComments()) {
					reports.addAll(comment.getReports());
				}
			}
		}
		return reports;
	}
	
	//CONVERSIONS
	
	public ReportDTO convertToDTO(Report report) {
		ReportDTO reportDTO = new ReportDTO();
		reportDTO.setId(report.getId());
		reportDTO.setReason(String.valueOf(report.getReason()));
		reportDTO.setByUser(report.getByUser().getUsername());
		if (report.getPost() != null) {
			reportDTO.setPostId(report.getPost().getId());
			reportDTO.setPostTitle(report.getPost().getTitle());
		}
		if (report.getComment() != null) {
			reportDTO.setCommentId(report.getComment().getId());
			reportDTO.setCommentText(report.getComment().getText());
		}
		return reportDTO;
	}
	
	public Report convertToEntity(ReportDTO reportDTO) {
		Report report = new Report();
		report.setId(reportDTO.getId());
		report.setReason(ReportReason.valueOf(reportDTO.getReason()));
		if (reportDTO.getByUser() != null) {
			report.setByUser(userService.getByUsername(reportDTO.getByUser()));
		}
		if (reportDTO.getPostId() != null) {
			report.setPost(postService.get(reportDTO.getPostId()));
		}
		if (reportDTO.getCommentId() != null) {
			report.setComment(commentService.get(reportDTO.getCommentId()));
		}
		return report;
	}
	
	public List<ReportDTO> convertListToDTO(List<Report> reports) {
		List<ReportDTO> dtos = new ArrayList<>();
		for (Report report : reports) {
			dtos.add(convertToDTO(report));
		}
		return dtos;
	}
}
