package quarkus.example.feature.list;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quarkus.example.test.IntegrationTest;

import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static com.natpryce.makeiteasy.MakeItEasy.an;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static quarkus.example.feature.list.ItemListMaker.ItemList;

@IntegrationTest
@QuarkusTest
@Transactional
public class JpaItemListRepositoryTest {

    @Inject
    JpaItemListRepository repository;

    @BeforeEach
    public void beforeEach() {
        ItemListEntity.deleteAll();
    }

    @Test
    public void notFoundId() {
        Optional<ItemList> result = repository.findById(ItemListId.create());
        assertFalse(result.isPresent());
    }

    @Test
    public void add() {
        ItemList list = make(an(ItemList));
        repository.add(list);
        Optional<ItemList> found = repository.findById(list.getId());
        assertTrue(found.isPresent());
        assertTrue(list.getId().sameAs(found.get().getId()));
        assertTrue(list.getName().sameAs(found.get().getName()));
    }

    @Test
    public void listEmptyRepository() {
        List<ItemList> items = repository.list();
        assertTrue(items.isEmpty());
    }

    @Test
    public void listNotEmptyRepository() {
        repository.add(make(an(ItemList)));
        repository.add(make(an(ItemList)));
        List<ItemList> items = repository.list();
        assertEquals(2, items.size());
    }

}
