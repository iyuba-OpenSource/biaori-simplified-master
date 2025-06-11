package com.iyuba.biaori.simplified.ui.textbook;

import androidx.recyclerview.widget.GridLayoutManager;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.BookListAdapter;
import com.iyuba.biaori.simplified.databinding.ActivityBookListBinding;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.entity.BookEventBus;
import com.iyuba.biaori.simplified.model.bean.BookListBean;
import com.iyuba.biaori.simplified.presenter.BookListPresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.util.GridSpacingItemDecoration;
import com.iyuba.biaori.simplified.view.BookListContract;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 书籍列表
 */
public class BookListActivity extends BaseActivity<BookListContract.BookListView, BookListContract.BookListPresenter>
        implements BookListContract.BookListView {

    private ActivityBookListBinding activityBookListBinding;

    private BookListAdapter bookListAdapter;

    private View empty_view;

    private TextView empty_tv_content;

    private GridSpacingItemDecoration gridSpacingItemDecoration;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sp = getSharedPreferences(Constant.SP_BOOK, MODE_PRIVATE);

        initOperation();

        presenter.getJpBook(0, Constant.APPID, Build.BRAND);
    }

    private void initOperation() {

        empty_view = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
        empty_tv_content = empty_view.findViewById(R.id.empty_tv_content);
        empty_tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.getJpBook(0, Constant.APPID, Build.BRAND);
            }
        });

        activityBookListBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        activityBookListBinding.toolbar.toolbarIvTitle.setText("选择课本");


        gridSpacingItemDecoration = new GridSpacingItemDecoration(3, 20, true);
        activityBookListBinding.blRvList.addItemDecoration(gridSpacingItemDecoration);

        activityBookListBinding.blRvList.setLayoutManager(new GridLayoutManager(this, 3));
        bookListAdapter = new BookListAdapter(R.layout.item_book_list, new ArrayList<>());
        bookListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                Book dataDTO = bookListAdapter.getItem(i);
                sp.edit().putString(Constant.SP_KEY_BOOK_INFO, new Gson().toJson(dataDTO)).apply();

                Constant.bookDataDTO = dataDTO;
                //触发更新
                EventBus.getDefault().post(new BookEventBus());
                finish();
            }
        });
        activityBookListBinding.blRvList.setAdapter(bookListAdapter);
    }

    @Override
    public View initLayout() {
        activityBookListBinding = ActivityBookListBinding.inflate(getLayoutInflater());
        return activityBookListBinding.getRoot();
    }

    @Override
    public BookListContract.BookListPresenter initPresenter() {
        return new BookListPresenter();
    }

    @Override
    public void showLoading(String msg) {

        activityBookListBinding.blPbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

        activityBookListBinding.blPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getJpBookComplete(BookListBean bookListBean) {

        hideLoading();
        //添加emptyview
        if (bookListAdapter.getEmptyView() == null) {

            bookListAdapter.setEmptyView(empty_view);
        }

        if (bookListBean == null) {

            empty_tv_content.setText("请求超时，点击重试");
        } else {

            if (bookListBean.getData() == null || bookListBean.getData().size() == 0) {

                empty_tv_content.setText("没有数据了，点击重试");
            } else {

                //处理不需要的数据
                int i = 0;
                while (i < bookListBean.getData().size()) {

                    Book dataDTO = bookListBean.getData().get(i);
                    if (dataDTO.getShow() == 0) {

                        bookListBean.getData().remove(dataDTO);
                    } else {
                        i++;
                    }
                }
                bookListAdapter.setNewData(bookListBean.getData());
            }
        }
    }
}