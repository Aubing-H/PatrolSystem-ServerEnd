package com.bingo.mapper;

import com.bingo.pojo.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Mapper
@Repository
public interface LocationMapper {

    int addLocation(Location location);

    Location queryLocationById(@Param("id") String id);

    Location queryLocationByName(@Param("name") String name);

    List<Location> queryLocationList();

    int deleteLocation(@Param("id")String id, @Param("name") String name);
}
