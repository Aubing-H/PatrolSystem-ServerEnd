package com.bingo.controller;

import com.bingo.mapper.ConditionItemMapper;
import com.bingo.pojo.ConditionItem;
import com.bingo.utils.MyIdFactory;
import com.bingo.utils.SyncData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/conditionItem")
public class ConditionItemController {

    @Autowired
    ConditionItemMapper conditionItemMapper;

    @PostMapping("/add")
    @ResponseBody
    public Object addConditionItem(@RequestBody Map<String, Object> rec){
        String name = (String)rec.get("name");
        String msg = "该检查项目已存在";
        int state = SyncData.STATE_FAILED;
        if(conditionItemMapper.queryConditionItemByName(name) == null){
            String id = MyIdFactory.getId();
            int res = conditionItemMapper.addConditionItem(new ConditionItem(id, name));
            if(res == 1){
                msg = "新增成功";
                state = SyncData.STATE_OK;
            }else{
                msg = "新增失败，返回值=" + res;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        map.put("state", state);
        return map;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Object deleteConditionItem(@RequestBody Map<String, Object> rec){
        String id = (String)rec.get("id");
        String name = (String)rec.get("name");
        int res = conditionItemMapper.deleteConditionItem(id, name);
        Map<String, Object> map = new HashMap<>();
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
    public Object queryConditionItemList(){
        List<ConditionItem> items = conditionItemMapper.queryConditionItem();
        String msg = "";
        int state = SyncData.STATE_FAILED;
        if(items != null){
            msg = "查询检查条目成功";
            state = SyncData.STATE_OK;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        map.put("state", state);
        map.put("data", items);
        return map;
    }
}
