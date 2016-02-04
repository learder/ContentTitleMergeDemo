package com.gcw7788.yanjuegong.contenttitlemergedemo;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * 这是一个合并的管理器。
 * 提供了一个内容适配器的接口以及该类的接口
 * 主要做三件事情：
 *   第一件：将内容数据根据适配器的接口返回值来生成一个列表，以及数据的整合。
 *   第二件：监听content_listview的scrollview事件，来使title_listview自动滑动
 *   第三件：重写title_listview的onItemClick事件，并放出接口
 *
 * 值得注意的是，setData 这个方法里面的内容刷新很撇脚
 *
 * Created by yanjuegong on 2016/2/3.
 */
public class ContentTitleMerge {

    ListView contentListView;
    ListView titleListView;
    IContentAdapter contentAdapter;
    BaseAdapter titleAdapter;

    /**
     * 记录标题的位置与端的标记
     */
    private SparseIntArray positionOfSection;

    AbsListView.OnScrollListener mDelegateOnScrollListener;//无用

    IContentTitleMerge icontentTitleMerge;

    TitleObjectAndStateEntity lastTitleObjectAndState;//记录最后一次选中的，当再次被选中的时候设置回来，而不用再FOR

    public interface IContentAdapter extends ListAdapter {
        boolean isTitle(int position);
        Object getTitle(int position);
        List<? extends Object> getList();
        void setList(List<? extends Object> list);
    }

    public interface IContentTitleMerge{
        /**
         * 通过content的数据来获取出来的标题数据，请放到你的适配器里，已经帮你刷新了~ 别重复哈~
         * @param list
         */
        void titleListChange(List<TitleObjectAndStateEntity> list);

        /**
         * title_list 的OnItemClick点击事件，外面请不要重复set 否则会被覆盖
         * @param parent
         * @param view
         * @param position
         * @param titleObject 临时构建的title_listview的数据源
         */
        void titleOnItemClick(AdapterView<?> parent, View view, int position, Object titleObject);
    }

    /**
     *
     * @param contentListView 内容的listview
     * @param titleListView 标题的listview
     * @param icontentTitleMerge 接口
     */
    public ContentTitleMerge(ListView contentListView, ListView titleListView, IContentTitleMerge icontentTitleMerge) {
        this.contentListView=contentListView;
        this.titleListView=titleListView;
        this.icontentTitleMerge=icontentTitleMerge;
        init();
    }

    private void init() {
        if (contentListView==null){
            return;
        }
        if (titleListView==null){
            return;
        }
        if (contentListView.getAdapter()==null){
            new Throwable("adapter is empty");
            return;
        }
        if (contentListView.getAdapter() instanceof IContentAdapter){
            contentAdapter= (IContentAdapter) contentListView.getAdapter();
        }else {
            new Throwable("please let your adapter implements IContentAdapter");
            return;
        }
        if (titleListView.getAdapter()==null){
            return;
        }
        titleAdapter= (BaseAdapter) titleListView.getAdapter();
        //设置自己的监听
        titleListView.setOnItemClickListener(new MyOnItemClick());
        contentListView.setOnScrollListener(new MyScrollListener());
        setData(contentAdapter.getList());
    }

    /**
     * 当数据更换的时候，调用这个类
     * @param list
     */
    public void setData(List<? extends Object> list){
        List<TitleObjectAndStateEntity> titleList=getTitleList(list);
        if (icontentTitleMerge!=null){
            icontentTitleMerge.titleListChange(titleList);
        }
        titleAdapter.notifyDataSetChanged();
        //更换数据
        contentAdapter.setList(list);
        //获取适配器，并且刷新数据
        BaseAdapter baseAdapter= (BaseAdapter) contentListView.getAdapter();
        baseAdapter.notifyDataSetChanged();
    }

