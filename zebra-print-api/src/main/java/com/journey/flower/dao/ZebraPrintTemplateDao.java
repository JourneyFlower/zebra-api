package com.journey.flower.dao;

import com.journey.flower.core.model.ZebraPrintTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JourneyOfFlower
 * @version v1.0
 * @date 2022.09.23 15:19
 * @description ZB_TODO
 */
@Mapper
@Component("zebraPrintTemplateDao")
public interface ZebraPrintTemplateDao {
    int deleteByPrimaryKey(String id);

    int insert(ZebraPrintTemplate record);

    int insertSelective(ZebraPrintTemplate record);

    int updateByPrimaryKeySelective(ZebraPrintTemplate record);

    int updateByPrimaryKey(ZebraPrintTemplate record);

    List<ZebraPrintTemplate> getZPLTemplateList(ZebraPrintTemplate record);
}
