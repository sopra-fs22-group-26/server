package ch.uzh.ifi.group26.scrumblebee.rest.dto;

public class UserPostDTO {

    private String name;
    private String username;
    private String emailAddress;
    private String password;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
    return name;
  }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
    return username;
  }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getEmailAddress() { return emailAddress; }

    public void setPassword(String password) { this.password = password; }

    public String getPassword() { return password; }
}
