package com.bingo.utils;

import java.util.UUID;

public class MyIdFactory {

    public static String getId(){
        String[] split = UUID.randomUUID().toString().split("-");
        return split[0] + split[1] + split[3] + split[4];
    }
}
