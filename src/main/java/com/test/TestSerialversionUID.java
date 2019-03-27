package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

public class TestSerialversionUID {


    public static void main(String[] args) throws Exception {
//        SerializeCustomer();// 序列化Customer对象
        Customer cc = (Customer) Class.forName("com.test.Customer").newInstance();
        Customer ccc = (Customer) Customer.class.getConstructor(String.class,int.class)
                .newInstance("haha",2);
        System.out.println(ccc.toString());
//        Customer customer = DeserializeCustomer();// 反序列Customer对象
//        System.out.println(customer);
    }

    /**
     * MethodName: SerializeCustomer
     * Description: 序列化Customer对象
     * @author xudp
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void SerializeCustomer() throws FileNotFoundException,
            IOException {
        Customer customer = new Customer("gacl",25);
        // ObjectOutputStream 对象输出流
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(
                new File("E:/Customer.txt")));
        oo.writeObject(customer);
        System.out.println("Customer对象序列化成功！");
        oo.close();
    }

    /**
     * MethodName: DeserializeCustomer
     * Description: 反序列Customer对象
     * @author xudp
     * @return
     * @throws Exception
     * @throws IOException
     */
    private static Customer DeserializeCustomer() throws Exception, IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                new File("E:/Customer.txt")));
        Customer customer = (Customer) ois.readObject();
        System.out.println("Customer对象反序列化成功！");
        return customer;
    }
}

