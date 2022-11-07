package account.Model;

import account.CustomSetSerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column
    @NotBlank
    @Email(regexp = ".+@acme\\.com$")
    private String email;

    @Column
    @NotBlank
    private String lastname;


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })

    @JoinTable(name = "user_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"
            ))
    @JsonSerialize(using = CustomSetSerializer.class)
    @JsonProperty(value = "roles")
    private Set<Group> userGroups = new TreeSet<>(new Comparator<Group>() {
        @Override
        public int compare(Group o1, Group o2) {
            return o1.getName().length() - o2.getName().length();
        }
    });


    public void addUserGroups(Group group) {

        this.userGroups.add(group);
    }

    public void removeUserGroup(String groupToRemove) {
        Set <Group> filteredRoles = this.userGroups.stream()
                .filter(group -> !group.getName().equals(groupToRemove))
                .collect(Collectors.toSet());

        this.setUserGroups(filteredRoles);
    }

//    public String toString() {
//        return "{ \n \"id\": " + this.getId() + ", \n" +
//                "\"name\": " + "\"" + this.getName() + "\", \n" +
//                "\"lastname\": " + "\"" + this.getLastname() + "\", \n" +
//                "\"email\": " + "\"" + this.getEmail() + "\", \n" + this.roleOrder();
//    }
//
//
//    public String roleOrder() {
//        Iterator<Group> groupIterator = this.userGroups.iterator();
//        String result = "\"roles\": [ \n" ;
//
//        while (groupIterator.hasNext()) {
//            result += "\"" + groupIterator.next().getName() + "\"";
//            if (groupIterator.hasNext()) result += ",";
//            result += "\n";
//        }
//
//        result += "] \n }";
//
//        return result;
//    }


}
