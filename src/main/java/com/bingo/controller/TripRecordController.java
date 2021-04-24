package com.bingo.controller;

import com.bingo.mapper.TripRecordMapper;
import com.bingo.mapper.UserMapper;
import com.bingo.pojo.TripRecord;
import com.bingo.pojo.User;
import com.bingo.utils.SyncData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.crypto.HmacSha1Aes128CksumType;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/tripRecord")
public class TripRecordController {
    @Autowired
    TripRecordMapper mapper;

    @Autowired
    UserMapper userMapper;

    @PostMapping("/add")
    @ResponseBody
    public Object addTripRecord(@RequestBody Map<String, Object> rec)
            throws ParseException {
        String username = (String)rec.get("username");
        String msg = "";
        int state = SyncData.STATE_FAILED;
        User user = userMapper.queryUserByName(username);
        if(user == null){
            msg = "该用户不在数据库中";
        }else{
            String userId = user.getId();
            Timestamp startTime = SyncData.stringToTimestamp(
                    (String)rec.get("start_time"));
            Timestamp endTime = SyncData.stringToTimestamp(
                    (String)rec.get("end_time"));
            int totalLocNum = (int) rec.get("total_loc_num");
            int patrolNum = (int)rec.get("patrol_num");
            int abnormalNum = (int)rec.get("abnormal_num");
            TripRecord tripRecord = new TripRecord(userId, startTime,
                    endTime, totalLocNum, patrolNum, abnormalNum);
            int res = mapper.addTripRecord(tripRecord);
            if(res == 1){
                msg = "添加成功";
                state = SyncData.STATE_OK;
            }else{
                msg = "添加失败，返回值=" + res;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        map.put("state", state);
        return map;
    }

    @RequestMapping("/query")
    public Object queryTripRecord(){
        Map<String, Object> map = new HashMap<>();
        List<TripRecord> tripRecords = mapper.queryTripRecord();
        if(tripRecords != null){
            map.put("msg", "query successfully");
            map.put("state", SyncData.STATE_OK);
        }else{
            map.put("msg", "query failed");
            map.put("state", SyncData.STATE_FAILED);
        }
        map.put("data", tripRecords);
        return map;
    }

    @PostMapping("/queryByInfo")
    @ResponseBody
    public Object queryByInfo(@RequestBody Map<String, Object> rec) throws Exception{
        Timestamp startTime = null, endTime = null;
        if(rec.get("startTime") != null && rec.get("endTime") != null){
            startTime = SyncData.stringToTimestamp((String) rec.get("startTime") + ":00");
            endTime = SyncData.stringToTimestamp((String) rec.get("endTime") + ":00");
        }
        String id = (String)rec.get("patrol");
        List<TripRecord> tripRecords = mapper.queryByInfo(startTime, endTime, id);
        Map<String, Object> map = new HashMap<>();
        if(tripRecords != null){
            map.put("msg", "query successfully");
            map.put("state", SyncData.STATE_OK);
        }else{
            map.put("msg", "query failed");
            map.put("state", SyncData.STATE_FAILED);
        }
        map.put("data", tripRecords);
        return map;
    }

    @PostMapping("/dataAnalysis")
    @ResponseBody
    public Object dataAnalysis(@RequestBody Map<String, Object> rec) throws Exception{
        Timestamp startTime = null, endTime = null;
        if(rec.get("startDate") != null){
            startTime = SyncData.stringToTimestamp(rec.get("startDate") + " 00:00:00");
            endTime = SyncData.stringToTimestamp(rec.get("endDate") + " 23:59:59");
        }else{
            Date start = mapper.queryFirstDay().getStartTime();
            startTime = new Timestamp(start.getTime());
            Date end = mapper.queryLastDay().getEndTime();
            endTime = new Timestamp(end.getTime());
        }
        List<User> patrolList = userMapper.queryPatrolList();
        /* 数据分析格式 
         * {value: [{patrolName: [巡更点数, ...]}, ...],
         * x: [日期, ...]}
         * */
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<Map<String, Object>> abnormalMapList = new ArrayList<>();
        List<Map<String, Object>> rateMapList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Timestamp p1;
        for(Timestamp p = startTime; p.before(endTime); p = new Timestamp(p.getTime()
                + 1000 * 60 * 60 * 24)){
            dateList.add(SyncData.dateToString2(p));
        }
        map.put("date", dateList);
        // 饼图
        List<Map<String, Object>> pieList = new ArrayList<>();
        for (User user : patrolList) {
            List<Integer> patrolNumList = new ArrayList<>();
            List<Integer> abnormalList = new ArrayList<>();
            List<Float> patrolRateList = new ArrayList<>();
            // 饼图
            Map<String, Object> pieMap = new HashMap<>();
            int pieTotal = 0, pieActual = 0;
            for(Timestamp p = startTime; p.before(endTime); p = new Timestamp(p.getTime()
                    + 1000 * 60 * 60 * 24)){
                p1 = new Timestamp(p.getTime() + 1000 * 60 * 60 * 24);
                List<TripRecord> tripRecords = mapper.queryByInfo(p, p1, user.getId());

                if(tripRecords == null || tripRecords.size() == 0){
                    patrolNumList.add(0);
                    abnormalList.add(0);
                    patrolRateList.add(0.0f);
                }else{
                    //actual 实际巡更数 rate 完成率 abnormal 异常数
                    int actual = 0, total = 0, abnormal = 0;
                    for (TripRecord tripRecord : tripRecords) {
                        actual += tripRecord.getPatrolNum();
                        total += tripRecord.getTotalLocNum();
                        abnormal += tripRecord.getAbnormalNum();
                    }
                    //actual 实际巡更数 rate 完成率 abnormal 异常数
                    abnormalList.add(abnormal);
                    patrolNumList.add(actual);
                    if(total == 0)
                        patrolRateList.add(0.0f);
                    else
                        patrolRateList.add((float)actual / total);
                    // 饼图
                    pieActual += actual;
                    pieTotal += total;
                }
            }
            Map<String, Object> mapItem = new HashMap<>();
            mapItem.put(user.getName(), patrolNumList);
            mapList.add(mapItem);

            mapItem = new HashMap<>();
            mapItem.put(user.getName(), abnormalList);
            abnormalMapList.add(mapItem);

            mapItem = new HashMap<>();
            mapItem.put(user.getName(), patrolRateList);
            rateMapList.add(mapItem);
            // 饼图
            pieMap.put("value", pieTotal != 0 ? (float)pieActual / pieTotal : 0.0f);
            pieMap.put("name", user.getName());
            pieList.add(pieMap);
        }
        map.put("actual", mapList);
        map.put("abnormal", abnormalMapList);
        map.put("rate", rateMapList);
        // 饼图
        map.put("pie", pieList);
        map.put("state", SyncData.STATE_OK);
        map.put("msg", "查询成功");
        return map;
    }
}
