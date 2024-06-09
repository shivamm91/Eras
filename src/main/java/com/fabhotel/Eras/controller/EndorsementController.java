package com.fabhotel.Eras.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;

import com.fabhotel.Eras.modals.request.PostEndorsementRequest;
import com.fabhotel.Eras.modals.response.PostEndorsementResponse;
import com.fabhotel.Eras.service.EndorsementService;
import com.fabhotel.Eras.util.PostEndorsementRequestValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/endorsements")
@Validated
public class EndorsementController {
    
    private static final Logger logger = LoggerFactory.getLogger(EndorsementController.class);

    private final EndorsementService endorsementService;
    private final PostEndorsementRequestValidator validator;

    @Autowired
    public EndorsementController(EndorsementService endorsementService,PostEndorsementRequestValidator validator) {
        this.endorsementService = endorsementService;
        this.validator = validator;
    }

    @PostMapping
    @ApiOperation(value = "Post a new endorsement")
    public PostEndorsementResponse postEndorsement(@Valid @RequestBody  PostEndorsementRequest postEndorsementRequest) {
    	logger.info("User {} is posting a new endorsement ");
    
    	 validator.validateEndorsementRequest(postEndorsementRequest);
    	PostEndorsementResponse posEndorsementResponse = endorsementService.postEndorsement(postEndorsementRequest);
    	 logger.info("Endorsement posted successfully: ", posEndorsementResponse);
         return posEndorsementResponse;
    }

    @GetMapping("/{userId}")
    @ApiOperation(value = "Get endorsements for a user")
    public Map<String, List<String>> getEndorsements(@PathVariable("userId") @NotBlank String userId) {
    	logger.info("Fetching endorsements for user ", userId);
    	Map<String, List<String>> endorsements = endorsementService.getEndorsements(userId);
    	logger.info("Endorsements fetched successfully ", userId, endorsements);
        return endorsements;
    }
    
    
}