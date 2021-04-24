package com.bingo.mapper;

import com.bingo.pojo.PatrolRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface PatrolRecordMapper {

    int addPatrolRecord(PatrolRecord patrolRecord);

    PatrolRecord queryOnePatrolRecord(@Param("username") String username,
                                      @Param("locationId") String locationId,
                                      @Param("time") Date time);
    List<PatrolRecord> queryByInfo(@Param("start") Timestamp start,
                                   @Param("end") Timestamp end,
                                   @Param("userId") String userId,
                                   @Param("locId") String locId);
}
