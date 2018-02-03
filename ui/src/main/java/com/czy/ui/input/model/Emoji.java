package com.czy.ui.input.model;

/**
 * 作者：叶应是叶
 * 时间：2017/12/10 18:52
 * 说明：表情Bean
 */
public class Emoji {

    private String name;

    private int id;

    public Emoji(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
