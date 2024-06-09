package com.fabhotel.Eras.util;



import com.fabhotel.Eras.ExceptionHandler.ValidationException;
import com.fabhotel.Eras.modals.request.PostEndorsementRequest;

public class PostEndorsementRequestValidator {
	public void validateEndorsementRequest(PostEndorsementRequest request) {
		if (request.getRevieweeUserId() == null || request.getRevieweeUserId().trim().isEmpty()) {
			throw new ValidationException("Enter valid RevieweeUserId");
		}
		if(request.getReviewerUserId()== null || request.getReviewerUserId().trim().isEmpty()) {
			throw new ValidationException("Enter valid ReviewerUserId");
		}
        if (request.getScore() == 0 || request.getScore()<0) {
            throw new ValidationException("Score must not be zero or less than zero");
        }
        if (request.getSkillName() == null || request.getSkillName().isEmpty()) {
            throw new ValidationException("Skill must not be blank");
        }
    }
}
