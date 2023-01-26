package igor.osa.reddit.be.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class PostDTO {

    private Integer id;
	private String title;
	private String text;
	private LocalDateTime creationDate;
	private String imagePath;
	private Integer userId;
	private String author;
	private String communityName;
	private Integer communityId;
	private Integer flairId;
	private Integer karma;
	private Integer hotFactor;
	private List<Integer> commentIds;
    private List<Integer> reportIds;
}