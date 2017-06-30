package com.itheima.news01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yls.test.R;
import com.itheima.news01.bean.NewsEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yls on 2017/6/29.
 */

public class NewsAdapter extends BaseAdapter {
    private Context context;
    private List<NewsEntity.ResultBean> listDatas;
    public NewsAdapter(List<NewsEntity.ResultBean> listDatas, Context context) {
        this.listDatas = listDatas;
        this.context = context;
    }
    @Override
    public int getCount() {
        return (listDatas == null)? 0:listDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static final int ITEM_TYPE_1_IMAGE = 0;
    private static final int ITEM_TYPE_3_IMAGE = 1;

    @Override
    public int getItemViewType(int position) {
        NewsEntity.ResultBean news = (NewsEntity.ResultBean) getItem(position);
        if (news.getImgextra() != null && news.getImgextra().size()>0){
            return ITEM_TYPE_3_IMAGE;
        }else {
            return ITEM_TYPE_1_IMAGE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    public void setDatas(List<NewsEntity.ResultBean> listDatas) {
        this.listDatas = listDatas;
        notifyDataSetChanged();
    }
    public void appendDatas(List<NewsEntity.ResultBean> listDatas) {
        this.listDatas.addAll(listDatas);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type == ITEM_TYPE_1_IMAGE) {        // 显示1张图片的item
            // 1. 创建列表项item视图
            if (convertView == null) {  // 为空时才创建列表项，提高列表效率
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_news_1, parent,false);
            }

            // 2. 查找列表项中的子控件
            ImageView iv01 = (ImageView) convertView.findViewById(R.id.iv_01);
            TextView tvNewsTitle = (TextView) convertView.findViewById(R.id.tv_news_title);
            TextView tvNewsFrom = (TextView) convertView.findViewById(R.id.tv_news_from);
            TextView tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);

            // 3. 获取列表项对应的数据（javabean）
            NewsEntity.ResultBean news = (NewsEntity.ResultBean) getItem(position);

            // 4. 显示列表项中的子控件
            tvNewsTitle.setText(news.getTitle());           // 显示标题
            tvNewsFrom.setText(news.getSource());           // 新闻来源
            tvCommentCount.setText(news.getReplyCount() + "跟帖"); // 新闻来源
            // 显示新闻图片
            Picasso.with(context).load(news.getImgsrc()).into(iv01);
        } else {                                // 显示3张图片的item
            // 1. 创建列表项item视图
            if (convertView == null) {  // 为空时才创建列表项，提高列表效率
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_news_2, parent,false);
            }

            // 2. 查找列表项中的子控件
            ImageView iv01 = (ImageView) convertView.findViewById(R.id.iv_01);
            ImageView iv02 = (ImageView) convertView.findViewById(R.id.iv_02);
            ImageView iv03 = (ImageView) convertView.findViewById(R.id.iv_03);
            TextView tvNewsTitle = (TextView) convertView.findViewById(R.id.tv_news_title);
            TextView tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);

            // 3. 获取列表项对应的数据（javabean）
            NewsEntity.ResultBean news = (NewsEntity.ResultBean) getItem(position);

            // 4. 显示列表项中的子控件
            tvNewsTitle.setText(news.getTitle());           // 显示标题
            tvCommentCount.setText(news.getReplyCount() + "跟帖"); // 新闻来源
            // 显示3张新闻图片
            Picasso.with(context).load(news.getImgsrc()).into(iv01);
            Picasso.with(context).load(news.getImgextra().get(0).getImgsrc()).into(iv02);
            Picasso.with(context).load(news.getImgextra().get(1).getImgsrc()).into(iv03);
        }

        return convertView;
    }
}
