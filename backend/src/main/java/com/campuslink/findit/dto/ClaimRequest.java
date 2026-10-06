package com.campuslink.findit.dto;
import jakarta.validation.constraints.NotBlank;
public record ClaimRequest(@NotBlank String ownershipDescription) { }
