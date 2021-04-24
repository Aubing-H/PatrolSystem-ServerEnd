package com.bingo.controller;

import com.bingo.pojo.User;
import com.bingo.utils.SyncData;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
public class MainController {

    @RequestMapping({"/", "/index"})
    public Object hello(){
        Map<String, Object> map = new HashMap<>();
        map.put("state", SyncData.STATE_OK);
        map.put("msg", "this is main page");
        return map;
    }

    @PostMapping("/test")
    @ResponseBody
    public Object test(@RequestBody Map<String, Object> rec){
        System.out.println(rec.get("data"));
        Map<String, Object> map = new HashMap<>();
        map.put("state", SyncData.STATE_OK);
        map.put("msg", "test page");
        map.put("data", new User("id1", "name1", "12345", 1));
        return map;
    }

    @RequestMapping(value = "/image/{image_name}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("image_name") String image_name) throws Exception{

        byte[] imageContent ;
        String path = "C:\\Users\\houki\\Pictures\\PatrolSystem\\" + image_name;
        imageContent = fileToByte(new File(path));

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    public static byte[] fileToByte(File img) throws Exception {
        byte[] bytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BufferedImage bi;
            bi = ImageIO.read(img);
            ImageIO.write(bi, "jpg", baos);
            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            baos.close();
        }
        return bytes;
    }

    @RequestMapping("/**/*")
    public Object error(){
        Map<String, Object> map = new HashMap<>();
        map.put("state", SyncData.STATE_FAILED);
        map.put("msg", "can not find this path");
        return map;
    }
}
