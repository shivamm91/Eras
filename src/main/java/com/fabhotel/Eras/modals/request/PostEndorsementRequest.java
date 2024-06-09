package com.fabhotel.Eras.modals.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEndorsementRequest {

	@NotBlank(message = "Reviewee user ID must not be blank")
	private String revieweeUserId;

	@NotBlank(message = "Reviewer user ID must not be blank")
	private String reviewerUserId;

	@NotBlank(message = "Skill must not be blank")
	private String skillName;

	@NotNull(message = "Score must not be null")
	@Positive(message = "Score must be greater than zero")
	private double score;

}
