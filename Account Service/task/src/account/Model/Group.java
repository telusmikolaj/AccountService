package account.Model;


import account.Controller.CustomUserSetSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "Groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "userGroups")
    @JsonSerialize(using = CustomUserSetSerializer.class)
    private Set<User> users;


    public Group(String name) {
        this.name = name;
    }

    public Group() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUser() {
        return users;
    }

    public void setUser(Set<User> users) {
        this.users = users;
    }


}
