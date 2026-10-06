package com.campuslink.borrowhub;
import com.campuslink.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface BorrowRequestRepository extends JpaRepository<BorrowRequest, UUID> {
    List<BorrowRequest> findAllByOrderByCreatedAtDesc();
    List<BorrowRequest> findByAuthorOrderByCreatedAtDesc(User author);
}
