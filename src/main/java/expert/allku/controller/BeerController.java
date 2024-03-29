package expert.allku.controller;

import expert.allku.dto.BeerDtoForList;
import expert.allku.dto.BeerDtoIn;
import expert.allku.dto.Message;
import expert.allku.dto.MessageDelete;
import expert.allku.model.Beer;
import expert.allku.repository.IBeerRepo;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller("/rest/v1")
public class BeerController {

  protected final IBeerRepo beerRepository;

  public BeerController(IBeerRepo beerRepository) {
    this.beerRepository = beerRepository;
  }

  @Get(value = "/beers")
  public List<BeerDtoForList> all() {
    return beerRepository.findAll();
  }

  @Get(value = "/beer/{id}")
  public HttpResponse<?> show(Integer id) {
    var beer = beerRepository
            .findById(id).orElse(null);

    if (beer == null) {
      return HttpResponse.notFound(new Message("Beer not found"));
    }
    return HttpResponse.ok(beer);
  }

  @Post(value = "/beer")
  public HttpResponse<Beer> save(@Body @Valid BeerDtoIn beerDtoIn) {
    Beer beer = beerRepository.save(beerDtoIn);

    return HttpResponse
            .created(beer)
            .headers(headers -> headers.location(location(beer.getId())));
  }

  @Put(value = "/beer/{id}")
  public HttpResponse update(Integer id, @Body @Valid BeerDtoIn beerDtoIn) {
    var beerId = beerRepository.update(id, beerDtoIn);

    return HttpResponse
            .noContent()
            .headers(headers -> headers.location(location(beerId)));
  }

  @Delete(value = "/beer/{id}")
  public HttpResponse<?> delete(Integer id) {
    var beer = beerRepository
            .findById(id).orElse(null);

    if (beer == null) {
      return HttpResponse.notFound(new Message("Beer not found"));
    }

    beerRepository.delete(id);
    return HttpResponse.ok(new MessageDelete(
            beer.getId(),
            beer.getName()
    ));
  }

  protected URI location(Integer id) {
    return URI.create("/beer/" + id);
  }
}