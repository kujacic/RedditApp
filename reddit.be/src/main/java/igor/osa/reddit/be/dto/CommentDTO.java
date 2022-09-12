package igor.osa.reddit.be.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDTO {

    private Integer id;
	private String text;
	private LocalDate timestamp;
	private boolean isDeleted;
	private Integer userId;
	private Integer postId;
    private List<Integer> reportIds;
}
