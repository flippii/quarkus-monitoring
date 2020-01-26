package quarkus.example.feature.list;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "list")
public class ItemListEntity extends PanacheEntityBase {

    @Id
    @Column(name = "id")
    public UUID id;

    @Column(name = "name")
    public String name;

    public ItemListEntity() {
        super();
    }

    public ItemListEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

}
