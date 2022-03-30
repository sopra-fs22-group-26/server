package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String emailAddress;

    @Column()
    private String name;

    @Column()
    private java.util.Date birthDate;

    @Column(nullable = false)
    private java.util.Date creationDate;

    @Column(nullable = false)
    private boolean loggedIn;

    @Column(nullable = false)
    private String token;

    @Column(columnDefinition = "0")
    private int score;

    public void setId(long id) { this.id = id; }

    public long getId() { return id; }

    public void setUsername(String username) { this.username = username; }

    public String getUsername() { return username; }

    public void setPassword(String password) { this.password = password; }

    public String getPassword() { return password; }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getEmailAddress() { return emailAddress; }

    public void setName(String name) { this.name = name; }

    public String getName() {  return name; }

    public void setBirthDate(java.util.Date birthDate) { this.birthDate = birthDate; }

    public java.util.Date getBirthDate() { return birthDate; }

    public void setCreationDate(java.util.Date creationDate) { this.creationDate = creationDate; }

    public java.util.Date getCreationDate() { return creationDate; }

    public void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }

    public boolean getLoggedIn() { return loggedIn; }

    public void setToken(String token) { this.token = token; }

    public String getToken() { return token; }

    public void setScore(int score) { this.score = score; }

    public int getScore() { return score; }

}
