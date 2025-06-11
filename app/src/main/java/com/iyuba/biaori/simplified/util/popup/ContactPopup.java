package com.iyuba.biaori.simplified.util.popup;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.me.ContactAdapter;
import com.iyuba.biaori.simplified.databinding.PopupContactBinding;
import com.iyuba.biaori.simplified.entity.Contact;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class ContactPopup extends BasePopupWindow {

    private PopupContactBinding binding;
    private ContactAdapter contactAdapter;

    private Callback callback;

    public ContactPopup(Context context) {
        super(context);
        View view = createPopupById(R.layout.popup_contact);
        binding = PopupContactBinding.bind(view);
        setContentView(view);

        contactAdapter = new ContactAdapter(R.layout.item_contact, new ArrayList<>());
        binding.contactRvList.setAdapter(contactAdapter);
        binding.contactRvList.setLayoutManager(new LinearLayoutManager(getContext()));
        contactAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (callback != null) {

                    callback.click(contactAdapter.getItem(position), position);
                }
            }
        });
        contactAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                Contact contact = contactAdapter.getItem(position);
                ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", contact.getNum());
                manager.setPrimaryClip(mClipData);
                Toast.makeText(MyApplication.getContext(), "已将内容复制到剪贴板", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        binding.contactTvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }

    public void setData(List<Contact> contactList) {

        contactAdapter.setNewData(contactList);
    }


    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void click(Contact contact, int position);
    }
}
