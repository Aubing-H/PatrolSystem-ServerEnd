package com.bingo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PatrolRecord {
    String userId;
    String patrolLocId;
    Date time;
    int condition;
}
