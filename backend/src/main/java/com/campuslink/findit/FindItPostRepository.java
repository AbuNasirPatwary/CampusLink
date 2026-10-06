package com.campuslink.findit;
import com.campuslink.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface FindItPostRepository extends JpaRepository<FindItPost, UUID> {
    List<FindItPost> findAllByOrderByCreatedAtDesc();
    List<FindItPost> findByOwnerOrderByCreatedAtDesc(User owner);
}
