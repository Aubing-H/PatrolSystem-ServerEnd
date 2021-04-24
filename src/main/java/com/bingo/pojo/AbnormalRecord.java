package com.bingo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AbnormalRecord {
    String userId;
    String patrolLocId;
    Date time;
    String abnormalItem;
    String abnormalDetail;
    String pictureLink;
}
