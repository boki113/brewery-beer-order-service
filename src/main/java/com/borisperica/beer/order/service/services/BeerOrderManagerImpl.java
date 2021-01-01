package com.borisperica.beer.order.service.services;

import com.borisperica.beer.order.service.domain.BeerOrder;
import com.borisperica.beer.order.service.domain.BeerOrderEventEnum;
import com.borisperica.beer.order.service.domain.BeerOrderStatusEnum;
import com.borisperica.beer.order.service.repositories.BeerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class BeerOrderManagerImpl implements BeerOrderManager {

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachineFactory;
    private final BeerOrderRepository beerOrderRepository;

    @Transactional
    @Override
    public BeerOrder newBeerOrder(BeerOrder beerOrder) {

        beerOrder.setId(null);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);

        BeerOrder saved = beerOrderRepository.save(beerOrder);

        sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATE_ORDER);

        return saved;
    }

    private void sendBeerOrderEvent(BeerOrder beerOrder, BeerOrderEventEnum beerOrderEventEnum){
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> sm = build(beerOrder);

        Message message = MessageBuilder.withPayload(beerOrderEventEnum).build();

        sm.sendEvent(message);
    }

    private StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> build(BeerOrder beerOrder){
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> sm = stateMachineFactory.getStateMachine(beerOrder.getId());

        sm.stop();

        sm.getStateMachineAccessor()
            .doWithAllRegions(sma -> {
                sma.resetStateMachine(new DefaultStateMachineContext<>(beerOrder.getOrderStatus(), null, null, null));
            });

        sm.start();

        return sm;
    }




}
