package com.bingo.mapper;

import com.bingo.pojo.AbnormalRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AbnormalRecordMapper {

    int addAbnormalRecord(AbnormalRecord abnormalRecord);

    List<AbnormalRecord> queryByInfo(@Param("start")Timestamp start,
                                     @Param("end")Timestamp end,
                                     @Param("userId")String userId,
                                     @Param("locId") String locId);

    List<Map<String, Object>> locationAnalysis(@Param("start") Timestamp startTime,
                                         @Param("end") Timestamp endTime);
    Map<String, Object> otherLocation(@Param("start") Timestamp startTime,
                                      @Param("end") Timestamp endTime,
                                      @Param("locId") String locId);
}
