package com.bingo.mapper;

import com.bingo.pojo.PatrolTime;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PatrolTimeMapper {

    int addPatrolTime(PatrolTime patrolTime);

    int deletePatrolTime(PatrolTime patrolTime);

    List<PatrolTime> queryPatrolTime();

}
