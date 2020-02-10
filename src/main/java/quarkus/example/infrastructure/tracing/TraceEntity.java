package quarkus.example.infrastructure.tracing;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trace")
public class TraceEntity extends PanacheEntityBase {

    @Id
    @Column(name = "id")
    public UUID id;

    @Column(name = "type")
    public String type;
    
    @Column(name = "occurred_at")
    public LocalDateTime occurredAt;
    
}
