package com.gcw7788.yanjuegong.contenttitlemergedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gcw7788.yanjuegong.contenttitlemergedemo.adapter.ContentAdapter;
import com.gcw7788.yanjuegong.contenttitlemergedemo.adapter.TitleAdapter;
import com.gcw7788.yanjuegong.contenttitlemergedemo.entity.FoodEntity;
import com.gcw7788.yanjuegong.contenttitlemergedemo.view.PinnedSectionListView;

import java.util.ArrayList;
import java.util.List;


/**
 * 这是一个类似食物标签列表的demo
 * 但是我并不能完美的封装，如果你有好主意，可以邮件给我
 * email: 644613693@qq.com
 */
public class MainActivity extends AppCompatActivity {

    ListView title_listview;
    PinnedSectionListView content_listview;//这是一个  类似微信 带title头的listview 你可以把它换成其他Listview
    ContentAdapter contentAdapter;
    TitleAdapter titleAdapter;
    ContentTitleMerge contentTitleMerge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title_listview= (ListView) findViewById(R.id.title_listview);
        content_listview= (PinnedSectionListView) findViewById(R.id.content_listview);
        contentAdapter=new ContentAdapter(this,null);
        content_listview.setAdapter(contentAdapter);
        titleAdapter=new TitleAdapter(this);
        title_listview.setAdapter(titleAdapter);
        //new之前，请设置对应的 adapter 因为 初始化依赖adapter
        contentTitleMerge=new ContentTitleMerge(content_listview, title_listview, new ContentTitleMerge.IContentTitleMerge() {
            @Override
            public void titleListChange(List<ContentTitleMerge.TitleObjectAndStateEntity> list) {
                //数据刷新的时候，会触发获得新的数据，请把该数据放到你适配器里
                titleAdapter.setMDatas(list);
            }

            @Override
            public void titleOnItemClick(AdapterView<?> parent, View view, int position, Object titleObject) {
                //onItem点击事件
            }
        });

        contentTitleMerge.setData(getList());

    }


    private List<FoodEntity> getList(){
        List<FoodEntity> list=new ArrayList<>();
        FoodEntity foodEntity;
        for (int i=0;i<500;i++){
            foodEntity=new FoodEntity();
            foodEntity.setFoodName("这是第" + i + "个食物");
            if (i<10){
                if (i==0){
                    foodEntity.setTitle("这是第" +0 + "组的标题");
                }
            }else {
                foodEntity.setTitle("这是第" + i / 10 + "组的标题");
            }
            foodEntity.setType(i%10);
            list.add(foodEntity);
        }
        return list;
    }

}
