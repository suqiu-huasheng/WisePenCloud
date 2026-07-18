package com.oriole.wisepen.system.api.domain.dto.req;

import com.oriole.wisepen.system.api.constant.FeedbackValidationMessage;
import com.oriole.wisepen.system.api.enums.FeedbackStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理员更新反馈处理状态请求。
 */
@Data
public class FeedbackStatusUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = FeedbackValidationMessage.FEEDBACK_ID_NOT_NULL)
    private Long feedbackId;

    @NotNull(message = FeedbackValidationMessage.STATUS_NOT_NULL)
    private FeedbackStatus status;
}
