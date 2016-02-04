package com.gcw7788.yanjuegong.contenttitlemergedemo.entity;


/**
 * Created by yanjuegong on 2016/1/28.
 */
public class FoodEntity {
    private int type; //类别
    private String title;//左侧标题名称
    private String foodName;//食物名称


    public void setTitle(String title) {
        this.title = title;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setType(int type){
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }
}
