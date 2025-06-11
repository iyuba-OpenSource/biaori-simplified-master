package com.iyuba.biaori.simplified.util.popup;

import android.content.Context;
import android.view.View;

import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.databinding.PopupPermissonBinding;

import razerdp.basepopup.BasePopupWindow;

public class PermissionPopup extends BasePopupWindow {

    private PopupPermissonBinding popupPermissonBinding;

    private Callback callback;

    public PermissionPopup(Context context) {
        super(context);

        View view = createPopupById(R.layout.popup_permisson);
        popupPermissonBinding = PopupPermissonBinding.bind(view);
        setContentView(view);

        popupPermissonBinding.permissionButOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callback != null) {

                    callback.clickOk();
                }
            }
        });
    }


    public void setContent(String content) {

        popupPermissonBinding.permissionTvContents.setText(content);
    }


    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void clickOk();

    }
}
