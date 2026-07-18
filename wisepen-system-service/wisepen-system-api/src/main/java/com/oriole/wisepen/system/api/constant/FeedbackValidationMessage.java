package com.oriole.wisepen.system.api.constant;

/**
 * 用户反馈校验消息常量
 */
public interface FeedbackValidationMessage {
    String CONTENT_EMPTY = "反馈内容不能为空";
    String CONTACT_EMPTY = "联系方式不能为空";
    String FEEDBACK_ID_NOT_NULL = "反馈 ID 不能为空";
    String STATUS_NOT_NULL = "反馈状态不能为空";
}
