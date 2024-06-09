package com.fabhotel.Eras.modals.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEndorsementResponse {

    private double adjustedScore;
    private String adjustmentReason;
}

