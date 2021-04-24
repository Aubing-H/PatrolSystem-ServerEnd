package com.bingo.controller;

import com.bingo.mapper.UserMapper;
import com.bingo.pojo.User;
import com.bingo.utils.MyIdFactory;
import com.bingo.utils.SyncData;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    private static final String TAG = "## UserController ## ";

    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/login", consumes = "application/json")
    @ResponseBody
    public Object login(@RequestBody Map<String, Object> rec){
        System.out.println(TAG + "login rec: " + rec);
        String name = (String)rec.get("name");
        String password = (String)rec.get("password");
        int type = (int)rec.get("type");

        User user = userMapper.queryUserByName(name);

        String msg = "";
        int state = SyncData.STATE_FAILED;
        if(user == null){
            msg = "该用户不存在";
        }else {
            if (!user.getPassword().equals(password)) {
                msg = "输入密码错误";
            } else if (type != user.getType()) {
                if (user.getType() == SyncData.TYPE_ADMIN)
                    msg = "请以管理员方式登录";
                else
                    msg = "您不是管理员，无法以管理员方式登录";
            } else {
                msg = "登录成功";
                state = SyncData.STATE_OK;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("state", state);
        map.put("msg", msg);
        map.put("user", user);
        return map;
    }

    @PostMapping("/register")
    @ResponseBody
    public Object register(@RequestBody Map<String, Object> rec){
        System.out.println(TAG + "login rec: " + rec);
        String name = (String)rec.get("name");
        int type = (int)rec.get("type");

        String msg = "";
        int state = SyncData.STATE_FAILED;

        if(userMapper.queryUserByName(name) == null){
            String password = (String)rec.get("password");
            String id = MyIdFactory.getId();
            int res = userMapper.addUser(new User(id, name, password, type));
            msg = "注册成功，返回值=" + res;
            state = SyncData.STATE_OK;
        }else{
            msg = "该用户名已经存在";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("state", state);
        map.put("msg", msg);
        return map;
    }

    @PostMapping("/modify")
    @ResponseBody
    public Object update(@RequestBody Map<String, Object> rec){
        System.out.println(TAG + "login rec: " + rec);
        String name = (String)rec.get("name");
        String password = (String)rec.get("password");
        int res = userMapper.updateUser(new User("", name, password, -1));
        String msg = "";
        int state = SyncData.STATE_FAILED;
        if(res == 1){
            msg = "修改成功";
            state = SyncData.STATE_OK;
        }else{
            msg = "该用户不存在";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("state", state);
        map.put("msg", msg);
        return map;
    }

    @RequestMapping("/queryList")
    public Object queryUserList(){
        List<User> users = userMapper.queryUserList();
        Map<String, Object> map = new HashMap<>();
        if(users != null){
            map.put("state", SyncData.STATE_OK);
            map.put("msg", "查询成功");
        }else{
            map.put("state", SyncData.STATE_FAILED);
            map.put("msg", "查询失败");
        }
        map.put("data", users);
        return map;
    }

    @RequestMapping("/queryPatrolList")
    public Object queryPatrolList(){
        List<User> users = userMapper.queryPatrolList();
        Map<String, Object> map = new HashMap<>();
        if(users != null){
            map.put("state", SyncData.STATE_OK);
            map.put("msg", "查询成功");
        }else{
            map.put("state", SyncData.STATE_FAILED);
            map.put("msg", "查询失败");
        }
        map.put("data", users);
        return map;
    }
}
