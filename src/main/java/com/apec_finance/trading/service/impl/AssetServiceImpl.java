package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.comon.PaginationRS;
import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.entity.InvestorAssetEntity;
import com.apec_finance.trading.entity.OrderEntity;
import com.apec_finance.trading.exception.validate.ValidationException;
import com.apec_finance.trading.mapper.InvestorAssetMapper;
import com.apec_finance.trading.model.asset.Asset;
import com.apec_finance.trading.model.asset.AssetRS;
import com.apec_finance.trading.model.asset.SearchAssetRQ;
import com.apec_finance.trading.repository.AssetInterestScheduleRepository;
import com.apec_finance.trading.repository.InvestorAssetRepository;
import com.apec_finance.trading.repository.OrderRepository;
import com.apec_finance.trading.service.AssetNoCacheService;
import com.apec_finance.trading.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Override
    public AssetRS getListAsset(SearchAssetRQ searchAssetRQ, Long investorId) {
        try {
            if (investorId == null || investorId <= 0) {
                throw new ValidationException("Invalid investor ID");
            }

            Pageable pageable = Pageable.unpaged();
            if (searchAssetRQ != null) {
                pageable = PageRequest.of(searchAssetRQ.getPage(), searchAssetRQ.getSize());
            }

            List<Integer> productIds = searchAssetRQ != null ? searchAssetRQ.getProductIds() : null;

            Page<InvestorAssetEntity> assetPage = investorAssetRepository.findByInvestorIdAndProductIds(investorId, productIds, pageable);

            if (assetPage.isEmpty()) {
                throw new ValidationException("No assets found for investor ID: " + investorId);
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
            for (OrderEntity order : orders) {
                String assetNo = order.getAssetNo();
                BigDecimal orderValue = BigDecimal.valueOf(order.getOrderValue());
                totalOrderValueByAssetNo.put(assetNo,
                        totalOrderValueByAssetNo.getOrDefault(assetNo, BigDecimal.ZERO).add(orderValue));
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

            List<Integer> allProductIds = investorAssetRepository.getAllProductId();

            AssetRS assetRS = new AssetRS();
            assetRS.setAssets(paginationRS);
            assetRS.setProductIds(allProductIds);

            return assetRS;

        } catch (ValidationException e) {
            throw new ValidationException("Error in getting list of assets for investor ID: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching the list of assets.", e);
        }
    }



}
