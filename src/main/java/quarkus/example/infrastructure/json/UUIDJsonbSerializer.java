package quarkus.example.infrastructure.json;

import javax.json.stream.JsonGenerator;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import java.util.UUID;

public class UUIDJsonbSerializer implements JsonbSerializer<UUID> {

    @Override
    public void serialize(UUID obj, JsonGenerator generator, SerializationContext ctx) {
        generator.write(obj.toString());
    }

}
