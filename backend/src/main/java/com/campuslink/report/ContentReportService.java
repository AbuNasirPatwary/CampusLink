package com.campuslink.report;

import com.campuslink.common.exception.ApiException;
import com.campuslink.report.dto.*;
import com.campuslink.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ContentReportService {
    private final ContentReportRepository repo;
    private final UserService users;

    public ContentReportService(ContentReportRepository r, UserService u) {
        repo = r;
        users = u;
    }

    @Transactional
    public ContentReportResponse create(ContentReportInput i) {
        ContentReport r = new ContentReport();
        r.setReporter(users.getCurrentUser());
        r.setModule(i.module());
        r.setTargetType(i.targetType());
        r.setTargetId(i.targetId());
        r.setReason(i.reason());
        return ContentReportResponse.from(repo.save(r));
    }

    public List<ContentReportResponse> all() {
        return repo.findAllByOrderByCreatedAtDesc().stream().map(ContentReportResponse::from).toList();
    }

    @Transactional
    public ContentReportResponse setStatus(UUID id, ReportStatus s) {
        ContentReport r = repo.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Report not found."));
        r.setStatus(s);
        return ContentReportResponse.from(repo.save(r));
    }
}
