package CampusSkillSwap.service;

import CampusSkillSwap.model.SwapRequest;
import CampusSkillSwap.model.SwapRequestResponse;
import CampusSkillSwap.model.User;
import CampusSkillSwap.repository.SwapRequestRepository;
import CampusSkillSwap.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class SwapRequestService {

    private final SwapRequestRepository requestRepository;
    private final UserRepository userRepository;

    public SwapRequestService(
            SwapRequestRepository requestRepository,
            UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SwapRequestResponse sendRequest(
            Long senderId, Long receiverId) {

        if (senderId == null || receiverId == null) {
            throw new IllegalArgumentException(
                    "Sender and receiver IDs are required."
            );
        }

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException(
                    "You cannot send a request to yourself."
            );
        }

        User sender = getUser(senderId);
        User receiver = getUser(receiverId);

        boolean alreadyPending =
                requestRepository
                        .existsBySenderIdAndReceiverIdAndStatus(
                                senderId, receiverId, "PENDING"
                        );

        if (alreadyPending) {
            throw new IllegalArgumentException(
                    "You already have a pending request to this student."
            );
        }

        boolean alreadyAccepted =
                requestRepository
                        .existsBySenderIdAndReceiverIdAndStatus(
                                senderId, receiverId, "ACCEPTED"
                        )
                || requestRepository
                        .existsBySenderIdAndReceiverIdAndStatus(
                                receiverId, senderId, "ACCEPTED"
                        );

        if (alreadyAccepted) {
            throw new IllegalArgumentException(
                    "A request between these students has already been accepted."
            );
        }

        SwapRequest request =
                new SwapRequest(senderId, receiverId);

        SwapRequest savedRequest =
                requestRepository.save(request);

        return toResponse(savedRequest, sender, receiver);
    }

    @Transactional(readOnly = true)
    public List<SwapRequestResponse> getReceivedRequests(
            Long userId) {

        getUser(userId);

        return requestRepository
                .findByReceiverIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SwapRequestResponse> getSentRequests(
            Long userId) {

        getUser(userId);

        return requestRepository
                .findBySenderIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SwapRequestResponse updateRequestStatus(
            Long requestId, Long currentUserId, String status) {

        if (requestId == null || currentUserId == null) {
            throw new IllegalArgumentException(
                    "Request ID and user ID are required."
            );
        }

        String normalizedStatus = status == null
                ? ""
                : status.trim().toUpperCase(Locale.ROOT);

        if (!normalizedStatus.equals("ACCEPTED")
                && !normalizedStatus.equals("REJECTED")) {
            throw new IllegalArgumentException(
                    "Status must be ACCEPTED or REJECTED."
            );
        }

        SwapRequest request = requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Request not found."
                        )
                );

        if (!request.getReceiverId().equals(currentUserId)) {
            throw new IllegalArgumentException(
                    "Only the recipient can respond to this request."
            );
        }

        if (!"PENDING".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalArgumentException(
                    "This request has already been processed."
            );
        }

        request.setStatus(normalizedStatus);

        SwapRequest savedRequest =
                requestRepository.save(request);

        return toResponse(savedRequest);
    }

    private User getUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID is required."
            );
        }

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Student not found."
                        )
                );
    }

    private SwapRequestResponse toResponse(
            SwapRequest request) {

        User sender = getUser(request.getSenderId());
        User receiver = getUser(request.getReceiverId());

        return toResponse(request, sender, receiver);
    }

    private SwapRequestResponse toResponse(
            SwapRequest request,
            User sender,
            User receiver) {

        SwapRequestResponse response =
                new SwapRequestResponse();

        response.setId(request.getId());
        response.setSenderId(request.getSenderId());
        response.setReceiverId(request.getReceiverId());
        response.setStatus(request.getStatus());
        response.setCreatedAt(request.getCreatedAt());

        response.setSenderName(sender.getName());
        response.setSenderDepartment(sender.getDepartment());
        response.setSenderClassYear(sender.getClassYear());

        response.setReceiverName(receiver.getName());
        response.setReceiverDepartment(receiver.getDepartment());
        response.setReceiverClassYear(receiver.getClassYear());

        response.setSenderSkillToTeach(sender.getSkillToTeach());
        response.setSenderSkillToLearn(sender.getSkillToLearn());

        response.setReceiverSkillToTeach(receiver.getSkillToTeach());
        response.setReceiverSkillToLearn(receiver.getSkillToLearn());

        return response;
    }
}