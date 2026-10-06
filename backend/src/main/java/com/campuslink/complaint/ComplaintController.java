package com.campuslink.complaint;

import com.campuslink.common.dto.MessageResponse;
import com.campuslink.complaint.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/complaints")
@PreAuthorize("hasAnyRole('STUDENT','ALUMNI','ADMIN')")
public class ComplaintController {
 private final ComplaintService service; public ComplaintController(ComplaintService s){service=s;}
 @GetMapping public List<ComplaintResponse> all(){return service.all();}
 @GetMapping("/{id}") public ComplaintResponse get(@PathVariable UUID id){return service.get(id);}
 @GetMapping("/mine") @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')") public List<ComplaintResponse> mine(){return service.mine();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')") public ComplaintResponse create(@Valid @RequestBody ComplaintInput i){return service.create(i);}
 @PutMapping("/{id}") @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')") public ComplaintResponse update(@PathVariable UUID id,@Valid @RequestBody ComplaintInput i){return service.update(id,i);}
 @DeleteMapping("/{id}") public MessageResponse remove(@PathVariable UUID id){service.remove(id);return new MessageResponse("Complaint removed.");}
 @GetMapping("/{id}/comments") public List<ComplaintCommentResponse> comments(@PathVariable UUID id){return service.comments(id);}
 @PostMapping("/{id}/comments") public ComplaintCommentResponse comment(@PathVariable UUID id,@Valid @RequestBody ComplaintCommentInput i){return service.comment(id,i);}
 @DeleteMapping("/comments/{id}") public MessageResponse removeComment(@PathVariable UUID id){service.removeComment(id);return new MessageResponse("Comment removed.");}
 @PostMapping("/{id}/react") public ComplaintResponse react(@PathVariable UUID id){return service.toggleReaction(id);}
 @PatchMapping("/admin/{id}") @PreAuthorize("hasRole('ADMIN')") public ComplaintResponse adminUpdate(@PathVariable UUID id,@Valid @RequestBody AdminComplaintUpdate i){return service.adminUpdate(id,i);}
}
