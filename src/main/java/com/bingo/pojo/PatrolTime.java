package com.bingo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;

@Data
@AllArgsConstructor
public class PatrolTime {
    private Time startTime;
    private Time endTime;
}
