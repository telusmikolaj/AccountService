package account.Service;

import account.Model.BreachedPasswords;
import account.Model.Group;
import account.Model.User;
import account.Repository.GroupRepository;
import account.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loadedUser = userRepository.findByEmailIgnoreCase(username);

        if (loadedUser == null){
            throw new UsernameNotFoundException(username);

        }


        return org.springframework.security.core.userdetails.User.withUsername(loadedUser.getEmail())
                .password(loadedUser.getPassword())
                .authorities(getAuthorities(loadedUser)).build();
    }

    private Collection<GrantedAuthority> getAuthorities(User user){
        Set<Group> userGroups = user.getUserGroups();
        Collection<GrantedAuthority> authorities = new ArrayList<>(userGroups.size());
        for(Group userGroup : userGroups){
            authorities.add(new SimpleGrantedAuthority(userGroup.getName().toUpperCase()));
        }

        return authorities;
    }

    public User save (User user) {

        checkIfUserEmailExist(user.getEmail());
        checkPasswordLength(user.getPassword());
        checkIfPasswordSafe(user.getPassword());

        user.setEmail(user.getEmail().toLowerCase(Locale.ROOT));
        user.setPassword(encoder.encode(user.getPassword()));
        user.addUserGroups(selectGroupForUser());
       return userRepository.save(user);
    }


    public void checkIfUserEmailExist(String email) {
        if (userRepository.existsByEmail(email.toLowerCase(Locale.ROOT))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }
    }

    public void checkIfUserEmailDoesNotExist(String email) {
        if (!userRepository.existsByEmail(email.toLowerCase(Locale.ROOT))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
    }

    public void checkIfPasswordSafe(String password) {

        BreachedPasswords breachedPasswords = new BreachedPasswords();
        if (breachedPasswords.ifContainsBreachedPassword(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
    }

    public void checkPasswordLength(String password) {
        if(password.length() < 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password length must be at least 12 chars!");

        }
    }

    public String changeUserPassword(String newPassword) {

        Authentication loggedInUserDetails = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmailIgnoreCase(loggedInUserDetails.getName());

        String currentPassword = user.getPassword();

        checkIfPasswordAreDiffrent(newPassword, currentPassword);
        checkPasswordLength(newPassword);
        checkIfPasswordSafe(newPassword);

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        return "{ \n" +
                "\"email\": " + "\"" + user.getEmail() + "\", \n" +
                "\"status\": " + "\"" + "The password has been updated successfully" +"\" \n" +
                "}";
    }

    public void checkIfPasswordAreDiffrent(String newPassword, String currentPassword) {
        if (encoder.matches(newPassword, currentPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }
    }

    public Group selectGroupForUser() {
        long numberOfUsers = userRepository.count();

        if (numberOfUsers == 0) {
            return groupRepository.findGroupByName("ROLE_ADMINISTRATOR");
        }

        return groupRepository.findGroupByName("ROLE_USER");

    }

    public Group grantGroupForUser(String role) {

        return groupRepository.findGroupByName(role);
    }

    public List<User> getAllUsersList() {
        return userRepository.findAll();
    }

    public String deleteUser(String email) {
        checkIfUserEmailDoesNotExist(email);
        User userToDelete = userRepository.findByEmailIgnoreCase(email);
        userToDelete.getUserGroups().forEach((group -> {
            if (group.getName().equals("ROLE_ADMINISTRATOR")) throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }));

        userRepository.delete(userToDelete);

        return String.format("""
                {
                   "user": "%s",
                   "status": "Deleted successfully!"
                }""", email);

    }

    public void checkIfRoleExsist(String role) {
        String roleName = "ROLE_" + role;
        if (!groupRepository.existsGroupByName(roleName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }
    }

    public void checkIfUserHaveAroleForRemove(User user, String role) {
        String roleName = "ROLE_" + role;
        Set<Group> roles = user.getUserGroups();
        Set <Group> searchedRole = roles.stream()
                .filter(group -> group.getName().equals(roleName))
                .collect(Collectors.toSet());

        if (searchedRole.isEmpty()) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
        }

    }

    public void checkIfUserHaveRoleForGrant(User user, String role) {
        String roleName = "ROLE_" + role;
        Set<Group> roles = user.getUserGroups();
        Set <Group> searchedRole = roles.stream()
                .filter(group -> group.getName().equals(roleName))
                .collect(Collectors.toSet());

        if (!searchedRole.isEmpty()) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "User have this role !");
        }
    }

    public void checkIfUserHaveMoreThanOneRole(User user) {
        Set<Group> roles = user.getUserGroups();

        if (roles.size() == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
        }
    }


    public boolean checkIfUserIsAdministrator(User user) {
        Set <Group> searchedRole = user.getUserGroups().stream()
                .filter(group -> group.getName().equals("ROLE_ADMINISTRATOR"))
                .collect(Collectors.toSet());

        return !searchedRole.isEmpty();
    }

    public boolean checkIfUserHaveBuisnessRole(User user) {
        Set <Group> searchedRole = user.getUserGroups().stream()
                .filter(group -> group.getName().equals("ROLE_ACCOUNTANT") || group.getName().equals("ROLE_USER"))
                .collect(Collectors.toSet());

        return !searchedRole.isEmpty();
    }


    public User setRole(Map<String, String> json) {
        String email = json.get("user");
        String role = json.get("role");
        String operation = json.get("operation");
        String roleName = "ROLE_" + role;
        checkIfUserEmailDoesNotExist(email);
        checkIfRoleExsist(role);

        User user = userRepository.findByEmailIgnoreCase(email);
        if (role.equals("ADMINISTRATOR") && checkIfUserHaveBuisnessRole(user)) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        }

        if (checkIfUserIsAdministrator(user) && (role.equals("USER") || role.equals("ACCOUNTANT"))) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        }

        if ("GRANT".equals(operation)) {
            checkIfUserHaveRoleForGrant(user, role);
            Set<Group> saveUserGroups = user.getUserGroups();
            Set<Group> updatedUsersGrops = new TreeSet<>(new Comparator<Group>() {
                @Override
                public int compare(Group o1, Group o2) {
                    return o2.getName().length() - o1.getName().length();
                }
            });

            updatedUsersGrops.add(grantGroupForUser(roleName));
            updatedUsersGrops.addAll(saveUserGroups);
            user.setUserGroups(updatedUsersGrops);

        } else if ("REMOVE".equals(operation)) {
            if (role.equals("ADMINISTRATOR") && checkIfUserIsAdministrator(user)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
            }
            checkIfUserHaveAroleForRemove(user, role);
            checkIfUserHaveMoreThanOneRole(user);
            user.removeUserGroup(roleName);
        }


        return userRepository.save(user);
    }





}
