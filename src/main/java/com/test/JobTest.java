package com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2019-3-26 0026.
 */
@Component
public class JobTest {
    @Autowired
    Order1 order1;
    @Autowired
    Order1 order2;
    @Scheduled(cron = "${cron.jobtest}")
    public void jobTest(){
        System.out.println(System.currentTimeMillis());
        System.out.println("order1"+order1.toString());
        System.out.println("order2"+order2.toString());
        order1.setId("321");
        order1.setName("jack");
        System.out.println("order1"+order1.toString());
        System.out.println("order2"+order2.toString());
    }

}
