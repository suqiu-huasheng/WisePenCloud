package com.oriole.wisepen.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.oriole.wisepen.system.api.enums.FeedbackStatus;
import com.oriole.wisepen.system.api.enums.FeedbackType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Xiong Heng
 */
@Data
@TableName("feedback")
public class FeedbackEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String content;
    private String contact;
    private String browser;
    @TableField("image_url")
    private String imageUrl;
    /**
     * 主类型（多选时取首个类型）。
     */
    private FeedbackType type;
    /**
     * 全部类型，逗号分隔，例如 BUG_REPORT,SUGGESTION。
     */
    @TableField("types")
    private String typeValues;
    private FeedbackStatus status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
