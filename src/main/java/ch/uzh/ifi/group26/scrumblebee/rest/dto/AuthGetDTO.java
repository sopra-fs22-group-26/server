package ch.uzh.ifi.group26.scrumblebee.rest.dto;

public class AuthGetDTO {
    private Long id;
    private String username;
    private String token;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
