package quarkus.example.feature.list;

import org.junit.jupiter.api.Test;

import javax.ws.rs.NotFoundException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static com.natpryce.makeiteasy.MakeItEasy.an;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.junit.jupiter.api.Assertions.*;
import static quarkus.example.feature.list.ItemListMaker.ItemList;

class ItemListResourceTest {

    ItemListRepository repository;
    ItemListResource resource;

    @Test
    void listNotEmpty() {
        repository = new FakeItemListRepository(make(an(ItemList)));
        resource = new ItemListResource(repository);
        Collection<ItemListRestDto> list = resource.list();
        assertEquals(1, list.size());
    }

    @Test
    void listEmpty() {
        repository = new FakeItemListRepository();
        resource = new ItemListResource(repository);
        Collection<ItemListRestDto> list = resource.list();
        assertEquals(0, list.size());
    }

    @Test
    void getItemList() {
        ItemList itemList = make(an(ItemList));
        repository = new FakeItemListRepository(itemList);
        resource = new ItemListResource(repository);
        ItemListRestDto dto = resource.getById(itemList.getId().value());
        assertNotNull(dto);
        assertEquals(itemList.getId().value(), dto.id);
        assertEquals(itemList.getName().value(), dto.name);
    }

    @Test
    void getNonexistentItemList() {
        repository = new FakeItemListRepository();
        resource = new ItemListResource(repository);
        assertThrows(NotFoundException.class, () -> resource.getById(UUID.randomUUID()));
    }

    @Test
    void add() {
        repository = new FakeItemListRepository();
        resource = new ItemListResource(repository);
        ItemListRestDto dto = new ItemListRestDto(UUID.randomUUID(), "Test List");
        resource.add(dto);
        Optional<ItemList> itemList = repository.findById(new ItemListId(dto.id));
        assertTrue(itemList.isPresent());
        assertEquals(dto.id, itemList.get().getId().value());
        assertEquals(dto.name, itemList.get().getName().value());
    }

}