package com.bingo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    private String id;
    private String name;
    private double longitude;
    private double latitude;
}
