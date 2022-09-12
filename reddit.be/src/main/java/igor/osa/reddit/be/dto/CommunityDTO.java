package igor.osa.reddit.be.dto;

import java.util.List;

import lombok.Data;

@Data
public class CommunityDTO {

    private Integer id;
	private String name;
	private String description;
	private String creationDate;
	private boolean isSuspended;
	private String suspendedReason;
	private List<Integer> bannedListIds;
	private List<Integer> rulesIds;
	private List<Integer> postsIds;
	private List<Integer> moderatorsIds;
	private List<Integer> flairsIds;
}
