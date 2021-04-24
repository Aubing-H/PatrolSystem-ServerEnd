package com.bingo.mapper;

import com.bingo.pojo.ConditionItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ConditionItemMapper {

    int addConditionItem(ConditionItem conditionItem);

    List<ConditionItem> queryConditionItem();

    ConditionItem queryConditionItemByName(@Param("name") String name);

    int deleteConditionItem(@Param("id") String id, @Param("name") String name);
}
