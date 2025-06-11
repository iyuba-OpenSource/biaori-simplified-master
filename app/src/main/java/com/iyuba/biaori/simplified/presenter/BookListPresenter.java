package com.iyuba.biaori.simplified.presenter;

import com.iyuba.biaori.simplified.model.BookListModel;
import com.iyuba.biaori.simplified.model.bean.BookListBean;
import com.iyuba.biaori.simplified.view.BookListContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class BookListPresenter extends BasePresenter<BookListContract.BookListView, BookListContract.BookListModel>
        implements BookListContract.BookListPresenter {


    @Override
    protected BookListContract.BookListModel initModel() {
        return new BookListModel();
    }

    @Override
    public void getJpBook(int uid, int appid, String platform) {

        Disposable disposable = model.getJpBook(uid, appid, platform, new BookListContract.BookListCallback() {
            @Override
            public void success(BookListBean bookListBean) {

                if (bookListBean.getResult() == 200) {

                    view.getJpBookComplete(bookListBean);
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时!");
                }
            }
        });
        addSubscribe(disposable);
    }
}
