package CampusSkillSwap.repository;

import CampusSkillSwap.model.SwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SwapRequestRepository
        extends JpaRepository<SwapRequest, Long> {

    List<SwapRequest> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    List<SwapRequest> findBySenderIdOrderByCreatedAtDesc(Long senderId);

    boolean existsBySenderIdAndReceiverIdAndStatus(
            Long senderId,
            Long receiverId,
            String status
    );
}