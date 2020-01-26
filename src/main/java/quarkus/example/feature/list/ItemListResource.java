package quarkus.example.feature.list;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.logging.Logger;
import quarkus.example.infrastructure.tracing.Traceable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@RequestScoped
@Path("/list")
public class ItemListResource {

    private static final Logger LOGGER = Logger.getLogger(ItemListResource.class);

    private ItemListRepository itemListRepository;
    private final AtomicInteger counter;

    @Inject
    public ItemListResource(ItemListRepository itemListRepository) {
        this.itemListRepository = itemListRepository;
        this.counter = new AtomicInteger();
    }

    @GET
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    public ItemListRestDto getById(@PathParam("id") UUID id) {
        return itemListRepository.findById(new ItemListId(id)).map(ItemListResource::toRestDto)
                .orElseThrow(NotFoundException::new);
    }

    @GET
    @Path("/")
    @Produces(APPLICATION_JSON)
    @Timeout(250)
    @Fallback(fallbackMethod = "fallbackList")
    @Traceable
    public Collection<ItemListRestDto> list() {
        long started = System.currentTimeMillis();
        final long invocationNumber = counter.getAndIncrement();

        try {
            randomDelay();
            LOGGER.infof("ItemListResource#list() invocation #%d returning successfully", invocationNumber);
            return itemListRepository.list().stream().map(ItemListResource::toRestDto).collect(Collectors.toList());
        } catch (InterruptedException e) {
            LOGGER.errorf("ItemListResource#list() invocation #%d timed out after %d ms",
                    invocationNumber, System.currentTimeMillis() - started);
            return null;
        }
    }

    private void randomDelay() throws InterruptedException {
        Thread.sleep(new Random().nextInt(500));
    }

    public Collection<ItemListRestDto> fallbackList() {
        LOGGER.info("Falling back to ItemListResource#fallbackList()");
        return Collections.emptyList();
    }

    @POST
    @Path("/")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Transactional
    public void add(ItemListRestDto list) {
        itemListRepository.add(toItemList(list));
    }

    private static ItemList toItemList(ItemListRestDto dto) {
        return new ItemList(new ItemListId(dto.id), new ItemListName(dto.name));
    }

    private static ItemListRestDto toRestDto(ItemList list) {
        return new ItemListRestDto(list.getId().value(), list.getName().value());
    }

}
