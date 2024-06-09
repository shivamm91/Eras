package com.fabhotel.Eras.service;

import java.util.List;
import java.util.Map;

import com.fabhotel.Eras.modals.request.PostEndorsementRequest;
import com.fabhotel.Eras.modals.response.PostEndorsementResponse;

public interface EndorsementService {

    public PostEndorsementResponse postEndorsement(PostEndorsementRequest postEndorsementRequest);
   
    public Map<String, List<String>> getEndorsements(String userId);
    
}
