package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.entity.OrderEntity;
import com.apec_finance.trading.mapper.OrderMapper;
import com.apec_finance.trading.model.*;
import com.apec_finance.trading.model.order.*;
import com.apec_finance.trading.repository.OrderRepository;
import com.apec_finance.trading.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CashClient cashClient;
    private final KeycloakService keycloakService;
    private final AppClient appClient;
    private final OrderNoCacheService orderNoCacheService;
    private final OrderMapper orderMapper;

    public List<Issuer> getIssuerAvailableLimits(List<Integer> productIds) {
        List<IssuerProjection> results = orderRepository.getIssuerAvailableLimits(productIds);
        List<Issuer> issuers = new ArrayList<>();

        for (IssuerProjection result : results) {
            Integer productId = result.getProductId();
            BigDecimal availableLimit = result.getAvailableLimit();

            if (availableLimit != null) {
                availableLimit = availableLimit.setScale(2, RoundingMode.HALF_UP);
            }

            Issuer issuer = new Issuer();
            issuer.setProductId(productId);
            issuer.setAvailableLimit(availableLimit);

            issuers.add(issuer);
        }

        return issuers;
    }

    @Override
    public BigDecimal getAvailableLimitByInvestor(Integer productId) {
        Float orderValue = orderRepository.getOrderValueByInvestor(productId, keycloakService.getInvestorIdFromToken());

        if (orderValue == null) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(orderValue);
    }


    @Override
    public Order createDeposite(OrderDepositRQ rq) {
        if (!validateAmount(rq)) return null;

//        List<TransactionRange> transactionRanges = appClient.getTransactionRange();
        AppStockRS stockRS = appClient.getStockRs(rq.getProductId(), "*,stock_product_detail.*");
        AppProduct appProduct = stockRS.getData().get(0);
        AppStockProductDetail productDetail = appProduct.getStockProductDetail();
        Long orderValue = rq.getOrderQuantity() * appProduct.getParValue();
        Long parValue = appProduct.getParValue();

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setInvestorId(keycloakService.getInvestorIdFromToken())
                .setOrderNo(generateOrderNo())
                .setInvestorAccountNo(rq.getInvestorAccountNo())
                .setOrderDate(LocalDate.now())
                .setOrderTime(LocalDateTime.now())
                .setOrderSide("B")
                .setOrderType(1)
                .setOrderStatus(1)
                .setProductId(rq.getProductId())
                .setProductCode(rq.getProductCode())
                .setParValue(parValue.floatValue())
                .setOrderQuantity(rq.getOrderQuantity())
                .setOrderPrice(parValue.floatValue())
                .setOrderValue(orderValue.floatValue())
                .setInterestRate(appProduct.getInterestRate())
                .setPaidAmount(orderValue.floatValue())
                .setCreatedBy(keycloakService.getNameFromToken());
        orderRepository.save(orderEntity);

        UpdateCashBalance updateCashBalance = new UpdateCashBalance();
        updateCashBalance.setInvestorId(keycloakService.getInvestorIdFromToken());
        updateCashBalance.setPaidAmount((float) orderValue);

        CreateCashTransaction createCashTransaction = new CreateCashTransaction();
        createCashTransaction.setOpr("-");
        createCashTransaction.setInvestorId(keycloakService.getInvestorIdFromToken());
        createCashTransaction.setTranDate(LocalDate.now());
        createCashTransaction.setTranTime(LocalDateTime.now());
        createCashTransaction.setRefNo(orderEntity.getOrderNo());
        createCashTransaction.setRefId(orderEntity.getId());
        createCashTransaction.setTranType("ORD");
        createCashTransaction.setStatus("P");
        createCashTransaction.setTranAmount(orderValue.floatValue());
        createCashTransaction.setCreatedBy(keycloakService.getNameFromToken());

        try {
            cashClient.updateCashBalance("Bearer " + keycloakService.getToken(), updateCashBalance);
            cashClient.createCashTransaction("Bearer " + keycloakService.getToken(), createCashTransaction);
            log.info("Updated to cash service");
        } catch (Exception e) {
            log.error("Fail to update cash service " + e.getMessage());
        }


        return orderMapper.getDTO(orderEntity);
    }

    public Boolean validateAmount(OrderDepositRQ rq) {
        ResponseBuilder<InvestorCashBalance> cashBalance = cashClient.getBalance("Bearer " + keycloakService.getToken());
        BigDecimal totalBalance = cashBalance.getResult().getTotalBalance();

        if (totalBalance == null || rq.getAmount().compareTo(totalBalance) > 0) {
            return false;
        }

        var issuer = getIssuerAvailableLimits(List.of(rq.getProductId()));
        if (!issuer.isEmpty()) {
            BigDecimal availableLimit = issuer.get(0).getAvailableLimit();

            if (availableLimit == null || rq.getSumInvestmentLimit() == null || availableLimit.add(rq.getAmount()).compareTo(new BigDecimal(rq.getSumInvestmentLimit())) > 0) {
                return false;
            }
        }

        Float orderValue = orderRepository.getOrderValueByInvestor(rq.getProductId(), keycloakService.getInvestorIdFromToken());
        if (orderValue == null) orderValue = 0f;
        if ((orderValue + rq.getAmount().doubleValue()) > rq.getPersonalInvestmentLimit())
            return false;

        return true;
    }

    public String generateOrderNo() {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        int orderSequence = orderNoCacheService.generateOrderNo();
        return "O" + datePrefix + String.format("%04d", orderSequence);
    }

}
