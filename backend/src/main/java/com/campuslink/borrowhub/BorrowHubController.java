package com.campuslink.borrowhub;

import com.campuslink.borrowhub.dto.*;
import com.campuslink.common.dto.MessageResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/borrowhub")
@PreAuthorize("hasAnyRole('STUDENT','ALUMNI','ADMIN')")
public class BorrowHubController {
    private final BorrowHubService service;
    public BorrowHubController(BorrowHubService service){this.service=service;}
    @GetMapping public List<BorrowRequestResponse> all(){return service.all();}
    @GetMapping("/{id}") public BorrowRequestResponse get(@PathVariable UUID id){return service.get(id);}
    @GetMapping("/mine") @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')") public List<BorrowRequestResponse> mine(){return service.mine();}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')")
    public BorrowRequestResponse create(@Valid @RequestBody BorrowRequestInput input){return service.create(input);}
    @PutMapping("/{id}") @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')") public BorrowRequestResponse update(@PathVariable UUID id,@Valid @RequestBody BorrowRequestInput input){return service.update(id,input);}
    @PatchMapping("/{id}/complete") @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')") public BorrowRequestResponse complete(@PathVariable UUID id){return service.complete(id);}
    @DeleteMapping("/{id}") public MessageResponse remove(@PathVariable UUID id){service.remove(id);return new MessageResponse("Request removed.");}
    @GetMapping("/{id}/comments") public List<BorrowCommentResponse> comments(@PathVariable UUID id){return service.comments(id);}
    @PostMapping("/{id}/comments") @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')") public BorrowCommentResponse comment(@PathVariable UUID id,@Valid @RequestBody BorrowCommentInput input){return service.comment(id,input);}
    @DeleteMapping("/comments/{commentId}") public MessageResponse removeComment(@PathVariable UUID commentId){service.removeComment(commentId);return new MessageResponse("Comment removed.");}
}
