package com.borisperica.beer.order.service.web.mappers;

import com.borisperica.beer.order.service.domain.BeerOrderLine;
import com.borisperica.beer.order.service.services.beer.BeerService;
import com.borisperica.brewery.model.BeerDto;
import com.borisperica.brewery.model.BeerOrderLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper {

    private BeerService beerService;
    private BeerOrderLineMapper beerOrderLineMapper;

    @Autowired
    public void setBeerService(BeerService beerService) {
        this.beerService = beerService;
    }

    @Autowired
    @Qualifier("delegate")
    public void setBeerOrderLineMapper(BeerOrderLineMapper beerOrderLineMapper) {
        this.beerOrderLineMapper = beerOrderLineMapper;
    }

    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine beerOrderLine){
        BeerOrderLineDto orderLineDto = beerOrderLineMapper.beerOrderLineToDto(beerOrderLine);
        Optional<BeerDto> beerDtoOptional = beerService.getBeerByUpc(beerOrderLine.getUpc());

        beerDtoOptional.ifPresent(beerDto -> {
            orderLineDto.setBeerName(beerDto.getBeerName());
            orderLineDto.setBeerStyle(beerDto.getBeerStyle());
            orderLineDto.setPrice(beerDto.getPrice());
            orderLineDto.setId(beerDto.getId());
        });

        return orderLineDto;
    }
}
