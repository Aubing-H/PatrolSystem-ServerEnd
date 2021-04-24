package com.bingo.controller;

import com.bingo.mapper.LocationMapper;
import com.bingo.mapper.PatrolRecordMapper;
import com.bingo.mapper.UserMapper;
import com.bingo.pojo.PatrolRecord;
import com.bingo.pojo.User;
import com.bingo.utils.SyncData;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/patrolRecord")
public class PatrolRecordController {
    private static final String TAG = "## PatrolRecordController ## ";

    @Autowired
    PatrolRecordMapper mapper;

    @Autowired
    UserMapper userMapper;

    @PostMapping("/add")
    @ResponseBody
    public Object addPatrolRecord(@RequestBody Map<String, Object> rec) throws ParseException {
        String username = (String)rec.get("username");
        String locationId = (String)rec.get("locationId");
        String timeString = (String) rec.get("time");
        Timestamp time = SyncData.stringToTimestamp(timeString);
        int condition = (int)rec.get("condition");

        String msg = "";
        int state = SyncData.STATE_FAILED;

        if(mapper.queryOnePatrolRecord(username, locationId, time) == null){
            User user = userMapper.queryUserByName(username);
            if(user != null){
                PatrolRecord record = new PatrolRecord(user.getId(), locationId, time, condition);
                System.out.println(TAG + record);
                int res = mapper.addPatrolRecord(record);
                if(res == 1){
                    msg = "记录添加成功";
                    state = SyncData.STATE_OK;
                }else{
                    msg = "添加失败，返回值=" + res;
                }
            }else{
                msg = "该用户不在数据库中";
            }
        }else{
            msg = "该条记录已存在";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        map.put("state", state);
        return map;
    }

    @PostMapping("queryByInfo")
    @ResponseBody
    public Object queryByInfo(@RequestBody Map<String, Object> rec) throws Exception{
        Timestamp startTime=null, endTime=null;
        if(rec.get("startTime") != null && rec.get("endTime") != null){
            startTime = SyncData.stringToTimestamp(rec.get("startTime") + ":00");
            endTime = SyncData.stringToTimestamp(rec.get("endTime") + ":00");
        }
        String userId = (String)rec.get("userId");
        String locId = (String)rec.get("locId");
        List<PatrolRecord> patrolRecords = mapper.queryByInfo(startTime, endTime, userId, locId);
        Map<String, Object> map = new HashMap<>();
        if(patrolRecords != null){
            map.put("msg", "query successfully");
            map.put("state", SyncData.STATE_OK);
        }else{
            map.put("msg", "query failed");
            map.put("state", SyncData.STATE_FAILED);
        }
        map.put("data", patrolRecords);
        return map;
    }
}
