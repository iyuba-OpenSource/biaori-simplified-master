package com.iyuba.biaori.simplified.util.popup;

import android.content.Context;
import android.view.View;

import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.databinding.PopupAnalysisBinding;
import com.iyuba.biaori.simplified.db.JpWord;

import razerdp.basepopup.BasePopupWindow;

public class AnalysisPopup extends BasePopupWindow {

    private PopupAnalysisBinding popupAnalysisBinding;

    public AnalysisPopup(Context context) {
        super(context);
        View view = createPopupById(R.layout.popup_analysis);
        popupAnalysisBinding = PopupAnalysisBinding.bind(view);
        setContentView(view);

        initOperation();
    }

    private void initOperation() {

        popupAnalysisBinding.analysisIvX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }


    public void setJpWord(JpWord word) {

        popupAnalysisBinding.analysisTvWord.setText(word.getWord());
        popupAnalysisBinding.analysisTvPron.setText(word.getPron());
        popupAnalysisBinding.analysisTvWordch.setText(word.getWordCh());
        popupAnalysisBinding.analysisTvSentence.setText(word.getSentence());
        popupAnalysisBinding.analysisTvSentencech.setText(word.getSentenceCh());
    }


}
