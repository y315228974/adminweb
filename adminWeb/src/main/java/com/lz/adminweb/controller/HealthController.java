package com.lz.adminweb.controller;

import com.lz.adminweb.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HealthController
 * @author yaoyanhua
 * @date 2020/4/27
 */
@RestController
@Slf4j
@RequestMapping("health")
public class HealthController {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 健康检查
     * @param
     * @return java.lang.Integer
     * @author yaoyanhua
     * @date 2020/6/23 11:52
     */
    @GetMapping("status")
    public Integer status() {
        return 2;
    }

//    @GetMapping("test")
//    public void test() {
//       redisUtil.set("nik", "123456");
//       commonService.setYaoPrizeList();
//        for(int i = 0; i < 100; i++) {
//            new Thread(() -> {
//                try {
//                    long id = Thread.currentThread().getId();
//                    for(int ii = 0; ii < 10000; ii++) {
//                        redisUtil.set("key" + id, id);
//                        Integer val = (Integer) redisUtil.get("key" + id);
//                        System.out.println("线程:" + id + ", " + val);
//                    }
//                    System.out.println("完成:" + id);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    log.error(e.getMessage(), e);
//                }
//            }).start();
//        }
//    }


}
