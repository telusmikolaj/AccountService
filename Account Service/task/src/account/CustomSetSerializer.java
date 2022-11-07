package account;

import account.Model.Group;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomSetSerializer extends StdSerializer<Set<Group>> {


    public CustomSetSerializer() {
        this(null);
    }

    protected CustomSetSerializer(Class<Set<Group>> t) {
        super(t);
    }

    @Override
    public void serialize(Set<Group> value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        List<String> names = new ArrayList<>();
        for (Group group : value) {
            names.add(group.getName());
        }
        gen.writeObject(names);

    }
}
