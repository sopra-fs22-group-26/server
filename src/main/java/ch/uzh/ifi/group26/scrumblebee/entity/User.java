package ch.uzh.ifi.group26.scrumblebee.entity;

import ch.uzh.ifi.group26.scrumblebee.constant.RoleType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
@Table(name = "sb_user")
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
    private int score;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sb_user_roles",
            joinColumns = @JoinColumn(name = "sb_user_id"),
            inverseJoinColumns = @JoinColumn(name = "sb_role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "invitees")
    private Set<PollMeeting> meeting_invitations = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<PollMeeting> meeting_participations = new HashSet<>();


    /**
     * Getter & setter methods
     */

    public void setId(Long id) { this.id = id; }

    public Long getId() { return id; }

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

    public void setScore(int score) { this.score = score; }

    public int getScore() { return score; }

    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public Set<Role> getRoles() { return roles; }

    public void setMeeting_invitations(Set<PollMeeting> meeting_invitations) {
        this.meeting_invitations = meeting_invitations;
    }

    public Set<PollMeeting> getMeeting_invitations() {
        return meeting_invitations;
    }

    public void setMeeting_participations(Set<PollMeeting> meeting_participations) {
        this.meeting_participations = meeting_participations;
    }

    public Set<PollMeeting> getMeeting_participations() {
        return meeting_participations;
    }


    /**
     * Additional functions to manipulate data
     */

    public void addScore(int additionalScore) { this.score += additionalScore; }

}
