package com.campuslink.talent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TalentCommentRepository extends JpaRepository<TalentComment,UUID>{
 List<TalentComment> findByPostAndRemovedFalseOrderByCreatedAtAsc(TalentPost post);
}
