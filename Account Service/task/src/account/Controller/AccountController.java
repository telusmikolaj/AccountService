package account.Controller;

import account.Model.Payment;
import account.Model.User;
import account.Repository.UserRepository;
import account.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    UserService userService;


    @PostMapping("api/auth/signup")
    public User signup(@Valid @RequestBody User user) throws IOException {

        return userService.save(user);

    }


    @PostMapping("api/auth/changepass")
    public String changePass(@RequestBody Map<String,String> newPasswordMap) {
        String newPassword = newPasswordMap.get("new_password");

        return userService.changeUserPassword(newPassword);

    }
}
