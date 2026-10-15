package CampusSkillSwap.controller;

import CampusSkillSwap.model.ChatMessageResponse;
import CampusSkillSwap.model.ChatPartnerResponse;
import CampusSkillSwap.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatMessageController {

    private final ChatService chatService;

    public ChatMessageController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/partner/{currentUserId}/{otherUserId}")
    public ResponseEntity<?> getChatPartner(
            @PathVariable Long currentUserId,
            @PathVariable Long otherUserId) {

        try {
            ChatPartnerResponse partner =
                    chatService.getChatPartner(
                            currentUserId,
                            otherUserId
                    );

            return ResponseEntity.ok(partner);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/{currentUserId}/{otherUserId}")
    public ResponseEntity<?> getConversation(
            @PathVariable Long currentUserId,
            @PathVariable Long otherUserId) {

        try {
            List<ChatMessageResponse> messages =
                    chatService.getConversation(
                            currentUserId,
                            otherUserId
                    );

            return ResponseEntity.ok(messages);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestBody Map<String, Object> body) {

        try {
            Long senderId = readId(body.get("senderId"));
            Long receiverId = readId(body.get("receiverId"));
            Object contentValue = body.get("content");

            String content = contentValue instanceof String
                    ? (String) contentValue
                    : null;

            ChatMessageResponse response =
                    chatService.sendMessage(
                            senderId,
                            receiverId,
                            content
                    );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    private Long readId(Object value) {

        if (value instanceof Number number) {
            return number.longValue();
        }

        if (value instanceof String string) {
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException ignored) {
                // Return the standard validation error below.
            }
        }

        throw new IllegalArgumentException(
                "Valid sender and receiver IDs are required."
        );
    }
}