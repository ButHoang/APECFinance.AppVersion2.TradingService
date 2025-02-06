package com.apec_finance.trading.mapper;

import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleDetail;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleRS;
import com.apec_finance.trading.repository.AssetInterestScheduleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper
public interface AssetInterestScheduleMapper {
    @Mapping(target = "interestValue", source = "interestValue", qualifiedByName = "convertToBigDecimal")
    List<InterestScheduleDetail> getDetails(List<AssetInterestScheduleEntity> assetInterestScheduleEntities);

    @Named("convertToBigDecimal")
    default BigDecimal convertToBigDecimal(Float value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
