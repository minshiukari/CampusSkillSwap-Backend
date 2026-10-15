package CampusSkillSwap.model;

public class ChatPartnerResponse {

    private Long id;
    private String name;
    private String department;
    private String classYear;

    public ChatPartnerResponse() {
    }

    public ChatPartnerResponse(
            Long id,
            String name,
            String department,
            String classYear) {

        this.id = id;
        this.name = name;
        this.department = department;
        this.classYear = classYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getClassYear() {
        return classYear;
    }

    public void setClassYear(String classYear) {
        this.classYear = classYear;
    }
}