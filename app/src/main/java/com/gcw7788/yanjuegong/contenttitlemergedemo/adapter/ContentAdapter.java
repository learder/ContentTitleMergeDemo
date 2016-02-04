package com.gcw7788.yanjuegong.contenttitlemergedemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcw7788.yanjuegong.contenttitlemergedemo.ContentTitleMerge;
import com.gcw7788.yanjuegong.contenttitlemergedemo.R;
import com.gcw7788.yanjuegong.contenttitlemergedemo.entity.FoodEntity;
import com.gcw7788.yanjuegong.contenttitlemergedemo.view.PinnedSectionListView;

import java.util.List;

/**
 *
 * 内容适配器，该适配器继承2个接口
 * 其中 pinnedSectionListAdapter是我使用的list来决定的，根据实际情况决定是否要继承
 * IContentAdapter 是必须继承的
 *
 * Created by yanjuegong on 2016/2/4.
 */
public class ContentAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter,
        ContentTitleMerge.IContentAdapter {
    List<FoodEntity> mDatas;
    Context context;

    public ContentAdapter(Context context, List<FoodEntity> mDatas) {
        this.mDatas=mDatas;
        this.context=context;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mDatas==null?0:mDatas.size();
    }

    //不要返回空。有调用到
    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView==null){
            holder=new Holder();
            switch (getItemViewType(position)){
                case 0:
                    convertView=View.inflate(context, R.layout.content_layout_item,null);
                    holder.textView= (TextView) convertView.findViewById(R.id.textView);
                    break;
                default:
                    convertView=View.inflate(context,R.layout.content_layout_item,null);
                    holder.textView= (TextView) convertView.findViewById(R.id.textView);
                    break;
            }
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        switch (getItemViewType(position)){
            case 0:
                holder.textView.setText(mDatas.get(position).getTitle());
                holder.textView.setBackgroundColor(0xff555555);
                break;
            default:
                holder.textView.setBackgroundColor(0xff777777);
                holder.textView.setText(mDatas.get(position).getFoodName());
                break;
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType()==0?0:1;
    }

    //PinnedSectionListAdapter  来自它的   可以不用管
    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType==0;
    }

    //返回是否为标题
    @Override
    public boolean isTitle(int position) {
        return mDatas.get(position).getType()==0;
    }

    //titleList的数据
    @Override
    public Object getTitle(int position) {
        return mDatas.get(position).getTitle();
    }

    //请返回当前的list
    @Override
    public List<? extends Object> getList() {
        return mDatas;
    }

    //数据刷新，请这样写
    @Override
    public void setList(List<? extends Object> list) {
        this.mDatas= (List<FoodEntity>) list;
    }

    class Holder {
        TextView textView;
    }
}
