package CampusSkillSwap.model;

import java.time.LocalDateTime;

public class SwapRequestResponse {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String status;
    private LocalDateTime createdAt;

    private String senderName;
    private String senderDepartment;
    private String senderClassYear;

    private String receiverName;
    private String receiverDepartment;
    private String receiverClassYear;

    private String senderSkillToTeach;
    private String senderSkillToLearn;

    private String receiverSkillToTeach;
    private String receiverSkillToLearn;

    public SwapRequestResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderDepartment() {
        return senderDepartment;
    }

    public void setSenderDepartment(String senderDepartment) {
        this.senderDepartment = senderDepartment;
    }

    public String getSenderClassYear() {
        return senderClassYear;
    }

    public void setSenderClassYear(String senderClassYear) {
        this.senderClassYear = senderClassYear;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverDepartment() {
        return receiverDepartment;
    }

    public void setReceiverDepartment(String receiverDepartment) {
        this.receiverDepartment = receiverDepartment;
    }

    public String getReceiverClassYear() {
        return receiverClassYear;
    }

    public void setReceiverClassYear(String receiverClassYear) {
        this.receiverClassYear = receiverClassYear;
    }

    public String getSenderSkillToTeach() {
        return senderSkillToTeach;
    }

    public void setSenderSkillToTeach(String senderSkillToTeach) {
        this.senderSkillToTeach = senderSkillToTeach;
    }

    public String getSenderSkillToLearn() {
        return senderSkillToLearn;
    }

    public void setSenderSkillToLearn(String senderSkillToLearn) {
        this.senderSkillToLearn = senderSkillToLearn;
    }

    public String getReceiverSkillToTeach() {
        return receiverSkillToTeach;
    }

    public void setReceiverSkillToTeach(String receiverSkillToTeach) {
        this.receiverSkillToTeach = receiverSkillToTeach;
    }

    public String getReceiverSkillToLearn() {
        return receiverSkillToLearn;
    }

    public void setReceiverSkillToLearn(String receiverSkillToLearn) {
        this.receiverSkillToLearn = receiverSkillToLearn;
    }
}