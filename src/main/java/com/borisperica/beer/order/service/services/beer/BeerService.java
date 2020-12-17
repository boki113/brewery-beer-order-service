package com.borisperica.beer.order.service.services.beer;

import com.borisperica.beer.order.service.web.model.BeerDto;

import java.util.Optional;
import java.util.UUID;

/**
 * Service to call beer-service to receive BeerDto.
 */
public interface BeerService {

    Optional<BeerDto> getBeerById(UUID id);

    Optional<BeerDto> getBeerByUpc(String upc);
}
