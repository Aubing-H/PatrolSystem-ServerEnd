package com.bingo.patrolsystem_v02;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

@SpringBootTest
class PatrolsystemV02ApplicationTests {

    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() throws Exception{
        System.out.println("##############");
        System.out.println(dataSource.getClass());
        System.out.println(dataSource.getConnection());
    }

}
