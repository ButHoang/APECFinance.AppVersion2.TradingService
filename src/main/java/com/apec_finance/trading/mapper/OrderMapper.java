package com.apec_finance.trading.mapper;

import com.apec_finance.trading.entity.OrderEntity;
import com.apec_finance.trading.model.order.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {
    Order getDTO(OrderEntity orderEntity);
}
