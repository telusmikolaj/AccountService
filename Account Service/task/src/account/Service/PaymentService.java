package account.Service;


import account.Model.Payment;
import account.Model.User;
import account.Repository.PaymentRepository;
import account.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PaymentRepository paymentRepository;

    @Transactional
    public String addPayment(List<Payment> payments)  {

        payments.forEach(payment -> {
            Long salary = payment.getSalary();

            if (salary < 1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid salary " + salary);

            String employeeMail = payment.getEmployee();
            Date period = payment.getPeriod();

            User employee = userRepository.findByEmailIgnoreCase(employeeMail);
            checkIfPeriodEmployeePairIsUnique(employeeMail, period);

            payment.setUser(employee);
            paymentRepository.save(payment);
        });

        return "{ \n \"status\": \"Added successfully!\"\n } ";
    }


    public void checkIfPeriodEmployeePairIsUnique(String employee, Date period) {
        Payment payment = findPaymentByEmployeeAndPeriod(employee, period);

        if (payment != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment for employee " + employee + " for period " + "exist! ");
        }
    }

    public Payment findPaymentByEmployeeAndPeriod(String employee, Date period) {

        return paymentRepository.findPaymentByEmployeeAndPeriod(employee, period);

    }

    public void checkIfPaymentExist(String employee, Date period) {
        Payment payment = findPaymentByEmployeeAndPeriod(employee, period);

        if (payment == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment for employee " + employee +  " period " + period + " not found");
        }
    }

    public String updatePayment(Payment updatedPayment) throws ParseException {
        String employee = updatedPayment.getEmployee();
        Date period = updatedPayment.getPeriod();
        checkIfPaymentExist(employee, period);

        Payment savedPayment = findPaymentByEmployeeAndPeriod(employee, period);
        updatedPayment.setId(savedPayment.getId());
        updatedPayment.setUser(savedPayment.getUser());
        paymentRepository.save(updatedPayment);

        return "{ \n \"status\": \"Updated successfully!\"\n } ";

    }

    public String getPayment(String email, String periodInString) throws ParseException {
        if (periodInString == null) {
            return getAllUsersPayments(email).toString();
        }

        Date period = convertStringToDate(periodInString);
        checkIfPaymentExist(email, period);

        return findPaymentByEmployeeAndPeriod(email, period).toString();



    }

    public List<Payment> getAllUsersPayments(String email) {
        return paymentRepository.findPaymentByEmployeeOrderByPeriodDesc(email);
    }

    public Date convertStringToDate(String stringDate) throws ParseException {
        DateFormat dateFormat= new SimpleDateFormat("MM-yyyy");
        return dateFormat.parse(stringDate);
    }


    public Date getDateFormat(String date) throws ParseException {
        return new SimpleDateFormat("mm-yyyy").parse(date);
    }








}
