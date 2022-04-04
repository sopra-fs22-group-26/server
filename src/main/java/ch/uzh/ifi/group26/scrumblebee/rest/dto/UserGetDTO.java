package ch.uzh.ifi.group26.scrumblebee.rest.dto;

import org.mapstruct.Mapping;

import java.text.SimpleDateFormat;

public class UserGetDTO {

    private Long id;
    private String username;
    private String emailAddress;
    private String name;
    private java.util.Date birthDate;
    private java.util.Date creationDate;
    private boolean loggedIn;
    private String token;
    private int score;

    private static final SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    public void setId(Long id) {
    this.id = id;
  }

    public Long getId() {
        return id;
    }

    public void setUsername(String username) { this.username = username; }

    public String getUsername() { return username; }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getEmailAddress() { return emailAddress; }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setBirthDate(java.util.Date birthDate) { this.birthDate = birthDate; }

    public String getBirthDate() {
        if ( birthDate == null ) { return null; }
        return dateFormat.format(birthDate);
    }

    public void setCreationDate(java.util.Date creationDate) { this.creationDate = creationDate; }

    public String getCreationDate() { return dateFormat.format(creationDate); }

    public void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }

    public boolean getLoggedIn() { return loggedIn; }

    public void setToken(String token) { this.token = token; }

    public String getToken() { return token; }

    public void setScore(int score) { this.score = score; }

    public int getScore() { return score; }

}
