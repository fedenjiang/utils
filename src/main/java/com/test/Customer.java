package com.test;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * <p>ClassName: Customer<p>
 * <p>Description: Customer实现了Serializable接口，可以被序列化<p>
 * @author xudp
 * @version 1.0 V
 * @createTime 2014-6-9 下午04:20:17
 */
class Customer implements Serializable,Cloneable{

    private static final long serialVersionUID = 1819622255693672404L;
    //Customer类中没有定义serialVersionUID
    private String name;
    private int age;
    private String sex;

    public Customer() {
        this.name = "test";
        this.age = 1;
    }

    public Customer(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Customer(String name, int age,String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    /*
     * @MethodName toString
     * @Description 重写Object类的toString()方法
     * @author xudp
     * @return string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "name=" + name + ", age=" + age;
    }

    public Customer clone() throws CloneNotSupportedException {
        return (Customer) super.clone();
    }

    static String testAnno(@NotNull String... json){

        return "123";
    }
    public static void main(String[] args) {
        System.out.println(testAnno(null));
        System.out.println("456");
    }
}