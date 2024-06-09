package com.fabhotel.Eras.util;

public class Constants {

    // Error messages
    public static final String ERROR_REVIEWEE_NOT_FOUND = "Reviewee not found";
    public static final String ERROR_REVIEWER_NOT_FOUND = "Reviewer not found";
    public static final String ERROR_SKILL_NOT_FOUND = "Reviewee does not have the skill being endorsed!";
    public static final String DUPLICATE_ENTRY = "Duplicate entry detected: ReviewerId: ";
    
    // Points and reasons
    public static final String COWOERKER_POINT = "1 point as reviewer and reviewee are/were coworkers, ";
    public static final String EXP_POINT = "1 point as reviewer has more experience than reviewee, ";
    public static final String SKILL_CLOSENESS = "point(s) for skill closeness for ";
    
    // Other messages
    public static final String ENDORSE_SKILL_NOT_FOUND = "Skill not found with name: {} , you can only endorse a skill present in the system";
}