    /**
     * 获取标题的数据
     * @param list titleList
     * @return
     */
    private List<TitleObjectAndStateEntity> getTitleList(List<? extends Object> list){
        if (list==null||list.isEmpty()){
            return null;
        }
        List<TitleObjectAndStateEntity> tempList=new ArrayList<>();
        TitleObjectAndStateEntity titleObjectAndStateEntity;
        Object titleObject;
        if (positionOfSection==null){
            positionOfSection=new SparseIntArray();
        }else {
            positionOfSection.clear();
        }
        int key=0;
        for (int i=0;i<contentAdapter.getCount();i++){
            if (contentAdapter.isTitle(i)){
                titleObjectAndStateEntity=new TitleObjectAndStateEntity();
                titleObject=contentAdapter.getTitle(i);
                titleObjectAndStateEntity.setTitleObject(titleObject);
                if (i==0){//当第一个就是标题的时候，无法触发scroll导致未被选中。所以初始化的时候记录它
                    titleObjectAndStateEntity.setIsSelected(true);
                    lastTitleObjectAndState=titleObjectAndStateEntity;
                }else {
                    titleObjectAndStateEntity.setIsSelected(false);
                }
                //相互记录位置信息
                titleObjectAndStateEntity.setKeyOfPosition(i);
                tempList.add(titleObjectAndStateEntity);
                positionOfSection.append(i,key);
                key++;
            }else {
                positionOfSection.append(i,key-1);
            }
        }
        return tempList;
    }


    private class MyScrollListener implements AbsListView.OnScrollListener{
        int lastFirstVisibleItem=0;//记录最后一次刷新，防止无限刷新

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mDelegateOnScrollListener != null) { // delegate
                mDelegateOnScrollListener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mDelegateOnScrollListener != null) { // delegate
                mDelegateOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
            if (view.getAdapter().getCount()!=0){
                if (lastFirstVisibleItem==firstVisibleItem){
                    return;
                }
                lastFirstVisibleItem=firstVisibleItem;
                if (contentAdapter.isTitle(firstVisibleItem)){
                    scrollTitleItem(firstVisibleItem);
                }
            }
        }

        //跳转到对应的标题
        private void scrollTitleItem(int contentPosition){
            if (positionOfSection==null||positionOfSection.size()==0){
                return;
            }
            int vuale=positionOfSection.get(contentPosition,-1);
            if (vuale!=-1){
                TitleObjectAndStateEntity entity= (TitleObjectAndStateEntity) titleListView.getItemAtPosition(vuale);
                changeSelectedState(entity);
                titleListView.smoothScrollToPosition(vuale);
            }
        }
    }

    /**
     * 变更title_item的状态
     * @param entity
     */
    private void changeSelectedState(TitleObjectAndStateEntity entity){
        if (lastTitleObjectAndState!=null){
            lastTitleObjectAndState.setIsSelected(false);
        }
        entity.setIsSelected(true);
        lastTitleObjectAndState=entity;
        titleAdapter.notifyDataSetChanged();
    }

    private class MyOnItemClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TitleObjectAndStateEntity titleObject= (TitleObjectAndStateEntity) titleAdapter.getItem(position);
            //当用户点击 无法触发scroll的item的时候，使他状态变更
            changeSelectedState(titleObject);
            contentListView.setSelection(titleObject.getKeyOfPosition());
            if (icontentTitleMerge!=null){
                icontentTitleMerge.titleOnItemClick(parent,view,position,titleObject);
            }
        }
    }

    /**
     * title_adapter临时的实体，只不过放了更多的数据。getTitleObject获取的实体存储在 titleObject
     */
    public class TitleObjectAndStateEntity{
        /**
         * 接口getTitleObject获取的值
         */
        Object titleObject;
        /**
         * 是否被选中
         */
        boolean isSelected;
        /**
         * 记录对应title的位置
         */
        int keyOfPosition;

        public Object getTitleObject() {
            return titleObject;
        }

        public void setTitleObject(Object titleObject) {
            this.titleObject = titleObject;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public int getKeyOfPosition() {
            return keyOfPosition;
        }

        public void setKeyOfPosition(int keyOfPosition) {
            this.keyOfPosition = keyOfPosition;
        }
    }

}
