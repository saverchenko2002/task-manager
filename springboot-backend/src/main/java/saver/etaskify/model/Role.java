package saver.etaskify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "ROLE")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role {

    @Id
    @Column(name = "ROLE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ROLE_NAME")
    @Enumerated(EnumType.STRING)
    private RoleName role;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> userList = new HashSet<>();
}
