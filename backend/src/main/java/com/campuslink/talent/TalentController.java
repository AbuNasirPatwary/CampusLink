package com.campuslink.talent;
import com.campuslink.common.dto.MessageResponse;
import com.campuslink.talent.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/talent") @PreAuthorize("hasAnyRole('STUDENT','ALUMNI','ADMIN')")
public class TalentController{
 private final TalentService service; public TalentController(TalentService s){service=s;}
 @GetMapping public List<TalentPostResponse> all(){return service.all();}
 @GetMapping("/{id}") public TalentPostResponse get(@PathVariable UUID id){return service.get(id);}
 @GetMapping("/mine") @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')") public List<TalentPostResponse> mine(){return service.mine();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')") public TalentPostResponse create(@Valid @RequestBody TalentPostInput i){return service.create(i);}
 @PutMapping("/{id}") @PreAuthorize("hasAnyRole('STUDENT','ALUMNI')") public TalentPostResponse update(@PathVariable UUID id,@Valid @RequestBody TalentPostInput i){return service.update(id,i);}
 @DeleteMapping("/{id}") public MessageResponse remove(@PathVariable UUID id){service.remove(id);return new MessageResponse("Talent post removed.");}
 @GetMapping("/{id}/comments") public List<TalentCommentResponse> comments(@PathVariable UUID id){return service.comments(id);}
 @PostMapping("/{id}/comments") public TalentCommentResponse comment(@PathVariable UUID id,@Valid @RequestBody TalentCommentInput i){return service.comment(id,i);}
 @DeleteMapping("/comments/{id}") public MessageResponse removeComment(@PathVariable UUID id){service.removeComment(id);return new MessageResponse("Comment removed.");}
 @PostMapping("/{id}/like") public TalentPostResponse like(@PathVariable UUID id){return service.toggleLike(id);}
 @PostMapping("/{id}/bookmark") public Map<String,Boolean> bookmark(@PathVariable UUID id){return Map.of("bookmarked",service.toggleBookmark(id));}
 @GetMapping("/bookmarks/mine") public List<TalentPostResponse> bookmarks(){return service.bookmarks();}
 @PatchMapping("/admin/{id}/featured") @PreAuthorize("hasRole('ADMIN')") public TalentPostResponse feature(@PathVariable UUID id,@RequestParam boolean value){return service.feature(id,value);}
}
