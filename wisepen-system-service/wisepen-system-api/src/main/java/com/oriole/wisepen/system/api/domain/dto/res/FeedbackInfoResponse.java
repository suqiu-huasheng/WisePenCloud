package com.oriole.wisepen.system.api.domain.dto.res;

import com.oriole.wisepen.system.api.enums.FeedbackStatus;
import com.oriole.wisepen.system.api.enums.FeedbackType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 反馈详情 / 列表项响应。
 */
@Data
public class FeedbackInfoResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private String content;
    private String contact;
    private String browser;
    private String imageUrl;
    /**
     * 主类型（多选时取首个类型，便于兼容旧数据与简单筛选）。
     */
    private FeedbackType type;
    /**
     * 全部选中类型。
     */
    private List<FeedbackType> types = new ArrayList<>();
    private FeedbackStatus status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
