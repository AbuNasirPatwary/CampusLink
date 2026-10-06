package com.campuslink.report;
import com.campuslink.report.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/reports")
public class ContentReportController{
 private final ContentReportService service; public ContentReportController(ContentReportService s){service=s;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public ContentReportResponse create(@Valid @RequestBody ContentReportInput i){return service.create(i);}
 @GetMapping @PreAuthorize("hasRole('ADMIN')") public List<ContentReportResponse> all(){return service.all();}
 @PatchMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ContentReportResponse update(@PathVariable UUID id,@RequestParam ReportStatus status){return service.setStatus(id,status);}
}
