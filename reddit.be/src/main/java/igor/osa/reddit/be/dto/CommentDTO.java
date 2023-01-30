package igor.osa.reddit.be.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Integer id;
	private String text;
	private LocalDate timestamp;
	private boolean isDeleted;
	private Integer userId;
	private String author;
	private String authorDisplayName;
	private Integer postId;
	private Integer parentId;
	private Integer karma;
	private Integer hotFactor;
    private List<Integer> reportIds;
}
