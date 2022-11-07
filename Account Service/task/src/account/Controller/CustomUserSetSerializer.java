package account.Controller;

import account.CustomSetSerializer;
import account.Model.Group;
import account.Model.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomUserSetSerializer extends StdSerializer<Set<User>> {


    public CustomUserSetSerializer() {
        this(null);
    }

    protected CustomUserSetSerializer(Class<Set<User>> t) {
        super(t);
    }

    @Override
    public void serialize(Set<User> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        List<Long> ids = new ArrayList<>();
        for (User user : value) {
            ids.add(user.getId());
        }
        gen.writeObject(ids);

    }

}
