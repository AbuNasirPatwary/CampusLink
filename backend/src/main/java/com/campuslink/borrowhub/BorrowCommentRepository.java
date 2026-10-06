package com.campuslink.borrowhub;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface BorrowCommentRepository extends JpaRepository<BorrowComment, UUID> {
    List<BorrowComment> findByRequestAndRemovedFalseOrderByCreatedAtAsc(BorrowRequest request);
}
