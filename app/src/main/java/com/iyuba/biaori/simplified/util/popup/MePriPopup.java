package com.iyuba.biaori.simplified.util.popup;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.textbook.MoreAdapter;
import com.iyuba.biaori.simplified.databinding.PopupMoreBinding;
import com.iyuba.biaori.simplified.util.LineItemDecoration;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class MePriPopup extends BasePopupWindow {

    private PopupMoreBinding popupMoreBinding;

    private MoreAdapter moreAdapter;

    private Callback callback;

    public MePriPopup(Context context) {
        super(context);

        View view = createPopupById(R.layout.popup_me_pri);
        popupMoreBinding = PopupMoreBinding.bind(view);
        setContentView(popupMoreBinding.getRoot());
    }

    public void initOperation(List<String> strings) {

        LineItemDecoration lineItemDecoration = new LineItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(getContext().getDrawable(R.drawable.shape_line_me_pri));
        popupMoreBinding.moreRv.addItemDecoration(lineItemDecoration);
        popupMoreBinding.moreRv.setLayoutManager(new LinearLayoutManager(getContext()));
        moreAdapter = new MoreAdapter(R.layout.item_me_pri, strings);
        popupMoreBinding.moreRv.setAdapter(moreAdapter);
        moreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (callback != null) {

                    callback.getString(moreAdapter.getItem(position));
                }
            }
        });
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void getString(String s);
    }
}
