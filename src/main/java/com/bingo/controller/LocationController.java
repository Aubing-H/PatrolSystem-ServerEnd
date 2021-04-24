package com.bingo.controller;

import com.bingo.mapper.LocationMapper;
import com.bingo.pojo.Location;
import com.bingo.utils.SyncData;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/location")
public class LocationController {
    private static final String TAG = "## UserController ## ";

    @Autowired
    LocationMapper locationMapper;

    @PostMapping("/add")
    @ResponseBody
    public Object addLocation(@RequestBody Map<String, Object> rec){
        String id = (String)rec.get("id");
        String name = (String)rec.get("name");
        double longitude = (double)rec.get("longitude");
        double latitude = (double)rec.get("latitude");

        String msg = "";
        int state = SyncData.STATE_FAILED;

        if(locationMapper.queryLocationById(id) != null){
            msg = "该NFC卡已经被注册";
        }else if(locationMapper.queryLocationByName(name) != null){
            msg = "该地点名称已经被使用";
        }else{
            int res = locationMapper.addLocation(new Location(id, name, longitude, latitude));
            if(res == 1) {
                msg = "地点注册成功";
                state = SyncData.STATE_OK;
            }
            else
                msg = "地点注册失败，返回值=" + res;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        map.put("state", state);
        return map;
    }

    @PostMapping("/query")
    @ResponseBody
    public Object queryLocation(@RequestBody Map<String, Object> rec){
        String id = (String) rec.get("id");
        Location location = locationMapper.queryLocationById(id);
        String msg = "";
        int state = SyncData.STATE_FAILED;
        if(location == null){
            msg = "查询不到此卡";
        }else{
            msg = "查询成功";
            state = SyncData.STATE_OK;
        }
        Map<String, Object> map = new HashMap<>();
        List<Location> locations = new ArrayList<>();
        locations.add(location);
        map.put("msg", msg);
        map.put("state", state);
        map.put("location", locations);
        return map;
    }

    @RequestMapping("/queryList")
    public Object queryLocationList(){
        List<Location> locations = locationMapper.queryLocationList();
        String msg = "地点查询失败";
        int state = SyncData.STATE_FAILED;
        if(locations != null){
            msg = "地点查询成功";
            state = SyncData.STATE_OK;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        map.put("state", state);
        map.put("data", locations);
        return map;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Object deleteLocation(@RequestBody Map<String, Object> rec){
        String id = (String)rec.get("id");
        String name = (String)rec.get("name");
        int res = locationMapper.deleteLocation(id, name);
        Map<String, Object> map = new HashMap<>();
        if(res == 1){
            map.put("msg", "删除成功");
            map.put("state", SyncData.STATE_OK);
        }{
            map.put("msg", "删除失败");
            map.put("state", SyncData.STATE_FAILED);
        }
        return  map;
    }

}
