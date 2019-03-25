package com.mmall;

/**
 * @Auther: HP
 * @Date: 2019/3/23 16:38
 * @Description:
 */
public class ThisDemo {
    private String name = "rui";

    public void print(String name){
        System.out.println(this.name);
        System.out.println(name);
        System.out.println("-------");
        System.out.println(this.name = name);
    }

    public static void main(String[] args) {
        ThisDemo thisDemo = new ThisDemo();
        thisDemo.print("abc");
    }
}
