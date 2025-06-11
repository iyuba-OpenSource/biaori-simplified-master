package com.iyuba.biaori.simplified.util.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iyuba.biaori.simplified.R;

import razerdp.basepopup.BasePopupWindow;

public class DownloadPopup extends BasePopupWindow {

    private TextView download_tv_content;

    public DownloadPopup(Context context) {
        super(context);

        View view = createPopupById(R.layout.popup_download);
        setContentView(view);

        setOutSideDismiss(false);
        setBackPressEnable(false);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);

        download_tv_content = contentView.findViewById(R.id.download_tv_content);

    }

    public void setContent(String content) {

        download_tv_content.setText(content);
    }


}
