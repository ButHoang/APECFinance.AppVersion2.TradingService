package com.apec_finance.trading.mapper;

import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleDetail;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleRS;
import com.apec_finance.trading.repository.AssetInterestScheduleRepository;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AssetInterestScheduleMapper {
    List<InterestScheduleDetail> getDetails(List<AssetInterestScheduleEntity> assetInterestScheduleEntities);
}
