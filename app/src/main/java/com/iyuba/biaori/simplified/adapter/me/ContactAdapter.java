package com.iyuba.biaori.simplified.adapter.me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.entity.Contact;

import java.util.List;

public class ContactAdapter extends BaseQuickAdapter<Contact, BaseViewHolder> {

    public ContactAdapter(int layoutResId, @Nullable List<Contact> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Contact item) {

        helper.setText(R.id.contact_tv_title, item.getTitle() + ":");
        helper.setText(R.id.contact_tv_num, item.getNum());
    }
}
