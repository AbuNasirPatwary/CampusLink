package com.campuslink.talent;
import com.campuslink.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TalentBookmarkRepository extends JpaRepository<TalentBookmark,UUID>{
 Optional<TalentBookmark> findByPostAndUser(TalentPost post,User user); List<TalentBookmark> findByUserOrderByCreatedAtDesc(User user);
}
