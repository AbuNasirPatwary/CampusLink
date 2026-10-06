package com.campuslink.findit;

import com.campuslink.common.dto.MessageResponse;
import com.campuslink.findit.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/findit")
@PreAuthorize("hasAnyRole('STUDENT','ALUMNI','ADMIN')")
public class FindItController {
    private final FindItService service;
    public FindItController(FindItService service) { this.service = service; }

    @GetMapping public List<FindItPostResponse> all() { return service.all(); }
    @GetMapping("/{id}") public FindItPostResponse get(@PathVariable UUID id) { return service.get(id); }

    @GetMapping("/mine/posts")
    @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')")
    public List<FindItPostResponse> mine() { return service.mine(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')")
    public FindItPostResponse create(@Valid @RequestBody FindItPostRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')")
    public FindItPostResponse update(@PathVariable UUID id, @Valid @RequestBody FindItPostRequest request) { return service.update(id, request); }

    @DeleteMapping("/{id}")
    public MessageResponse remove(@PathVariable UUID id) { service.remove(id); return new MessageResponse("Post removed."); }

    @PostMapping("/{id}/claims")
    @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')")
    public ClaimResponse claim(@PathVariable UUID id, @Valid @RequestBody ClaimRequest request) { return service.claim(id, request); }

    @GetMapping("/mine/claims")
    @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')")
    public List<ClaimResponse> myClaims() { return service.myClaims(); }

    @GetMapping("/admin/claims")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminClaimResponse> allClaims() { return service.allClaims(); }

    @PatchMapping("/admin/claims/{claimId}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminClaimResponse review(@PathVariable UUID claimId, @RequestParam ClaimStatus status) { return service.reviewClaim(claimId, status); }

    @PatchMapping("/admin/posts/{postId}/delivered")
    @PreAuthorize("hasRole('ADMIN')")
    public FindItPostResponse delivered(@PathVariable UUID postId) { return service.markDelivered(postId); }
}
