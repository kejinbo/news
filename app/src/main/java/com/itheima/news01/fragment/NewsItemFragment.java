package com.itheima.news01.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.yls.test.R;
import com.google.gson.Gson;
import com.itheima.news01.NewsDetailActivity;
import com.itheima.news01.adapter.NewsAdapter;
import com.itheima.news01.base.URLManager;
import com.itheima.news01.bean.NewsEntity;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 显示一个类别下的新闻
 *
 * @author WJQ
 */
public class NewsItemFragment extends BaseFragment {

    private TextView textView;
    private ListView listView;


    /** 新闻类别id */
    private String newsCategoryId;
    private NewsAdapter newAdapter;
    private SpringView springView;
    private View headerView;

    private List<NewsEntity.ResultBean> listDatas;

    /** 设置新闻类别id */
    public void setNewsCategoryId(String newsCategoryId) {
        this.newsCategoryId = newsCategoryId;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_news_item;
    }

    @Override
    public void initView() {
        textView = (TextView) mRootView.findViewById(R.id.tv_01);

        listView = (ListView) mRootView.findViewById(R.id.list_view);
        newAdapter = new NewsAdapter(null ,getContext());
        listView.setAdapter(newAdapter);

        //textView.setText("类别id：" + newsCategoryId);
        initSpringView();
    }

    private void initSpringView() {
        springView = (SpringView) mRootView.findViewById(R.id.spring_view);

        springView.setHeader(new MeituanHeader(getContext()));
        springView.setFooter(new DefaultFooter(getContext()));

        springView.setType(SpringView.Type.FOLLOW);

        // 设置监听器
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

                getNewsDatas(true);
            }

            @Override
            public void onLoadmore() {

                getNewsDatas(false);
            }
        });
    }

    @Override
    public void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                NewsEntity.ResultBean news = listDatas.get(position);

                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        getNewsDatas(true);
    }
    private int pageNo = 1;

    private void getNewsDatas(final boolean refresh) {
        if (refresh)
            pageNo = 1;
        String newsUrl = URLManager.getUrl(newsCategoryId,pageNo);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, newsUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                System.out.println("-----json:"+json);
                json = json.replace(newsCategoryId,"result");
                Gson gson = new Gson();
                NewsEntity newsEntity = gson.fromJson(json,NewsEntity.class);
                System.out.println("--------解析：" +
                        newsEntity.getResult().size() + " " +"条新闻");

                listDatas = newsEntity.getResult();

                if (refresh) {
                    showListView(listDatas);

                } else {
                    newAdapter.appendDatas(listDatas);
                }
                pageNo++;

                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                System.out.println("===msg"+msg);
            }
        });
    }
    private void showListView(List<NewsEntity.ResultBean> listDatas) {
        // （1）显示轮播图

        // 如果列表已经添加了头部布局，则先移除
        if (listView.getHeaderViewsCount() > 0) {
            listView.removeHeaderView(headerView);
        }

        // 第一条新闻
        NewsEntity.ResultBean firstNews = listDatas.get(0);
        // 有轮播图
        if (firstNews.getAds() != null && firstNews.getAds().size() > 0) {
            headerView = LayoutInflater.from(getContext()).inflate(R.layout
                    .list_view_header, listView, false);

            // 查找轮播图控件
            SliderLayout sliderLayout = (SliderLayout)
                    headerView.findViewById(R.id.slider_layout);
            // 准备轮播图要显示的数据
            List<NewsEntity.ResultBean.AdsBean> ads = firstNews.getAds();
            // 添加轮播图子界面
            for (int i = 0; i < ads.size(); i++) {
                NewsEntity.ResultBean.AdsBean bean = ads.get(i);

                // 一个TextSliderView表示一个子界面
                TextSliderView textSliderView = new TextSliderView(getContext());
                textSliderView.description(bean.getTitle())  // 显示标题
                        .image(bean.getImgsrc());      // 显示图片

                sliderLayout.addSlider(textSliderView);       // 添加一个子界面
            }

            // 添加到轮播图到列表的头部
            listView.addHeaderView(headerView);

        } else {
            // 没有轮播图的情况
        }

        newAdapter.setDatas(listDatas);
    }
    
}
