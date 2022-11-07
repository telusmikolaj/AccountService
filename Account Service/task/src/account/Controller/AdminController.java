package account.Controller;


import account.Model.User;
import account.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class AdminController {

    @Autowired
    UserService userService;

    @PutMapping("api/admin/user/role")
    public User setRole(@RequestBody Map<String, String> json) {


        return userService.setRole(json);
    }


    @GetMapping("api/admin/user")
    public List<User> getAllUsersList() {
        return userService.getAllUsersList();
    }


    @DeleteMapping("api/admin/user/{email}")
    public String deleteUser(@PathVariable String email) {
        return userService.deleteUser(email);
    }

}
