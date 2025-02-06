package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.comon.PaginationRS;
import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.entity.InvestorAssetEntity;
import com.apec_finance.trading.entity.OrderEntity;
import com.apec_finance.trading.exception.validate.ValidationException;
import com.apec_finance.trading.mapper.InvestorAssetMapper;
import com.apec_finance.trading.model.asset.*;
import com.apec_finance.trading.repository.AssetInterestScheduleRepository;
import com.apec_finance.trading.repository.InvestorAssetRepository;
import com.apec_finance.trading.repository.OrderRepository;
import com.apec_finance.trading.service.AppClient;
import com.apec_finance.trading.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final InvestorAssetRepository investorAssetRepository;
    private final AssetInterestScheduleRepository assetInterestScheduleRepository;
    private final InvestorAssetMapper investorAssetMapper;
    private final OrderRepository orderRepository;
    private final AppClient appClient;

    @Override
    public AssetRS getListAsset(SearchAssetRQ searchAssetRQ, Long investorId) {
        try {
            if (investorId == null || investorId <= 0) {
                throw new ValidationException("Invalid investor ID");
            }

            AssetRS assetRS = new AssetRS();
            List<Integer> allProductIds = investorAssetRepository.getAllProductId();
            assetRS.setProductIds(allProductIds);

            Pageable pageable = Pageable.unpaged();
            if (searchAssetRQ != null) {
                pageable = PageRequest.of(searchAssetRQ.getPage(), searchAssetRQ.getSize());
            }

            List<Integer> productIds = searchAssetRQ != null ? searchAssetRQ.getProductIds() : null;

            Page<InvestorAssetEntity> assetPage = investorAssetRepository.findByInvestorIdAndStatusAndProductIds(investorId, searchAssetRQ.getStatus(), productIds, pageable);

            if (assetPage.isEmpty()) {
//                throw new ValidationException("No assets found for investor ID: " + investorId);
                return assetRS;
            }

            List<InvestorAssetEntity> assets = assetPage.getContent();

            List<String> assetNos = assets.stream()
                    .map(InvestorAssetEntity::getAssetNo)
                    .distinct()
                    .collect(Collectors.toList());
            if (assetNos.isEmpty()) {
                throw new ValidationException("No assetNo found for investor ID: " + investorId);
            }

            List<OrderEntity> orders = orderRepository.findByAssetNoInAndOrderSideAndOrderTypeInAndOrderStatus(
                    assetNos, "S", Arrays.asList(2, 3), 1);


            Map<String, BigDecimal> totalOrderValueByAssetNo = new HashMap<>();
            Map<String, LocalDate> transDateMap = new HashMap<>();

            for (OrderEntity order : orders) {
                String assetNo = order.getAssetNo();
                BigDecimal orderValue = BigDecimal.valueOf(order.getOrderValue());
                totalOrderValueByAssetNo.put(assetNo,
                        totalOrderValueByAssetNo.getOrDefault(assetNo, BigDecimal.ZERO).add(orderValue));
                if (searchAssetRQ.getStatus() == 0) {
                    LocalDate transDate = order.getOrderDate();
                    transDateMap.put(assetNo, transDate);
                }
            }

            List<Asset> assetRs = investorAssetMapper.toDTOs(assets).stream().peek(asset -> {
                asset.setValue(asset.getValue().setScale(2, RoundingMode.HALF_UP));

                BigDecimal totalOrderValue = totalOrderValueByAssetNo.getOrDefault(asset.getAssetNo(), BigDecimal.ZERO);

                BigDecimal availableWithdrawBalance = asset.getValue().subtract(totalOrderValue);
                availableWithdrawBalance = availableWithdrawBalance.setScale(2, RoundingMode.HALF_UP);
                if (availableWithdrawBalance.compareTo(BigDecimal.ZERO) < 0) {
                    throw new ValidationException("Available withdraw balance is negative for asset: " + asset.getAssetNo());
                }
                asset.setAvailableWithdrawBalance(availableWithdrawBalance);

                LocalDate transDate = transDateMap.get(asset.getAssetNo());
                if (transDate != null) {
                    asset.setAccrualWithdrawalDate(transDate);
                }
            }).collect(Collectors.toList());

            List<Integer> uniqueProductIds = assetRs.stream()
                    .map(Asset::getProductId)
                    .distinct()
                    .collect(Collectors.toList());

            List<AssetInterestScheduleEntity> assetInterestSchedules = assetInterestScheduleRepository.findByProductIdIn(uniqueProductIds);

            Map<Integer, BigDecimal> receivedInterestMap = new HashMap<>();
            Map<Integer, BigDecimal> expectedInterestMap = new HashMap<>();

            for (AssetInterestScheduleEntity entity : assetInterestSchedules) {
                BigDecimal interestValue = BigDecimal.valueOf(entity.getInterestValue())
                        .subtract(BigDecimal.valueOf(entity.getFeeAmount()))
                        .setScale(2, RoundingMode.HALF_UP);

                if (entity.getStatus() == 1) {
                    receivedInterestMap.merge(entity.getProductId(), interestValue, BigDecimal::add);
                }

                expectedInterestMap.merge(entity.getProductId(), interestValue, BigDecimal::add);
            }

            for (Asset asset : assetRs) {
                BigDecimal receivedInterestValue = receivedInterestMap.getOrDefault(asset.getProductId(), BigDecimal.ZERO);
                BigDecimal expectedInterestValue = expectedInterestMap.getOrDefault(asset.getProductId(), BigDecimal.ZERO);
                BigDecimal unReceivedInterestValue = expectedInterestValue.subtract(receivedInterestValue);

                asset.setReceivedInterestValue(receivedInterestValue);
                asset.setExpectedInterestValue(expectedInterestValue);
                asset.setUnReceivedInterestValue(unReceivedInterestValue);
            }

            PaginationRS<Asset> paginationRS = new PaginationRS<>();
            paginationRS.setContent(assetRs);
            paginationRS.setPageNumber(assetPage.getNumber());
            paginationRS.setPageSize(assetPage.getSize());
            paginationRS.setTotalElements(assetPage.getTotalElements());
            paginationRS.setTotalPages(assetPage.getTotalPages());


            assetRS.setAssets(paginationRS);

            return assetRS;

        } catch (ValidationException e) {
            throw new ValidationException("Error in getting list of assets for investor ID: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching the list of assets.", e);
        }
    }

    @Override
    public AssetOrderInfo getAssetAndOrderInfo(String assetNo) {
        OrderEntity order = orderRepository.findByAssetNo(assetNo);
        AssetOrderInfo assetOrderInfo = new AssetOrderInfo();
        assetOrderInfo.setOrderValue(BigDecimal.valueOf(order.getOrderValue()).setScale(1, RoundingMode.HALF_UP));
        assetOrderInfo.setCreatedDate(LocalDate.from(order.getCreatedDate()));
        assetOrderInfo.setAssetNo(assetNo);
        assetOrderInfo.setVerifyDate(order.getVerifiedDate().toLocalDate());
        LocalDate maturityDate = assetInterestScheduleRepository.findLatestInterestDateByAssetNo(assetNo);
        assetOrderInfo.setMaturityDate(maturityDate);
        return assetOrderInfo;
    }

    @Override
    public AssetProduct getAssetAndProductInfo(Long investorId, List<Integer> productIds) {
        AppProductRS appProductRS = appClient.getListProduct(productIds, "id,type.title");

        List<Integer> asavingsIds = appProductRS.getData().stream()
                .filter(product -> "ASAVINGS".equals(product.getType().getTitle()))
                .map(AppProductType::getId)
                .collect(Collectors.toList());

        List<Integer> cashUpIds = appProductRS.getData().stream()
                .filter(product -> "CASH-UP".equals(product.getType().getTitle()))
                .map(AppProductType::getId)
                .collect(Collectors.toList());

        Float asavingAssetValue = investorAssetRepository.getTotalValueByProductIdsAndInvestor(investorId, asavingsIds);
        Float cashUpAssetValue = investorAssetRepository.getTotalValueByProductIdsAndInvestor(investorId, cashUpIds);
        asavingAssetValue = (asavingAssetValue != null) ? asavingAssetValue : 0.0f;
        cashUpAssetValue = (cashUpAssetValue != null) ? cashUpAssetValue : 0.0f;

        Float asavingWaitingOrderValue = orderRepository.getWaitingOrderValueByInvestor(investorId, asavingsIds);
        Float cashUpWaitingOrderValue = orderRepository.getWaitingOrderValueByInvestor(investorId, cashUpIds);
        asavingWaitingOrderValue = (asavingWaitingOrderValue != null) ? asavingWaitingOrderValue : 0.0f;
        cashUpWaitingOrderValue = (cashUpWaitingOrderValue != null) ? cashUpWaitingOrderValue : 0.0f;


        AssetProduct assetProduct = new AssetProduct();
        assetProduct.setAsavingValue(BigDecimal.valueOf(asavingAssetValue - asavingWaitingOrderValue).setScale(1, RoundingMode.HALF_UP));
        assetProduct.setCashUpValue(BigDecimal.valueOf(cashUpAssetValue - cashUpWaitingOrderValue).setScale(1, RoundingMode.HALF_UP));
        assetProduct.setWaitingValue(BigDecimal.valueOf(asavingWaitingOrderValue + cashUpWaitingOrderValue).setScale(1, RoundingMode.HALF_UP));

        return assetProduct;
    }

    @Override
    public AssetStats getAssetStats(Long investorId, Integer status) {
        Float totalAssetValue = investorAssetRepository.getTotalValueByInvestorAndStatus(investorId, status);
        if (totalAssetValue == null) {
            totalAssetValue = 0f;
        }
        Long numOfAsset = investorAssetRepository.countByInvestorAndStatus(investorId, status);
        AssetStats assetStats = new AssetStats();
        assetStats.setTotalAssetValue(BigDecimal.valueOf(totalAssetValue).setScale(1, RoundingMode.HALF_UP));
        assetStats.setNumOfAsset(numOfAsset);
        return assetStats;
    }


}
