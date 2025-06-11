package com.iyuba.biaori.simplified.view;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.BookListBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;

import io.reactivex.disposables.Disposable;

public interface BookListContract {


    interface BookListView extends LoadingView {

        void getJpBookComplete(BookListBean bookListBean);
    }


    interface BookListPresenter extends IBasePresenter<BookListView> {

        void getJpBook(int uid, int appid, String platform);
    }


    interface BookListModel extends BaseModel {

        Disposable getJpBook(int uid, int appid, String platform, BookListCallback bookListCallback);
    }

    interface BookListCallback {

        void success(BookListBean bookListBean);

        void error(Exception e);

    }
}



