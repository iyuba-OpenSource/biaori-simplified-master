package com.iyuba.biaori.simplified.model;

import com.iyuba.biaori.simplified.model.bean.BookListBean;
import com.iyuba.biaori.simplified.view.BookListContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BookListModel implements BookListContract.BookListModel {


    @Override
    public Disposable getJpBook(int uid, int appid, String platform, BookListContract.BookListCallback bookListCallback) {

        return NetWorkManager
                .getRequestForAI()
                .getJpBook(uid, appid, platform)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BookListBean>() {
                    @Override
                    public void accept(BookListBean bookListBean) throws Exception {

                        bookListCallback.success(bookListBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        bookListCallback.error((Exception) throwable);
                    }
                });
    }
}
