package com.campuslink.complaint;
import com.campuslink.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ComplaintPostRepository extends JpaRepository<ComplaintPost,UUID>{
 List<ComplaintPost> findAllByOrderByCreatedAtDesc();
 List<ComplaintPost> findByAuthorOrderByCreatedAtDesc(User author);
}
