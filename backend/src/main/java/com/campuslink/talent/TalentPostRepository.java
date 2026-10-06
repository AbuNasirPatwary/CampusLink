package com.campuslink.talent;
import com.campuslink.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TalentPostRepository extends JpaRepository<TalentPost,UUID>{
 List<TalentPost> findAllByOrderByCreatedAtDesc();
 List<TalentPost> findByAuthorOrderByCreatedAtDesc(User author);
}
