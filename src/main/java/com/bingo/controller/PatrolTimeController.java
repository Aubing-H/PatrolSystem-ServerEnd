package com.bingo.controller;

import com.bingo.mapper.PatrolTimeMapper;
import com.bingo.pojo.PatrolTime;
import com.bingo.utils.SyncData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/patrolTime")
public class PatrolTimeController {
    @Autowired
    PatrolTimeMapper mapper;

    @PostMapping("/add")
    @ResponseBody
    public Object addPatrolTime(@RequestBody Map<String, Object> rec) throws Exception{
        HashMap<String, Object> map = new HashMap<>();
        map.put("state", SyncData.STATE_FAILED);
        System.out.println("##PatrolTime rec:" + rec);
        if(rec.get("startTime") != null && rec.get("endTime") != null){
            Time startTime = new Time(
                    SyncData.stringToDate("2021-01-01 " + rec.get("startTime") + ":00").getTime()
            );
            Time endTime = new Time(
                    SyncData.stringToDate("2021-01-01 " + rec.get("endTime") + ":00").getTime()
            );
            int res = mapper.addPatrolTime(new PatrolTime(startTime, endTime));
            if(res == 1){
                map.put("msg", "添加成功");
                map.put("state", SyncData.STATE_OK);
            }else{
                map.put("msg", "添加失败，返回值=" + res);
            }
        }else{
            map.put("msg", "发送内容为空");
        }
        return map;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Object deletePatrolTime(@RequestBody Map<String, Object> rec)
            throws Exception{
        Time startTime=null, endTime=null;
        if(rec.get("startTime") != null){
            startTime = new Time(
                    SyncData.stringToDate("2021-01-01 " +
                            rec.get("startTime") + ":00").getTime()
            );
        }
        if(rec.get("endTime") != null){
            endTime = new Time(
                    SyncData.stringToDate("2021-01-01 " +
                            rec.get("endTime") + ":00").getTime()
            );
        }
        int res = mapper.deletePatrolTime(new PatrolTime(startTime, endTime));
        HashMap<String, Object> map = new HashMap<>();
        if(res == 1){
            map.put("msg", "删除成功");
            map.put("state", SyncData.STATE_OK);
        }else{
            map.put("msg", "删除失败");
            map.put("state", SyncData.STATE_FAILED);
        }
        return map;
    }

    @RequestMapping("/queryList")
    public Object queryPatrolList(){
        List<PatrolTime> patrolTimes = mapper.queryPatrolTime();
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", patrolTimes);
        if(patrolTimes != null){
            map.put("msg", "查询成功");
            map.put("state", SyncData.STATE_OK);
        }else{
            map.put("msg", "查询失败");
            map.put("state", SyncData.STATE_FAILED);
        }
        return map;
    }
}
