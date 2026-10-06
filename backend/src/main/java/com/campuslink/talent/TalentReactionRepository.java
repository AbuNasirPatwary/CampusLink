package com.campuslink.talent;
import com.campuslink.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TalentReactionRepository extends JpaRepository<TalentReaction,UUID>{
 Optional<TalentReaction> findByPostAndUser(TalentPost post,User user); long countByPost(TalentPost post);
}
