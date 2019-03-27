package com.utils;

import com.config.ESConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by kedaom on 2018/6/2.
 */
/*@Component
@Order(value = 1)*/
public class PrepareCommandLine implements CommandLineRunner {
    @Autowired
    private ESConfigFactory esConfigFactory;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("");
        EsUtil.init(esConfigFactory);
        EsUtil.testScript();

    }

}
