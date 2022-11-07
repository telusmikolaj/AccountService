package account.Controller;

import account.Model.Payment;
import account.Model.User;
import account.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("api/acct/payments")
    public String addPayment(@Valid @RequestBody List<Payment> payments) {


        return paymentService.addPayment(payments);
    }

    @PutMapping("/api/acct/payments")
    public String updatePayment(@Valid @RequestBody Payment updatedPayment) throws ParseException {
        return paymentService.updatePayment(updatedPayment);
    }

    @GetMapping("/api/empl/payment")
    public String getPayment(@RequestParam(required = false) String period) throws ParseException {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String email = loggedInUser.getName();

        return paymentService.getPayment(email, period);
    }

}
