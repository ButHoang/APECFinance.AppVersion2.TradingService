package com.apec_finance.trading.repository;

import com.apec_finance.trading.entity.OrderEntity;
import com.apec_finance.trading.model.IssuerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("SELECT o.productId AS productId, " +
            "SUM(o.orderValue) AS availableLimit " +
            "FROM OrderEntity o " +
            "WHERE o.productId IN (:productIds) " +
            "AND o.orderType = 1 " +
            "AND (o.orderStatus = 1 OR o.orderStatus = 2) " +
            "GROUP BY o.productId")
    List<IssuerProjection> getIssuerAvailableLimits(@Param("productIds") List<Integer> productIds);

    @Query("SELECT SUM(o.orderValue) " +
            "FROM OrderEntity o " +
            "WHERE o.productId = :productId " +
            "AND o.investorId = :investorId " +
            "AND o.orderType = 1 " +
            "AND (o.orderStatus = 1 OR o.orderStatus = 2) " +
            "GROUP BY o.productId, o.investorId ")
    Float getOrderValueByInvestor(@Param("productId") Integer productId, @Param("investorId") Long investorId);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderNo LIKE :orderNoPrefix%")
    long countOrdersByPrefix(@Param("orderNoPrefix") String orderNoPrefix);

    List<OrderEntity> findByAssetNoInAndOrderSideAndOrderTypeInAndOrderStatus(
            List<String> assetNos,
            String orderSide,
            List<Integer> orderTypes,
            Integer orderStatus
    );

    @Query("SELECT o FROM OrderEntity o WHERE o.id IN :orderIds")
    List<OrderEntity> findOrderEntitiesByOrderIds(@Param("orderIds") Set<Long> orderIds);



}