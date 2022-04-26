package ch.uzh.ifi.group26.scrumblebee.rest.dto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserPutDTO {
    private String name;
    private String username;
    private String emailAddress;
    private Date birthDate;
    private String password;
    private String newPassword;
    private int score;

    private final Logger log = LoggerFactory.getLogger(UserPostDTO.class);

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    // User parameters
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

    public void setBirthDate(String birthDate){ try {
        this.birthDate = formatter.parse(birthDate);
    }
    catch(Exception e){
        log.debug(e.getMessage());
    }}

    public Date getBirthDate(){ return birthDate; }

    public void setPassword(String password) { this.password = password; }

    public String getPassword() { return password; }

    public void setNewPassword(String newPassword){ this.newPassword = newPassword; }

    public String getNewPassword(){ return newPassword; }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
