package com.iyuba.biaori.simplified.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.model.bean.BookListBean;

import java.util.List;

/**
 * 选择课本的适配器
 */
public class BookListAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {


    public BookListAdapter(int layoutResId, List<Book> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Book dataDTO) {

        //加载课本图片
        ImageView bl_iv_pic = baseViewHolder.getView(R.id.bl_iv_pic);
        Glide.with(mContext).load(Constant.BOOK_COVER_IMAGE + dataDTO.getImage()).into(bl_iv_pic);

        //书名
        baseViewHolder.setText(R.id.bl_iv_name, dataDTO.getBook());
    }
}
