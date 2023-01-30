package igor.osa.reddit.be.dto;

import lombok.Data;

@Data
public class RuleDTO {

	private Integer id;
	private String description;
	private String communityName;
}