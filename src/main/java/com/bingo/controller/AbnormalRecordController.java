package com.bingo.controller;

import com.bingo.mapper.AbnormalRecordMapper;
import com.bingo.mapper.LocationMapper;
import com.bingo.mapper.UserMapper;
import com.bingo.pojo.AbnormalRecord;
import com.bingo.pojo.Location;
import com.bingo.pojo.User;
import com.bingo.utils.SyncData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/abnormalRecord")
public class AbnormalRecordController {
    @Autowired
    AbnormalRecordMapper mapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    LocationMapper locationMapper;

    @PostMapping("/add")
    @ResponseBody
    public Object addAbnormalRecord(@RequestBody Map<String, Object> rec) throws ParseException {
        String msg = "";
        int state = SyncData.STATE_FAILED;

        String username = (String)rec.get("username");
        User user = userMapper.queryUserByName(username);
        if(user == null){
            msg = "系统错误，该用户不存在数据库中";
        }else{
            String userId = user.getId();
            String patrolLocId = (String)rec.get("patrolLocId");
            Timestamp time = SyncData.stringToTimestamp(
                    (String)rec.get("time"));
            String abnormalItem = (String)rec.get("abnormalItem");
            String abnormalDetail = (String)rec.get("abnormalDetail");
            String pictureLink = (String)rec.get("pictureLink");
            AbnormalRecord record = new AbnormalRecord(userId, patrolLocId,
                    time, abnormalItem, abnormalDetail, pictureLink);
            int res = mapper.addAbnormalRecord(record);
            if(res == 1){
                msg = "数据添加成功";
                state = SyncData.STATE_OK;
            }else{
                msg = "数据添加失败，返回值=" + res;
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        map.put("state", state);
        return map;
    }

    @PostMapping("/queryByInfo")
    @ResponseBody
    public Object queryByInfo(@RequestBody Map<String, Object> rec) throws Exception{
        Timestamp startTime=null, endTime=null;
        if(rec.get("startTime") != null && rec.get("endTime") != null){
            startTime = SyncData.stringToTimestamp(rec.get("startTime") + ":00");
            endTime = SyncData.stringToTimestamp(rec.get("endTime") + ":00");
        }
        String userId = (String)rec.get("userId");
        String locId = (String)rec.get("locId");
        Map<String, Object> map = new HashMap<>();
        List<AbnormalRecord> abnormalRecords = mapper.queryByInfo(startTime, endTime, userId, locId);
        if(abnormalRecords != null){
            map.put("msg", "query successfully");
            map.put("state", SyncData.STATE_OK);
        }else{
            map.put("msg", "query failed");
            map.put("state", SyncData.STATE_FAILED);
        }
        map.put("data", abnormalRecords);
        return map;
    }

    @PostMapping("/locationAnalysis")
    @ResponseBody
    public Object locationAnalysis(@RequestBody Map<String, Object> rec) throws Exception{
        Timestamp startTime = null, endTime = null;
        if(rec.get("startDate") != null){
            startTime = SyncData.stringToTimestamp(rec.get("startDate") + " 00:00:00");
            endTime = SyncData.stringToTimestamp(rec.get("endDate") + " 23:59:59");
        }
        List<Location> locations = locationMapper.queryLocationList();
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> other = mapper.otherLocation(startTime, endTime, "00000000");
        if(other == null){
            other = new HashMap<>();
            other.put("number", 0);
        }
        other.put("name", "其他");
        mapList.add(other);
        for (Location location : locations) {
            Map<String, Object> temp = mapper.otherLocation(startTime, endTime, location.getId());
            if(temp == null){
                temp = new HashMap<>();
                temp.put("number", 0);
            }
            temp.put("name", location.getName());
            mapList.add(temp);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("data", mapList);
        map.put("msg", "查询成功");
        map.put("state", SyncData.STATE_OK);
        return map;
    }
}
