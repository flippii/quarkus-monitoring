package quarkus.example.infrastructure.json;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonbContextResolver implements ContextResolver<Jsonb> {

    private final Jsonb jsonb;

    public JsonbContextResolver() {
        jsonb = JsonbBuilder.create(new JsonbConfig().withSerializers(new UUIDJsonbSerializer()));
    }

    @Override
    public Jsonb getContext(Class<?> type) {
        return jsonb;
    }
}
