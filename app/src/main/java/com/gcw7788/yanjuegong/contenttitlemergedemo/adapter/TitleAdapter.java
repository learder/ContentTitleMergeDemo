package com.gcw7788.yanjuegong.contenttitlemergedemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcw7788.yanjuegong.contenttitlemergedemo.ContentTitleMerge;
import com.gcw7788.yanjuegong.contenttitlemergedemo.R;

import java.util.List;

/**
 * 左侧标题的适配器
 * 请用 TitleObjectAndStateEntity 来当实体！！！！
 *
 * Created by yanjuegong on 2016/2/4.
 */
public class TitleAdapter extends BaseAdapter {
    Context context;
    List<ContentTitleMerge.TitleObjectAndStateEntity> list;//必须用它来当实体，因为我不能很好的封装它

    public TitleAdapter(Context context) {
        this.context = context;
    }

    //刷新数据
    public void setMDatas(List<ContentTitleMerge.TitleObjectAndStateEntity> list){
        this.list=list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public ContentTitleMerge.TitleObjectAndStateEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=View.inflate(context, R.layout.title_layout_item,null);
            viewHolder.textView= (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ContentTitleMerge.TitleObjectAndStateEntity entity= getItem(position);
        //设置选中与不选中时的状态
        if (entity.isSelected()){
            viewHolder.textView.setBackgroundColor(0xff234567);
        }else {
            viewHolder.textView.setBackgroundColor(0xff888888);
        }
        //请转换成一开始的类型
        viewHolder.textView.setText((String)entity.getTitleObject());

        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}
