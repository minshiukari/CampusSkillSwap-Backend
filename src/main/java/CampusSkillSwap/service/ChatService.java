package CampusSkillSwap.service;

import CampusSkillSwap.model.ChatMessage;
import CampusSkillSwap.model.ChatMessageResponse;
import CampusSkillSwap.model.ChatPartnerResponse;
import CampusSkillSwap.model.SwapRequest;
import CampusSkillSwap.model.User;
import CampusSkillSwap.repository.ChatMessageRepository;
import CampusSkillSwap.repository.SwapRequestRepository;
import CampusSkillSwap.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository chatRepository;
    private final UserRepository userRepository;
    private final SwapRequestRepository requestRepository;

    public ChatService(
            ChatMessageRepository chatRepository,
            UserRepository userRepository,
            SwapRequestRepository requestRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }

    @Transactional(readOnly = true)
    public ChatPartnerResponse getChatPartner(
            Long currentUserId,
            Long otherUserId) {

        validateUsers(currentUserId, otherUserId);
        requireAcceptedRequest(currentUserId, otherUserId);

        User otherUser = getUser(otherUserId);

        return new ChatPartnerResponse(
                otherUser.getId(),
                otherUser.getName(),
                otherUser.getDepartment(),
                otherUser.getClassYear()
        );
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getConversation(
            Long currentUserId,
            Long otherUserId) {

        validateUsers(currentUserId, otherUserId);
        requireAcceptedRequest(currentUserId, otherUserId);

        return chatRepository
                .findConversation(currentUserId, otherUserId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ChatMessageResponse sendMessage(
            Long senderId,
            Long receiverId,
            String content) {

        validateUsers(senderId, receiverId);
        requireAcceptedRequest(senderId, receiverId);

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty."
            );
        }

        String cleanedContent = content.trim();

        if (cleanedContent.length() > 5000) {
            throw new IllegalArgumentException(
                    "Message cannot exceed 5000 characters."
            );
        }

        ChatMessage message = new ChatMessage(
                senderId,
                receiverId,
                cleanedContent
        );

        ChatMessage savedMessage = chatRepository.save(message);

        return toResponse(savedMessage);
    }

    private void validateUsers(Long firstUserId, Long secondUserId) {

        if (firstUserId == null || secondUserId == null) {
            throw new IllegalArgumentException(
                    "Both user IDs are required."
            );
        }

        if (firstUserId.equals(secondUserId)) {
            throw new IllegalArgumentException(
                    "You cannot chat with yourself."
            );
        }

        getUser(firstUserId);
        getUser(secondUserId);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Student not found."
                        )
                );
    }

    private void requireAcceptedRequest(
            Long firstUserId,
            Long secondUserId) {

        List<SwapRequest> outgoingRequests =
                requestRepository
                        .findBySenderIdOrderByCreatedAtDesc(firstUserId);

        boolean accepted = outgoingRequests.stream()
                .anyMatch(request ->
                        request.getReceiverId().equals(secondUserId)
                                && "ACCEPTED".equalsIgnoreCase(
                                        request.getStatus()
                                )
                );

        if (!accepted) {
            List<SwapRequest> reverseRequests =
                    requestRepository
                            .findBySenderIdOrderByCreatedAtDesc(
                                    secondUserId
                            );

            accepted = reverseRequests.stream()
                    .anyMatch(request ->
                            request.getReceiverId().equals(firstUserId)
                                    && "ACCEPTED".equalsIgnoreCase(
                                            request.getStatus()
                                    )
                    );
        }

        if (!accepted) {
            throw new IllegalArgumentException(
                    "Chat is available only after a request has been accepted."
            );
        }
    }

    private ChatMessageResponse toResponse(ChatMessage message) {

        User sender = getUser(message.getSenderId());

        return new ChatMessageResponse(
                message.getId(),
                message.getSenderId(),
                message.getReceiverId(),
                message.getContent(),
                message.getTimestamp(),
                sender.getName()
        );
    }
}