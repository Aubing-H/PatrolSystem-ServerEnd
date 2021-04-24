package com.bingo.controller;

import com.bingo.mapper.AbnormalRecordMapper;
import com.bingo.mapper.UserMapper;
import com.bingo.pojo.AbnormalRecord;
import com.bingo.pojo.UploadRequest;
import com.bingo.pojo.User;
import com.bingo.utils.SyncData;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TSFBuilder;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jdk.internal.org.objectweb.asm.Handle;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.converter.json.GsonFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadPhotoController {
    private static final String TAG = "## UploadPhotoController ## ";

    private String filePath = "C:\\Users\\houki\\Pictures\\PatrolSystem\\";

    @Autowired
    UserMapper userMapper;

    @Autowired
    AbnormalRecordMapper mapper;

    @PostMapping(value = "/uploadPhoto", consumes = "multipart/*")
    @ResponseBody
    public Object uploadPhoto(@RequestParam("text") String text, @RequestParam("photo")
            MultipartFile multFile) throws IOException, ParseException {

        String msg = "";
        int state = SyncData.STATE_FAILED;
        // 去掉多余的‘\’符号和两边的‘"’符号
        text = text.replace("\\", "");
        text = text.substring(1, text.length() - 1);

        Map<String, Object> rec = new Gson().fromJson(text, new TypeToken<Map<String, Object>>(){
        }.getType());
        String username = (String)rec.get("username");
        User user = userMapper.queryUserByName(username);
        if(user == null){
            msg = "系统：该用户不在数据库中";
        }else{
            String userId = user.getId();
            String patrolLocId = (String)rec.get("patrolLocId");
            Timestamp time = SyncData.stringToTimestamp((String)rec.get("time"));
            String abnormalItem = (String)rec.get("abnormalItem");
            String abnormalDetail = (String)rec.get("abnormalDetail");

            File file = new File(filePath);
            if(!file.exists())
                file.mkdir();
            String photoPath = filePath + multFile.getOriginalFilename();
            multFile.transferTo(new File(photoPath));

            AbnormalRecord record = new AbnormalRecord(userId, patrolLocId,
                    time, abnormalItem, abnormalDetail, photoPath);
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
}
