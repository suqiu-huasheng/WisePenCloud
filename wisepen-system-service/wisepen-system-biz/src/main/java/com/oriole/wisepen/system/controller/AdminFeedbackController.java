package com.oriole.wisepen.system.controller;

import com.oriole.wisepen.common.core.domain.PageR;
import com.oriole.wisepen.common.core.domain.R;
import com.oriole.wisepen.common.core.domain.enums.IdentityType;
import com.oriole.wisepen.common.security.annotation.CheckRole;
import com.oriole.wisepen.system.api.domain.dto.req.FeedbackStatusUpdateRequest;
import com.oriole.wisepen.system.api.domain.dto.res.FeedbackInfoResponse;
import com.oriole.wisepen.system.api.enums.FeedbackStatus;
import com.oriole.wisepen.system.api.enums.FeedbackType;
import com.oriole.wisepen.system.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员 - 反馈", description = "管理员分页查询与处理用户反馈工单")
@RestController
@RequestMapping("/admin/feedback")
@RequiredArgsConstructor
@CheckRole(IdentityType.ADMIN)
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    @Operation(
            summary = "分页查询用户反馈",
            description = """
                    - 用途：管理员按状态、类型和提交用户筛选反馈工单列表，用于后台展示与处理。
                    - 请求：status、type、userId 为可选过滤条件；page 和 size 控制分页，默认 page=1、size=20。
                    - 约束：当前操作者必须具备管理员身份；type 过滤会同时匹配主类型字段与多选 types 字段。
                    - 处理：按创建时间倒序分页查询 feedback 表并返回反馈详情列表；不修改反馈状态，不补全用户昵称等展示信息。
                    - 失败：当前操作者不是管理员 -> PermissionError.UNAUTHORIZED。
                    - 响应：返回分页反馈列表和总数，列表项包含 id、userId、content、contact、imageUrl、type、types、status 与时间字段。
                    """
    )
    @GetMapping("/listFeedbacks")
    public R<PageR<FeedbackInfoResponse>> listFeedbacks(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(required = false) FeedbackStatus status,
            @RequestParam(required = false) FeedbackType type,
            @RequestParam(required = false) Long userId
    ) {
        return R.ok(feedbackService.listFeedbacksAdmin(page, size, status, type, userId));
    }

    @Operation(
            summary = "更新反馈处理状态",
            description = """
                    - 用途：管理员将用户反馈工单流转到处理中、已解决、已忽略或已关闭等状态。
                    - 请求：feedbackId 指定目标反馈；status 为目标处理状态。
                    - 约束：当前操作者必须具备管理员身份；目标反馈记录必须存在；status 必须是合法 FeedbackStatus。
                    - 处理：更新反馈状态与更新时间；不修改反馈正文、联系方式、截图或类型，不发送站内信或邮件通知。
                    - 失败：当前操作者不是管理员 -> PermissionError.UNAUTHORIZED；反馈记录不存在 -> SysError.FEEDBACK_NOT_FOUND。
                    - 响应：成功时返回空结果。
                    """
    )
    @PostMapping("/updateFeedbackStatus")
    public R<Void> updateFeedbackStatus(@Valid @RequestBody FeedbackStatusUpdateRequest request) {
        feedbackService.updateFeedbackStatus(request);
        return R.ok();
    }
}
