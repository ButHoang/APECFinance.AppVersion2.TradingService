package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.entity.InvestorAssetEntity;
import com.apec_finance.trading.entity.OrderEntity;
import com.apec_finance.trading.mapper.OrderMapper;
import com.apec_finance.trading.model.*;
import com.apec_finance.trading.model.order.*;
import com.apec_finance.trading.repository.AssetInterestScheduleRepository;
import com.apec_finance.trading.repository.InvestorAssetRepository;
import com.apec_finance.trading.repository.OrderRepository;
import com.apec_finance.trading.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final InvestorAssetRepository investorAssetRepository;
    private final AssetInterestScheduleRepository assetInterestScheduleRepository;
    private final CashClient cashClient;
    private final KeycloakService keycloakService;
    private final AppClient appClient;
    private final OrderNoCacheService orderNoCacheService;
    private final OrderMapper orderMapper;
    private final AssetNoCacheService assetNoCacheService;


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
    @Transactional
    public Order createDeposit(OrderDepositRQ rq) {
        if (!validateAmount(rq)) {
            log.error("Invalid amount in OrderDepositRQ");
            return null;
        }

        AppStockRS stockRS = appClient.getStockRs(rq.getProductId(), "*,product_int_detail.*,issuer_id.*");
        AppProduct appProduct = stockRS.getData().get(0);

        float orderValue = rq.getOrderQuantity() * appProduct.getParValue();
        Float parValue = (float) appProduct.getParValue();

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
                .setParValue(parValue)
                .setOrderQuantity(rq.getOrderQuantity())
                .setOrderPrice(parValue)
                .setOrderValue(orderValue)
                .setInterestRate(appProduct.getInterestRate())
                .setPaidAmount(orderValue)
                .setCreatedBy(keycloakService.getNameFromToken());

        orderRepository.save(orderEntity);

        UpdateCashBalance updateCashBalance = new UpdateCashBalance();
        updateCashBalance.setInvestorId(keycloakService.getInvestorIdFromToken());
        updateCashBalance.setPaidAmount(orderEntity.getOrderValue());
        updateCashBalance.setType("DEPOSIT");

        CreateCashTransaction createCashTransaction = new CreateCashTransaction();
        createCashTransaction.setOpr("-");
        createCashTransaction.setInvestorId(keycloakService.getInvestorIdFromToken());
        createCashTransaction.setTranDate(LocalDate.now());
        createCashTransaction.setTranTime(LocalDateTime.now());
        createCashTransaction.setRefNo(orderEntity.getOrderNo());
        createCashTransaction.setRefId(orderEntity.getId());
        createCashTransaction.setTranType("ORD");
        createCashTransaction.setStatus("P");
        createCashTransaction.setTranAmount(orderEntity.getOrderValue());
        createCashTransaction.setCreatedBy(keycloakService.getNameFromToken());


        try {
            cashClient.updateCashBalance("Bearer " + keycloakService.getToken(), updateCashBalance);
            cashClient.createCashTransaction("Bearer " + keycloakService.getToken(), createCashTransaction);
            log.info("Updated to cash service successfully");
        } catch (Exception e) {
            log.error("Failed to update cash service: " + e.getMessage());
        }

        if (checkVerifyTime()) verifyDepositOrder(orderEntity.getId());

        return orderMapper.getDTO(orderEntity);
    }

    @Transactional
    public Boolean verifyDepositOrder(Long orderId) {
        try {
            OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

            orderEntity.setOrderStatus(2)
                    .setVerifiedDate(OffsetDateTime.now())
                    .setVerifiedBy(keycloakService.getNameFromToken());

            orderRepository.save(orderEntity);

            VerifyCashTransaction verifyCashTransaction = new VerifyCashTransaction();
            verifyCashTransaction.setRefId(orderId);
            cashClient.verifyCashTransaction("Bearer " + keycloakService.getToken(), verifyCashTransaction);

            InvestorRS investorRS = appClient.getInvestorRs(keycloakService.getInvestorIdFromToken(), "*");
            InvestorInfo investorInfo = investorRS.getData().get(0);

            InvestorAssetEntity assetEntity = new InvestorAssetEntity();
            assetEntity.setInvestorId(keycloakService.getInvestorIdFromToken());
            assetEntity.setInvestorAccountNo(investorInfo.getAccountNo());
            assetEntity.setProductId(orderEntity.getProductId());
            assetEntity.setProductCode(orderEntity.getProductCode());
            assetEntity.setQuantity((int) (orderEntity.getOrderValue() / orderEntity.getParValue()));
            assetEntity.setValue(orderEntity.getOrderValue());
            assetEntity.setStatus(1);
            assetEntity.setAssetNo(assetNoCacheService.generateAssetNoForOrder());
            assetEntity.setCreatedBy(keycloakService.getNameFromToken());
            investorAssetRepository.save(assetEntity);

            AppStockRS stockRS = appClient.getStockRs(assetEntity.getProductId(), "*,product_int_detail.*,issuer_id.*");
            AppProduct appProduct = stockRS.getData().get(0);

            List<AssetInterestScheduleEntity> assetInterestScheduleEntities = createInterestSchedules(assetEntity, appProduct, investorInfo, LocalDate.now());
            assetInterestScheduleRepository.saveAll(assetInterestScheduleEntities);

            return true;

        } catch (Exception e) {
            log.error("Error verifying deposit order", e);
            return false;
        }
    }
    public Boolean checkVerifyTime() {
        List<TransactionRange> transactionRanges = appClient.getTransactionRange().getResult();

        LocalDateTime now = LocalDateTime.now();

        Optional<TransactionRange> frTimeRange = transactionRanges.stream()
                .filter(range -> "TRADING".equals(range.getGrName()) && "FRTIME".equals(range.getVarName()))
                .findFirst();

        Optional<TransactionRange> toTimeRange = transactionRanges.stream()
                .filter(range -> "TRADING".equals(range.getGrName()) && "TOTIME".equals(range.getVarName()))
                .findFirst();

        if (frTimeRange.isPresent() && toTimeRange.isPresent()) {
            LocalTime frTime = frTimeRange.get().getVarValue();
            LocalTime toTime = toTimeRange.get().getVarValue();

            return !now.toLocalTime().isBefore(frTime) && !now.toLocalTime().isAfter(toTime);
        } else {
            System.out.println("FRTIME or TOTIME is missing in the transaction range.");
        }
        return false;
    }

    public LocalDate calculateInterestDate(LocalDate startDate, int interestPeriod, int interestPeriodUnit) {
        LocalDate interestDate = startDate;

        // Tính toán ngày nhận lãi theo interest_period và interest_period_unit
        switch (interestPeriodUnit) {
            case 1: // Ngày
                interestDate = interestDate.plusDays(interestPeriod);
                break;
            case 2: // Tuần
                interestDate = interestDate.plusWeeks(interestPeriod);
                break;
            case 3: // Tháng
                interestDate = interestDate.plusMonths(interestPeriod);
                break;
            case 4: // Quý
                interestDate = interestDate.plusMonths(interestPeriod * 3L);
                break;
            case 5: // Năm
                interestDate = interestDate.plusYears(interestPeriod);
                break;
            default:
                throw new IllegalArgumentException("Invalid interest period unit");
        }

        // Điều chỉnh nếu interestDate là ngày 31, và tháng không có ngày 31 thì điều chỉnh về ngày cuối cùng của tháng
        if (interestDate.getDayOfMonth() == 31) {
            interestDate = interestDate.with(TemporalAdjusters.lastDayOfMonth());
        }

        // Trường hợp tháng 2 và ngày cuối tháng
        if (interestDate.getMonth() == Month.FEBRUARY && interestDate.getDayOfMonth() > 28) {
            interestDate = interestDate.with(TemporalAdjusters.lastDayOfMonth());
        }

        return interestDate;
    }

    public LocalDate calculateEndDate(LocalDate startDate, int period, int periodUnit) {
        LocalDate endDate = startDate;

        // Tính ngày kết thúc gói đầu tư dựa trên period và period_unit
        switch (periodUnit) {
            case 1: // Ngày
                endDate = endDate.plusDays(period);
                break;
            case 2: // Tuần
                endDate = endDate.plusWeeks(period);
                break;
            case 3: // Tháng
                endDate = endDate.plusMonths(period);
                break;
            case 4: // Quý
                endDate = endDate.plusMonths(period * 3L);
                break;
            case 5: // Năm
                endDate = endDate.plusYears(period);
                break;
            default:
                throw new IllegalArgumentException("Invalid period unit");
        }

        return endDate;
    }

    public List<AssetInterestScheduleEntity> createInterestSchedules(InvestorAssetEntity assetEntity, AppProduct appProduct, InvestorInfo investorInfo, LocalDate createdDate) {
        List<AssetInterestScheduleEntity> schedules = new ArrayList<>();

        // Tính ngày kết thúc gói đầu tư
        LocalDate endDate = calculateEndDate(createdDate, appProduct.getPeriod(), appProduct.getPeriodUnit());

        // Tính ngày nhận lãi
        LocalDate interestDate = createdDate;

        // Vòng lặp để tạo các bản ghi trả lãi
        while (interestDate.isBefore(endDate)) {
            // Tính toán interestDate theo interest_period và interest_period_unit
            interestDate = calculateInterestDate(interestDate, appProduct.getInterestPeriod(), appProduct.getInterestPeriodUnit());

            // Kiểm tra nếu interestDate đã vượt quá ngày kết thúc của gói đầu tư
            if (interestDate.isAfter(endDate)) {
                break;  // Dừng lại khi interestDate vượt quá ngày kết thúc
            }

            // Tạo bản ghi lãi
            AssetInterestScheduleEntity schedule = new AssetInterestScheduleEntity();
            schedule.setAssetId(assetEntity.getId());
            schedule.setAssetNo(assetEntity.getAssetNo());
            schedule.setInvestorId(investorInfo.getId());
            schedule.setInvestorAccountNo(investorInfo.getAccountNo());
            schedule.setProductId(assetEntity.getProductId());
            schedule.setProductCode(assetEntity.getProductCode());
            schedule.setQuantity(assetEntity.getQuantity());
            schedule.setValue(assetEntity.getValue());
            schedule.setStatus(0); // Chưa trả lãi
            schedule.setInterestRate(appProduct.getInterestRate());
            schedule.setFeeRate(0.05f); // fee_rate = 5%
            schedule.setInterestDate(interestDate);

            String createdByName = keycloakService.getNameFromToken();
            schedule.setCreatedBy(createdByName);

            // Tính toán lãi suất và phí cho bản ghi
            calculateInterestAndFee(appProduct, schedule, createdDate, interestDate);

            // Thêm bản ghi vào danh sách
            schedules.add(schedule);

            // Cập nhật createdDate cho lần lãi tiếp theo
            createdDate = interestDate;  // Cập nhật createdDate cho bản ghi sau
        }

        return schedules;
    }

    public void calculateInterestAndFee(AppProduct appProduct, AssetInterestScheduleEntity schedule, LocalDate createdDate, LocalDate interestDate) {
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(createdDate, interestDate);

        float interestValue = (daysBetween * appProduct.getInterestRate()) / appProduct.getBaseIntDays();
        schedule.setInterestValue(interestValue);

        float feeAmount = interestValue * schedule.getFeeRate();
        schedule.setFeeAmount(feeAmount);
    }


    @Override
    public Boolean checkOTP(String otp) {
        return true;
    }

    @Override
    public Map<Long, Integer> getListProductIdByOrderIds(Set<Long> orderIds) {
        List<OrderEntity> orderEntities = orderRepository.findOrderEntitiesByOrderIds(orderIds);

        Map<Long, Integer> rs = orderEntities.stream()
                .collect(Collectors.toMap(
                        OrderEntity::getId,
                        OrderEntity::getProductId,
                        (existing, replacement) -> existing
                ));

        return rs;
    }



    @Override
    @Transactional
    public Order createWithDraw(String assetNo, BigDecimal amount) {
        OrderEntity orderEntity = new OrderEntity();
        String accountNo = investorAssetRepository.findInvestorAccountNoByInvestorIdAndStatusAndDeleted(keycloakService.getInvestorIdFromToken(), 1, 0);
        InvestorAssetEntity assetEntity = investorAssetRepository.findByAssetNoAndStatusAndDeleted(assetNo, 1, 0);

        AppStockRS stockRS = appClient.getStockRs(assetEntity.getProductId(), "*,product_int_detail.*,issuer_id.*");
        AppProduct appProduct = stockRS.getData().get(0);

        Integer quantity = amount.divide(BigDecimal.valueOf(appProduct.getParValue()), RoundingMode.HALF_UP).intValue();

        orderEntity.setInvestorId(keycloakService.getInvestorIdFromToken())
                .setOrderNo(generateOrderNo())
                .setInvestorAccountNo(accountNo)
                .setOrderDate(LocalDate.now())
                .setOrderTime(LocalDateTime.now())
                .setOrderSide("S")
                .setOrderType(1)
                .setOrderStatus(getWithDrawStatus(amount, assetEntity.getValue()))
                .setProductId(assetEntity.getProductId())
                .setProductCode(assetEntity.getProductCode())
                .setParValue((float) appProduct.getParValue())
                .setOrderQuantity(quantity)
                .setOrderPrice((float) appProduct.getParValue())
                .setOrderValue(amount.floatValue())
                .setInterestRate(appProduct.getInterestRate())
                .setPaidAmount(amount.floatValue())
                .setCreatedBy(keycloakService.getNameFromToken());
        orderRepository.save(orderEntity);

//        UpdateCashBalance updateCashBalance = new UpdateCashBalance();
//        updateCashBalance.setInvestorId(keycloakService.getInvestorIdFromToken());
//        updateCashBalance.setPaidAmount(amount.floatValue());
//        updateCashBalance.setType("WITHDRAW");

        CreateCashTransaction createCashTransaction = new CreateCashTransaction();
        createCashTransaction.setOpr("+");
        createCashTransaction.setInvestorId(keycloakService.getInvestorIdFromToken());
        createCashTransaction.setTranDate(LocalDate.now());
        createCashTransaction.setTranTime(LocalDateTime.now());
        createCashTransaction.setRefNo(orderEntity.getOrderNo());
        createCashTransaction.setRefId(orderEntity.getId());
        createCashTransaction.setTranType("REV");
        createCashTransaction.setStatus("P");
        createCashTransaction.setTranAmount(amount.floatValue());
        createCashTransaction.setCreatedBy(keycloakService.getNameFromToken());


        try {
            cashClient.createCashTransaction("Bearer " + keycloakService.getToken(), createCashTransaction);
//            cashClient.updateCashBalance("Bearer " + keycloakService.getToken(), updateCashBalance);
            log.info("Updated to cash service successfully");
        } catch (Exception e) {
            log.error("Failed to update cash service: " + e.getMessage());
        }

        assetEntity.setQuantity(quantity);
        assetEntity.setValue(assetEntity.getValue() - amount.floatValue());
        investorAssetRepository.save(assetEntity);

//        List<AssetInterestScheduleEntity> assetInterestScheduleEntities = assetInterestScheduleRepository.findByAssetIdAndInterestDateGreaterThan(assetEntity.getId(), LocalDate.now());
//        assetInterestScheduleEntities.forEach(assetInterestScheduleEntity -> {
//            assetInterestScheduleEntity.setQuantity(quantity);
//            assetInterestScheduleEntity.setValue(assetEntity.getValue() - amount.floatValue());
//            float interestBeforeTax = assetEntity.getValue() * assetInterestScheduleEntity.getInterestRate() / appProduct.getBaseIntDays();
//            float feeAmount = interestBeforeTax * 5 / 100;
//            Float interestValue = interestBeforeTax - feeAmount;
//            assetInterestScheduleEntity.setInterestValue(interestValue);
//            assetInterestScheduleEntity.setFeeAmount(feeAmount);
//        });
//        assetInterestScheduleRepository.saveAll(assetInterestScheduleEntities);

        return orderMapper.getDTO(orderEntity);
    }

    private int getWithDrawStatus(BigDecimal amount, Float assetValue) {
        if (amount.compareTo(BigDecimal.valueOf(assetValue)) < 0) {
            return 3;
        } else {
            return 2;
        }
    }


    @Override
    public ExpectedInterest getExpectInterest(String assetNo, BigDecimal amount) {
        InvestorAssetEntity assetEntity = investorAssetRepository.findByAssetNoAndStatusAndDeleted(assetNo, 1, 0);
        LocalDate createdDate = assetEntity.getCreatedDate().toLocalDate();

        List<AssetInterestScheduleEntity> assetInterestScheduleEntities = assetInterestScheduleRepository.findByAssetNoAndStatusAndDeleted(assetNo, 0, 0);

        AppStockRS stockRS = appClient.getStockRs(assetEntity.getProductId(), "*,product_int_detail.*,issuer_id.*");
        AppProduct appProduct = stockRS.getData().get(0);

        ExpectedInterest expectedInterest = new ExpectedInterest();

        int baseIntDays = appProduct.getBaseIntDays();

        BigDecimal withdrawalInterestRate = calculateWithdrawalInterestRate(createdDate, stockRS.getData().get(0).getProductIntDetail(), baseIntDays);
        expectedInterest.setWithdrawalInterestRate(withdrawalInterestRate);

        BigDecimal totalInterestReceived = calculateTotalInterestReceived(assetInterestScheduleEntities);
        expectedInterest.setTotalInterestReceived(totalInterestReceived);

        BigDecimal incomeTaxPaid = calculateIncomeTaxPaid(assetInterestScheduleEntities);
        expectedInterest.setIncomeTaxPaid(incomeTaxPaid);

        long daysOfInterest = calculateInterestDays(createdDate);
        expectedInterest.setInterestDays(daysOfInterest);

        BigDecimal actualInterest = calculateActualInterest(amount, withdrawalInterestRate, daysOfInterest, baseIntDays);
        expectedInterest.setActualInterest(actualInterest);

        BigDecimal personalIncomeTax = calculatePersonalIncomeTax(actualInterest, totalInterestReceived, incomeTaxPaid);
        expectedInterest.setPersonalIncomeTax(personalIncomeTax);

        BigDecimal remainingInterest = calculateRemainingInterest(actualInterest, totalInterestReceived, personalIncomeTax);
        expectedInterest.setRemainingInterest(remainingInterest);

        BigDecimal actualReceived = amount.add(remainingInterest);
        expectedInterest.setActualReceived(actualReceived);

        return expectedInterest;
    }

    private BigDecimal calculateRemainingInterest(BigDecimal actualInterest, BigDecimal totalInterestReceived, BigDecimal personalIncomeTax) {
        if (personalIncomeTax.compareTo(BigDecimal.ZERO) > 0) {
            return actualInterest.subtract(totalInterestReceived).subtract(personalIncomeTax);
        } else {
            return actualInterest.subtract(totalInterestReceived);
        }
    }

    private BigDecimal calculatePersonalIncomeTax(BigDecimal actualInterest, BigDecimal totalInterestReceived, BigDecimal incomeTaxPaid) {
        BigDecimal taxableInterest = actualInterest.subtract(totalInterestReceived);
        BigDecimal personalIncomeTax = taxableInterest.multiply(BigDecimal.valueOf(0.05)).subtract(incomeTaxPaid);
        return personalIncomeTax.max(BigDecimal.ZERO);
    }

    private BigDecimal calculateActualInterest(BigDecimal amount, BigDecimal withdrawalInterestRate, long daysOfInterest, int baseIntDays) {
        if (withdrawalInterestRate == null || withdrawalInterestRate.compareTo(BigDecimal.ZERO) <= 0 || daysOfInterest <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal interestPerDay = withdrawalInterestRate.divide(BigDecimal.valueOf(baseIntDays), 10, RoundingMode.HALF_UP);
        return amount.multiply(interestPerDay).multiply(BigDecimal.valueOf(daysOfInterest));
    }

    private long calculateInterestDays(LocalDate createdDate) {
        LocalDate currentDate = LocalDate.now();
        return ChronoUnit.DAYS.between(createdDate, currentDate);
    }

    private BigDecimal calculateTotalInterestReceived(List<AssetInterestScheduleEntity> assetInterestScheduleEntities) {
        LocalDate currentDate = LocalDate.now();
        BigDecimal totalInterestReceived = BigDecimal.ZERO;

        for (AssetInterestScheduleEntity entity : assetInterestScheduleEntities) {
            if (entity.getInterestDate().isBefore(currentDate) || entity.getInterestDate().isEqual(currentDate)) {
                BigDecimal interestValue = BigDecimal.valueOf(entity.getInterestValue());
                BigDecimal feeAmount = BigDecimal.valueOf(entity.getFeeAmount());
                BigDecimal interestReceived = interestValue.subtract(feeAmount);

                totalInterestReceived = totalInterestReceived.add(interestReceived);
            }
        }

        return totalInterestReceived;
    }

    private BigDecimal calculateIncomeTaxPaid(List<AssetInterestScheduleEntity> assetInterestScheduleEntities) {
        LocalDate currentDate = LocalDate.now();
        BigDecimal incomeTaxPaid = BigDecimal.ZERO;

        for (AssetInterestScheduleEntity entity : assetInterestScheduleEntities) {
            if (entity.getInterestDate().isBefore(currentDate) || entity.getInterestDate().isEqual(currentDate)) {
                BigDecimal feeAmount = BigDecimal.valueOf(entity.getFeeAmount());
                incomeTaxPaid = incomeTaxPaid.add(feeAmount);
            }
        }

        return incomeTaxPaid;
    }

    private BigDecimal calculateWithdrawalInterestRate(LocalDate createdDate, List<AppStockProductDetail> productIntDetails, int baseIntDays) {
        LocalDate currentDate = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(createdDate, currentDate);

        for (AppStockProductDetail detail : productIntDetails) {
            long interestPeriodDays = calculateInterestPeriodInDays(detail.getInterestPeriod(), detail.getInterestPeriodUnit(), baseIntDays);

            if (daysBetween <= interestPeriodDays) {
                return new BigDecimal(detail.getInterestRate());
            }
        }

        return BigDecimal.ZERO;
    }

    private long calculateInterestPeriodInDays(int interestPeriod, int interestPeriodUnit, int baseIntDays) {
        switch (interestPeriodUnit) {
            case 1:
                return interestPeriod;
            case 2:
                return interestPeriod * 7L;
            case 3:
                return interestPeriod * 30L;
            case 4:
                return interestPeriod * 90L;
            case 5:
                return (long) interestPeriod * baseIntDays;
            default:
                return 0;
        }
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
