package ch.uzh.ifi.group26.scrumblebee.rest.dto;

public class AuthGetDTO {
    private String username;
    private String emailAddress;
    private String token;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getEmailAddress() { return emailAddress; }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
