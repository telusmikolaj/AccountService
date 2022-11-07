import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Email exisit")
public class EmailExistException extends RuntimeException {

    public EmailExistException(String cause) {
        super(cause);
        System.out.println("there");

    }
}
