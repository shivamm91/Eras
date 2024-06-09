
package com.fabhotel.Eras.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fabhotel.Eras.ExceptionHandler.RevieweeNotFoundException;
import com.fabhotel.Eras.ExceptionHandler.ReviewerNotFoundException;
import com.fabhotel.Eras.ExceptionHandler.SkillNotFoundException;
import com.fabhotel.Eras.ExceptionHandler.ValidationException;
import com.fabhotel.Eras.entity.EndorsementEntity;
import com.fabhotel.Eras.entity.SkillEntity;
import com.fabhotel.Eras.entity.UserEntity;
import com.fabhotel.Eras.modals.request.PostEndorsementRequest;
import com.fabhotel.Eras.modals.response.PostEndorsementResponse;
import com.fabhotel.Eras.repository.EndorsementRepository;
import com.fabhotel.Eras.repository.SkillRepository;
import com.fabhotel.Eras.repository.UserRepository;
import com.fabhotel.Eras.service.EndorsementService;
import com.fabhotel.Eras.util.Constants;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EndorsementServiceImpl implements EndorsementService {

	private static final Logger logger = LoggerFactory.getLogger(EndorsementService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private EndorsementRepository endorsementRepository;

	@Transactional
	@Override
	public PostEndorsementResponse postEndorsement(PostEndorsementRequest postEndorsementRequest) {

		logger.debug("Fetching reviewee with ID: {}", postEndorsementRequest.getRevieweeUserId());
		UserEntity reviewee = userRepository.findById(postEndorsementRequest.getRevieweeUserId().trim())
				.orElseThrow(() -> new RevieweeNotFoundException(
						Constants.ERROR_REVIEWEE_NOT_FOUND + postEndorsementRequest.getRevieweeUserId().trim()));

		logger.debug("Fetching reviewer with ID: {}", postEndorsementRequest.getReviewerUserId().trim());
		UserEntity reviewer = userRepository.findById(postEndorsementRequest.getReviewerUserId().trim())
				.orElseThrow(() -> new ReviewerNotFoundException(
						Constants.ERROR_REVIEWER_NOT_FOUND + postEndorsementRequest.getReviewerUserId()));

		logger.debug("Fetching skill with name: {}", postEndorsementRequest.getSkillName().trim());
		SkillEntity skillEntity = skillRepository.findByName(postEndorsementRequest.getSkillName().trim());
		if (skillEntity == null) {
			throw new SkillNotFoundException(Constants.ENDORSE_SKILL_NOT_FOUND + postEndorsementRequest.getSkillName());
		}

		logger.debug("Checking if the reviewee has the skill or a parent skill");
		if (!hasSkillOrParentSkill(reviewee, postEndorsementRequest.getSkillName())) {

			throw new ValidationException(Constants.ERROR_SKILL_NOT_FOUND);
		}

		EndorsementEntity endorsement = new EndorsementEntity();
		endorsement.setRevieweeId(postEndorsementRequest.getRevieweeUserId().trim());
		endorsement.setReviewerId(postEndorsementRequest.getReviewerUserId().trim());
		endorsement.setSkill(skillEntity);
		endorsement.setUserScore((int) (postEndorsementRequest.getScore()));

		PostEndorsementResponse postEndorsementResponse = calculateAdjustedScore(endorsement, reviewer, reviewee,
				skillEntity);

		logger.debug("Saving endorsement");
		int existingEndorsement = endorsementRepository.countByReviewerIdAndRevieweeIdAndSkillId(
				postEndorsementRequest.getReviewerUserId().trim(), postEndorsementRequest.getRevieweeUserId().trim(), endorsement.getSkill().getId());
		if (existingEndorsement != 0) {
			throw new ValidationException(Constants.DUPLICATE_ENTRY + endorsement.getReviewerId() + ", RevieweeId: "
					+ endorsement.getRevieweeId() + ", SkillId: " + endorsement.getSkill().getId());

		} else {
			endorsementRepository.save(endorsement);
		}

		return postEndorsementResponse;
	}

	public boolean hasSkillOrParentSkill(UserEntity user, String skillName) {

		// Iterate through the user's skills
		for (SkillEntity skill : user.getSkills()) {

			// Check if the current skill matches the specified skill name
			if (skillMatches(skill, skillName)) {
				return true; // User has the specified skill or parent skill
			}
		}
		return false; // User does not have the specified skill or parent skill
	}

	private boolean skillMatches(SkillEntity skillEntity, String skillName) {

		// Check if the current skill matches the specified skill name
		if (skillEntity.getName().equalsIgnoreCase(skillName)) {
			return true;
		}

		// Recursively check if any of the parent skills match the specified skill name
		if (skillEntity.getParentSkill() != null) {
			return skillMatches(skillEntity.getParentSkill(), skillName);
		}

		return false; // None of the skills or parent skills match the specified skill name
	}

	private PostEndorsementResponse calculateAdjustedScore(EndorsementEntity endorsement, UserEntity reviewer,
			UserEntity reviewee, SkillEntity skill) {

		double adjustedScore = 0;
		StringBuilder adjustmentReason = new StringBuilder("");

		// check if reviewer and reviewee are/were coworkers - assign 1 additional point
		// for it
		if (areOrWereCoworkers(reviewer, reviewee)) {

			adjustedScore += 1;
			adjustmentReason.append(Constants.COWOERKER_POINT);
		}

		// check if reviewer workExp is greater than reviewee - assign 1 additional
		// point
		if (isReviewerMoreExperienced(reviewee, reviewer)) {

			adjustedScore += 1;
			adjustmentReason.append(Constants.EXP_POINT);
		}

		// assign additional points based on skill closeness for reviewer
		int scoreForSkillClosenessForReviewer = calculatePointsBasedOnSkillCloseness(reviewee, skill);
		if (scoreForSkillClosenessForReviewer > 0) {

			adjustmentReason.append(scoreForSkillClosenessForReviewer + Constants.SKILL_CLOSENESS + " reviewer, ");
		}

		adjustedScore += scoreForSkillClosenessForReviewer;

		// assign additional points based on skill closeness for reviewee
		int scoreForSkillClosenessForReviewee = calculatePointsBasedOnSkillCloseness(reviewee, skill);
		if (scoreForSkillClosenessForReviewee > 0) {

			adjustmentReason.append(scoreForSkillClosenessForReviewee + Constants.SKILL_CLOSENESS + " reviewee, ");
		}

		adjustedScore += scoreForSkillClosenessForReviewee;
		endorsement.setAdjustedScore((int) adjustedScore);
		endorsement.setAdjustmentReason(
				adjustedScore == 0 ? "No matching criterion satisfied" : adjustmentReason.toString());

		PostEndorsementResponse postEndorsementResponse = new PostEndorsementResponse();
		postEndorsementResponse.setAdjustedScore(adjustedScore);
		postEndorsementResponse.setAdjustmentReason(adjustmentReason.toString());

		return postEndorsementResponse;

	}

	private boolean areOrWereCoworkers(UserEntity reviewee, UserEntity reviewer) {

		return reviewee.getCompanies().stream().anyMatch(revieweeCompany -> {

			return reviewer.getCompanies().stream()
					.anyMatch(reviewerCompany -> reviewerCompany.getId() == revieweeCompany.getId());
		});
	}

	private boolean isReviewerMoreExperienced(UserEntity reviewee, UserEntity reviewer) {

		return reviewee.getYearsOfExperience() < reviewer.getYearsOfExperience();
	}

	private int calculatePointsBasedOnSkillCloseness(UserEntity reviewee, SkillEntity skill) {

		for (SkillEntity revieweeSkill : reviewee.getSkills()) {

			if (skillMatches(revieweeSkill, skill.getName())) {

				return calcuateSkillClosenessScore(revieweeSkill, skill.getName());
			}
		}

		return 0;
	}

	private int calcuateSkillClosenessScore(SkillEntity skillEntity, String skillName) {
		// Initialize the score to 0

		// Check if the current skill matches the specified skill name
		if (skillEntity.getName().equalsIgnoreCase(skillName)) {

			// assign 1 point for each level of parent skill the matched skill has
			return 1 + getParentSkillCount(skillEntity);

		}

		// Recursively check if any of the parent skills match the specified skill name
		if (skillEntity.getParentSkill() != null) {
			return calcuateSkillClosenessScore(skillEntity.getParentSkill(), skillName);
		}

		return 0;
	}

	private int getParentSkillCount(SkillEntity skillEntity) {

		if (skillEntity.getParentSkill() != null) {
			return 1 + getParentSkillCount(skillEntity.getParentSkill());
		}

		return 0;
	}

	@Override
	public Map<String, List<String>> getEndorsements(String userId) {

		List<EndorsementEntity> endorsements = endorsementRepository.findByRevieweeId(userId);

		Map<String, List<String>> response = new HashMap<>();
		endorsements.stream().forEach(endorsement -> {

			List<String> skillEndorsementTexts = response.getOrDefault(endorsement.getSkill().getName(),
					new ArrayList<>());
			skillEndorsementTexts.add(getFormattedEndorsementText(endorsement));

			response.put(endorsement.getSkill().getName(), skillEndorsementTexts);
		});

		return response;
	}

	
	private String getFormattedEndorsementText(EndorsementEntity endorsement) {
		Optional<UserEntity> userEntity=userRepository.findById(endorsement.getReviewerId());
		return String.format("%s - %d (%d with reason(s) [ %s ])", 
				userEntity.get().getName(),
				endorsement.getUserScore(), endorsement.getAdjustedScore(), endorsement.getAdjustmentReason());
	}

}
