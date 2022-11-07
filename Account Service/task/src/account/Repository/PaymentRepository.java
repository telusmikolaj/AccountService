package account.Repository;

import account.Model.Payment;
import account.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {

    Payment findPaymentByEmployeeAndPeriod(String employee, Date period);
    List<Payment> findPaymentByEmployeeOrderByPeriodDesc(String employee);

}
