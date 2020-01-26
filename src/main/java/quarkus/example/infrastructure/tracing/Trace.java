package quarkus.example.infrastructure.tracing;

public class Trace {

    private final String type;

    public Trace(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
