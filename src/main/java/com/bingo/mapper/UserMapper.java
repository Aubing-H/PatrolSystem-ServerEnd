package com.bingo.mapper;

import com.bingo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// 这个注解表示是一个Mybatis的Mapper类
@Mapper
@Repository
public interface UserMapper {

    User queryUserByName(@Param("name") String name);

    int addUser(User user);

    int updateUser(User user);

    List<User> queryUserList();

    List<User> queryPatrolList();
}
