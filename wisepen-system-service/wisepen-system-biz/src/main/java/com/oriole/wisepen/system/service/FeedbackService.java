package com.oriole.wisepen.system.service;

import com.oriole.wisepen.common.core.domain.PageR;
import com.oriole.wisepen.system.api.domain.dto.req.FeedbackRequest;
import com.oriole.wisepen.system.api.domain.dto.req.FeedbackStatusUpdateRequest;
import com.oriole.wisepen.system.api.domain.dto.res.FeedbackInfoResponse;
import com.oriole.wisepen.system.api.enums.FeedbackStatus;
import com.oriole.wisepen.system.api.enums.FeedbackType;

public interface FeedbackService {
    void createFeedback(Long userId, FeedbackRequest feedbackRequest);

    PageR<FeedbackInfoResponse> listFeedbacksAdmin(int page, int size, FeedbackStatus status, FeedbackType type, Long userId);

    void updateFeedbackStatus(FeedbackStatusUpdateRequest request);
}
