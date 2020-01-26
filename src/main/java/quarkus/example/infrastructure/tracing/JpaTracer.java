package quarkus.example.infrastructure.tracing;

import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Dependent
public class JpaTracer implements Tracer {

    @Override
    @Transactional
    public void trace(Trace trace) {
        TraceEntity traceEntity = new TraceEntity();
        traceEntity.type = trace.getType();
        traceEntity.occurredAt = LocalDateTime.now();
        TraceEntity.persist(traceEntity);
    }

}
