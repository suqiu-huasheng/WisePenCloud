package com.oriole.wisepen.system.excpetion;

import com.oriole.wisepen.common.core.domain.IResult;
import com.oriole.wisepen.common.core.domain.ResultKey;
import com.oriole.wisepen.common.core.domain.enums.BusinessDomain;
import com.oriole.wisepen.common.core.exception.ErrorReason;
import com.oriole.wisepen.system.api.constant.SystemSubject;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统微服务(2)专属业务错误
 */
@Getter
@AllArgsConstructor
public enum SysError implements IResult {

    MAIL_SEND_FAILED(2111, new ResultKey(BusinessDomain.SYSTEM, SystemSubject.MAIL, ErrorReason.FAILED), "邮件发送失败"),

    FEEDBACK_TYPE_REQUIRED(2211, new ResultKey(BusinessDomain.SYSTEM, SystemSubject.FEEDBACK, ErrorReason.REQUIRED_MISSING), "反馈类型不能为空"),
    FEEDBACK_NOT_FOUND(2221, new ResultKey(BusinessDomain.SYSTEM, SystemSubject.FEEDBACK, ErrorReason.NOT_FOUND), "反馈记录不存在");

    private final Integer code;
    private final ResultKey key;
    private final String msg;
}