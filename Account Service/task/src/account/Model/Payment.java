package account.Model;


import account.CustomDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "employee")
    @NotBlank
    String employee;

    @Column(name ="period")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    Date period;

    @Column(name = "salary")
    Long salary;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    User user;

    @Override
    public String toString() {
        return "{ \n " +
                "\"name\": " + "\"" + user.getName() + "\", \n" +
                "\"lastname\": " + "\"" + user.getLastname() + "\", \n" +
                "\"period\": " + "\"" + getDate() + "\", \n" +
                "\"salary\": " + "\"" + getSalaryInDollarCentsForm() + "\" \n" +
                "}";
    }

    public String getSalaryInDollarCentsForm() {
        long dollars = salary/100;
        long cents = salary%100;

        return dollars + " dollar(s) " + cents + " cent(s)";

    }

    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(period);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        String cap = month.substring(0, 1).toUpperCase() + month.substring(1);

        return cap + "-" + calendar.get(Calendar.YEAR);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
