package com.campuslink.complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ComplaintCommentRepository extends JpaRepository<ComplaintComment,UUID>{
 List<ComplaintComment> findByPostAndRemovedFalseOrderByCreatedAtAsc(ComplaintPost post);
}
