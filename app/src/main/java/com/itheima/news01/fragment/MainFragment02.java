package com.itheima.news01.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.yls.test.R;
import com.google.gson.Gson;
import com.itheima.news01.adapter.VideoAdapter;
import com.itheima.news01.base.URLManager;
import com.itheima.news01.bean.VideoEntity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * @author WJQ
 */
public class MainFragment02 extends BaseFragment {

    private RecyclerView recyclerView;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_02;
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        getVideoDatas();
    }

    private void getVideoDatas() {
        new HttpUtils().send(HttpRequest.HttpMethod.GET, URLManager.VideoURL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                System.out.println("-----视频数据：" + json);
                json = json.replace("V9LG4B3A0","result");
                Gson gson = new Gson();
                VideoEntity entity = gson.fromJson(json,VideoEntity.class);
                List<VideoEntity.ResultBean> listDatas = entity.getResult();
                showRecyclerView(listDatas);
            }


            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
            }
        });
    }

    private void showRecyclerView(List<VideoEntity.ResultBean> listDatas) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        VideoAdapter videoAdapter = new VideoAdapter(getContext(), listDatas);
        recyclerView.setAdapter(videoAdapter);
    }
}
