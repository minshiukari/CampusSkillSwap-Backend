package CampusSkillSwap.controller;

import CampusSkillSwap.model.SwapRequestResponse;
import CampusSkillSwap.service.SwapRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/swaps")
public class SwapRequestController {

    private final SwapRequestService requestService;

    public SwapRequestController(
            SwapRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<?> sendRequest(
            @RequestBody Map<String, Long> body) {
        try {
            Long senderId = body.get("senderId");
            Long receiverId = body.get("receiverId");

            SwapRequestResponse response =
                    requestService.sendRequest(senderId, receiverId);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/received/{userId}")
    public ResponseEntity<?> getReceivedRequests(
            @PathVariable Long userId) {
        try {
            List<SwapRequestResponse> requests =
                    requestService.getReceivedRequests(userId);

            return ResponseEntity.ok(requests);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<?> getSentRequests(
            @PathVariable Long userId) {
        try {
            List<SwapRequestResponse> requests =
                    requestService.getSentRequests(userId);

            return ResponseEntity.ok(requests);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    @PutMapping("/{requestId}/{status}")
    public ResponseEntity<?> updateRequestStatus(
            @PathVariable Long requestId,
            @PathVariable String status,
            @RequestParam Long userId) {
        try {
            SwapRequestResponse response =
                    requestService.updateRequestStatus(
                            requestId, userId, status
                    );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", exception.getMessage()));
        }
    }
}