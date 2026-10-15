package CampusSkillSwap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String classYear;

    private String skillToTeach;

    private String skillToLearn;

    public User() {
    }

    public User(String name, String email, String password,
                String department, String classYear,
                String skillToTeach, String skillToLearn) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.classYear = classYear;
        this.skillToTeach = skillToTeach;
        this.skillToLearn = skillToLearn;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSkillToTeach() {
        return skillToTeach;
    }

    public void setSkillToTeach(String skillToTeach) {
        this.skillToTeach = skillToTeach;
    }

    public String getSkillToLearn() {
        return skillToLearn;
    }

    public void setSkillToLearn(String skillToLearn) {
        this.skillToLearn = skillToLearn;
    }
}