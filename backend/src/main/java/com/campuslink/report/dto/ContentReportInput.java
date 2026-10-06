package com.campuslink.report.dto;
import com.campuslink.report.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
public record ContentReportInput(@NotNull ReportModule module,@NotNull ReportTargetType targetType,@NotNull UUID targetId,@NotBlank String reason){}
