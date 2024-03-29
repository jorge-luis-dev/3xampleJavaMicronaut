package expert.allku;

import expert.allku.dto.BeerDtoIn;
import expert.allku.dto.IngredientDto;
import expert.allku.dto.LocationDto;
import expert.allku.repository.IBeerRepo;
import expert.allku.repository.ILocationRepo;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;


@Singleton
public class DataLoader implements ApplicationEventListener<ServerStartupEvent> {

    protected final IBeerRepo beerRepository;
    protected final ILocationRepo ILocationRepo;

    public DataLoader(
            IBeerRepo beerRepository,
            ILocationRepo ILocationRepo) {
        this.beerRepository = beerRepository;
        this.ILocationRepo = ILocationRepo;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {

        var locationRoot = new LocationDto("Tierra", "", "Activo");

        var america = new LocationDto("America", "", "Activo");
        var europa = new LocationDto("Europa", "", "Activo");

        america.getChildren().add(new LocationDto("Ecuador", "", "Activo"));
        europa.getChildren().add(new LocationDto("Alemania", "", "Activo"));

        locationRoot.getChildren()
                .add(america);
        locationRoot.getChildren()
                .add(europa);

        ILocationRepo.save(locationRoot);

        var locationBeer = ILocationRepo.findByName(america.getName());

        var beer1 = new BeerDtoIn("Calenturienta",
                "Cervezas Jorge Luis",
                "2021-10-27");

        if (locationBeer.isPresent())
            beer1.setLocationId(locationBeer.get().getId());

        beer1.getIngredients().add(new IngredientDto("Malta"));
        beer1.getIngredients().add(new IngredientDto("Agua"));
        beer1.getIngredients().add(new IngredientDto("Canela"));

        beerRepository.save(beer1);

        var beer2 = new BeerDtoIn("Pilsener",
                "Cervezas Nacionales",
                "1990-01-01");

        if (locationBeer.isPresent())
            beer2.setLocationId(locationBeer.get().getId());

        beer2.getIngredients().add(new IngredientDto("Malta"));
        beer2.getIngredients().add(new IngredientDto("Agua"));

        beerRepository.save(beer2);

        System.out.println("Insert data");
    }
}
