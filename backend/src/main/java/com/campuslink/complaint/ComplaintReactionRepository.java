package com.campuslink.complaint;
import com.campuslink.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ComplaintReactionRepository extends JpaRepository<ComplaintReaction,UUID>{
 Optional<ComplaintReaction> findByPostAndUser(ComplaintPost post,User user);
 long countByPost(ComplaintPost post);
}
