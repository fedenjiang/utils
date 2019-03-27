package com.test;

import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2019-3-26 0026.
 */
@Component //把类交给spring容器管理
@Order(value = 2)  //使用order属性，设置该类在spring容器中的加载顺序
@Scope("prototype")
public class Order1{
    private final int ORDERED = 1;
    private String name;
    private String id;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order1(){
        this.name = "tom";
        this.id = "123";
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Order1 [ORDERED=" + ORDERED + ",name=" + name + ",id=" + id + "]";
    }

}
