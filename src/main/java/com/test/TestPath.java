package com.test;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2019-3-20 0020.
 */
public class TestPath {
    public static void main(String[] args) throws IOException {
        File file = new File("E:/p.txt");
        FileOutputStream out = new FileOutputStream(file);
        out.write("content".getBytes("UTF-8"));
        out.close();
//        out.flush(); // flush to operating system
//        out.getFD().sync(); // sync to disk（getFD()返回与该流所对应的文件描述符）
        assertThat(file.length(), is(((long) "content".length())));
    }
}
