package ch.uzh.ifi.group26.scrumblebee.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "sb_refreshToken")
public class RefreshToken {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    public void setId(Long id) { this.id = id; }

    public Long getId() { return id; }

    public void setUser(User user) { this.user = user; }

    public User getUser() { return user; }

    public void setToken(String token) { this.token = token; }

    public String getToken() { return token; }

    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }

    public Instant getExpiryDate() { return expiryDate; }

}
