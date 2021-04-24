package com.bingo.mapper;

import com.bingo.pojo.TripRecord;
import com.sun.glass.ui.Timer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TripRecordMapper {

    int addTripRecord(TripRecord tripRecord);

    List<TripRecord> queryTripRecord();

    List<TripRecord> queryByInfo(@Param("start") Timestamp start,
                                 @Param("end") Timestamp end,
                                 @Param("id") String id);

    List<Map<String, Object>> queryByDay(@Param("start") Timestamp start,
                                         @Param("end") Timestamp end,
                                         @Param("userId") String userId);
    TripRecord queryFirstDay();
    TripRecord queryLastDay();
}
