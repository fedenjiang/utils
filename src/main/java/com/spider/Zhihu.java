package com.spider;

import java.util.ArrayList;
public class Zhihu {
    public String question;// 问题
    public String zhihuUrl;// 网页链接
    public ArrayList<String> answers;// 存储所有回答的数组
    // 构造方法初始化数据
    public Zhihu() {
        question = "";
        zhihuUrl = "";
        answers = new ArrayList<String>();
    }
    @Override
    public String toString() {
        return "问题：" + question + "\n链接：" + zhihuUrl + "\n回答：" + answers + "\n";
    }
}