package ch.uzh.ifi.group26.scrumblebee.entity;

import ch.uzh.ifi.group26.scrumblebee.constant.RoleType;

import javax.persistence.*;

@Entity
@Table(name = "sb_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column()
    private RoleType roleName;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public RoleType getRoleName() { return roleName; }

    public void setRoleName(RoleType role) { this.roleName = role; }

}
