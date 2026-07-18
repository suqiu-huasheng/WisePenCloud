package com.oriole.wisepen.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oriole.wisepen.common.core.domain.PageR;
import com.oriole.wisepen.common.core.exception.ServiceException;
import com.oriole.wisepen.system.api.domain.dto.req.FeedbackRequest;
import com.oriole.wisepen.system.api.domain.dto.req.FeedbackStatusUpdateRequest;
import com.oriole.wisepen.system.api.domain.dto.res.FeedbackInfoResponse;
import com.oriole.wisepen.system.api.enums.FeedbackStatus;
import com.oriole.wisepen.system.api.enums.FeedbackType;
import com.oriole.wisepen.system.domain.entity.FeedbackEntity;
import com.oriole.wisepen.system.excpetion.SysError;
import com.oriole.wisepen.system.mapper.FeedbackMapper;
import com.oriole.wisepen.system.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Xiong Heng
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackMapper feedbackMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFeedback(Long userId, FeedbackRequest feedbackRequest) {
        List<FeedbackType> selectedTypes = resolveSelectedTypes(feedbackRequest);
        if (selectedTypes.isEmpty()) {
            throw new ServiceException(SysError.FEEDBACK_TYPE_REQUIRED);
        }

        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setUserId(userId);
        feedbackEntity.setContent(feedbackRequest.getContent().trim());
        feedbackEntity.setContact(feedbackRequest.getContact().trim());
        feedbackEntity.setBrowser(StrUtil.trimToNull(feedbackRequest.getBrowser()));
        feedbackEntity.setImageUrl(StrUtil.trimToNull(feedbackRequest.getImageUrl()));
        feedbackEntity.setType(selectedTypes.getFirst());
        feedbackEntity.setTypeValues(selectedTypes.stream().map(FeedbackType::getValue).collect(Collectors.joining(",")));
        feedbackEntity.setStatus(FeedbackStatus.PENDING);
        feedbackMapper.insert(feedbackEntity);
    }

    @Override
    public PageR<FeedbackInfoResponse> listFeedbacksAdmin(int page, int size, FeedbackStatus status, FeedbackType type, Long userId) {
        LambdaQueryWrapper<FeedbackEntity> queryWrapper = Wrappers.<FeedbackEntity>lambdaQuery()
                .eq(status != null, FeedbackEntity::getStatus, status)
                .eq(userId != null, FeedbackEntity::getUserId, userId)
                .and(type != null, wrapper -> wrapper
                        .eq(FeedbackEntity::getType, type)
                        .or()
                        .apply("FIND_IN_SET({0}, types) > 0", type.getValue()))
                .orderByDesc(FeedbackEntity::getCreateTime);

        IPage<FeedbackEntity> result = feedbackMapper.selectPage(new Page<>(page, size), queryWrapper);
        PageR<FeedbackInfoResponse> pageR = new PageR<>(result.getTotal(), page, size);
        List<FeedbackInfoResponse> list = result.getRecords().stream()
                .map(this::toFeedbackInfoResponse)
                .toList();
        pageR.addAll(list);
        return pageR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFeedbackStatus(FeedbackStatusUpdateRequest request) {
        FeedbackEntity feedbackEntity = feedbackMapper.selectById(request.getFeedbackId());
        if (feedbackEntity == null) {
            throw new ServiceException(SysError.FEEDBACK_NOT_FOUND);
        }
        feedbackEntity.setStatus(request.getStatus());
        feedbackMapper.updateById(feedbackEntity);
    }

    private List<FeedbackType> resolveSelectedTypes(FeedbackRequest feedbackRequest) {
        List<FeedbackType> selectedTypes = new ArrayList<>();
        if (Boolean.TRUE.equals(feedbackRequest.getBugReport())) {
            selectedTypes.add(FeedbackType.BUG_REPORT);
        }
        if (Boolean.TRUE.equals(feedbackRequest.getSuggestion())) {
            selectedTypes.add(FeedbackType.SUGGESTION);
        }
        if (Boolean.TRUE.equals(feedbackRequest.getConsultation())) {
            selectedTypes.add(FeedbackType.CONSULTATION);
        }
        if (Boolean.TRUE.equals(feedbackRequest.getComplaint())) {
            selectedTypes.add(FeedbackType.COMPLAINT);
        }
        if (Boolean.TRUE.equals(feedbackRequest.getOther())) {
            selectedTypes.add(FeedbackType.OTHER);
        }
        if (selectedTypes.isEmpty() && feedbackRequest.getType() != null) {
            selectedTypes.add(feedbackRequest.getType());
        }
        return selectedTypes;
    }

    private FeedbackInfoResponse toFeedbackInfoResponse(FeedbackEntity entity) {
        FeedbackInfoResponse response = BeanUtil.copyProperties(entity, FeedbackInfoResponse.class);
        response.setTypes(parseTypes(entity));
        return response;
    }

    private List<FeedbackType> parseTypes(FeedbackEntity entity) {
        if (StrUtil.isNotBlank(entity.getTypeValues())) {
            List<FeedbackType> parsedTypes = new ArrayList<>();
            for (String value : entity.getTypeValues().split(",")) {
                String trimmed = value.trim();
                if (StrUtil.isBlank(trimmed)) {
                    continue;
                }
                for (FeedbackType feedbackType : FeedbackType.values()) {
                    if (feedbackType.getValue().equals(trimmed)) {
                        parsedTypes.add(feedbackType);
                        break;
                    }
                }
            }
            return parsedTypes;
        }
        if (entity.getType() != null) {
            return List.of(entity.getType());
        }
        return List.of();
    }
}
