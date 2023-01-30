package igor.osa.reddit.be.dto;

import lombok.Data;

@Data
public class ReportDTO {
	
	private Integer id;
	private String reason;
	private String byUser;
	private Integer postId;
	private Integer commentId;
	private String postTitle;
	private String commentText;
}