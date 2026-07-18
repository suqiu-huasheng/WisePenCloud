package com.oriole.wisepen.system.api.domain.dto.req;

import com.oriole.wisepen.system.api.constant.FeedbackValidationMessage;
import com.oriole.wisepen.system.api.enums.FeedbackType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Xiong Heng
 */

/**
 * 用户提交反馈请求。
 * <p>
 * 与前端约定使用多选布尔字段表达类型；兼容历史单字段 {@link #type}。
 */
@Data
public class FeedbackRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = FeedbackValidationMessage.CONTENT_EMPTY)
    private String content;

    @NotBlank(message = FeedbackValidationMessage.CONTACT_EMPTY)
    private String contact;

    /**
     * 反馈截图公开地址，可选。
     */
    private String imageUrl;

    /**
     * 浏览器信息，可选。
     */
    private String browser;

    private Boolean bugReport;
    private Boolean suggestion;
    private Boolean consultation;
    private Boolean complaint;
    private Boolean other;

    /**
     * 历史单类型字段；当布尔类型字段均未选中时作为回退。
     */
    private FeedbackType type;
}
