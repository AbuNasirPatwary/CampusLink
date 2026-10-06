package com.campuslink.report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ContentReportRepository extends JpaRepository<ContentReport,UUID>{ List<ContentReport> findAllByOrderByCreatedAtDesc(); }
