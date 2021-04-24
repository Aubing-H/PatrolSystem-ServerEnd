package com.bingo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TripRecord {
    String userId;
    Date startTime;
    Date endTime;
    int totalLocNum;
    int patrolNum;
    int abnormalNum;
}
