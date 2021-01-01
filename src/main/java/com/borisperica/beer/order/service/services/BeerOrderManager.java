package com.borisperica.beer.order.service.services;

import com.borisperica.beer.order.service.domain.BeerOrder;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);
}
