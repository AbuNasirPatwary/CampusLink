package com.campuslink.complaint.dto;
import com.campuslink.complaint.ComplaintStatus;
import jakarta.validation.constraints.NotNull;
public record AdminComplaintUpdate(@NotNull ComplaintStatus status,String officialResponse){}
