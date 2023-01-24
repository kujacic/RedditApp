package igor.osa.reddit.be.dto;

import igor.osa.reddit.be.model.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionDTO {

    private Integer id;
    private ReactionType type;
    private String user;
    private Integer postId;
    private Integer commentId;
    private LocalDate timeStamp;
}