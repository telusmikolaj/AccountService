package account;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateDeserializer extends StdDeserializer<Date> {
    private SimpleDateFormat formatter =
            new SimpleDateFormat("MM-yyyy");

    protected CustomDateDeserializer() {
        this((Class<?>) null);
    }

    protected CustomDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date = p.getText();
        String strPattern = "^((0[1-9]|1[012])-\\d{4}$)";

        if (!date.matches(strPattern)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong date " + date);
        }


        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
