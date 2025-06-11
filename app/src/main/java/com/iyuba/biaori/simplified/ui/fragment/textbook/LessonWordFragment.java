package com.iyuba.biaori.simplified.ui.fragment.textbook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.textbook.LessonWordAdapter;
import com.iyuba.biaori.simplified.databinding.FragmentLessonWordBinding;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordSentenceBean;
import com.iyuba.biaori.simplified.presenter.textbook.LessonWordPresenter;
import com.iyuba.biaori.simplified.ui.BaseFragment;
import com.iyuba.biaori.simplified.ui.textbook.WordDetailsActivity;
import com.iyuba.biaori.simplified.util.LineItemDecoration;
import com.iyuba.biaori.simplified.view.textbook.LessonWordContract;

import java.util.ArrayList;

/**
 * 课程评测详情页面-单词页面
 */
public class LessonWordFragment extends BaseFragment<LessonWordContract.LessonWordView, LessonWordContract.LessonWordPresenter>
        implements LessonWordContract.LessonWordView {

    private FragmentLessonWordBinding fragmentLessonWordBinding;

    /**
     * 课的source
     */
    private int level;

    /**
     * 课的id
     */
    private int sourceid;

    private LessonWordAdapter lessonWordAdapter;

    private View emptyView;

    private TextView empty_tv_content;

    private LineItemDecoration lineItemDecoration;

    public LessonWordFragment() {
        // Required empty public constructor
    }

    public static LessonWordFragment newInstance(int sourceid) {

        LessonWordFragment fragment = new LessonWordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("SOURCEID", sourceid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            sourceid = getArguments().getInt("SOURCEID");
        }

        level = Integer.parseInt(Constant.bookDataDTO.getLevel());

        lessonWordAdapter = new LessonWordAdapter(R.layout.item_lesson_word, new ArrayList<>());
        lessonWordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                WordDetailsActivity.startActivity(getActivity(), lessonWordAdapter.getData(), position);
            }
        });
        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.empty_view, null);
        empty_tv_content = emptyView.findViewById(R.id.empty_tv_content);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoading(null);
                presenter.getJpWordSentence(level, sourceid);
            }
        });

        lineItemDecoration = new LineItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(getContext().getDrawable(R.drawable.shape_line_word));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentLessonWordBinding.lwRv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        fragmentLessonWordBinding.lwRv.setAdapter(lessonWordAdapter);

        fragmentLessonWordBinding.lwRv.addItemDecoration(lineItemDecoration);

        showLoading(null);
        presenter.getJpWordSentence(level, sourceid);
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        fragmentLessonWordBinding = FragmentLessonWordBinding.inflate(inflater, container, false);
        return fragmentLessonWordBinding.getRoot();
    }

    @Override
    protected LessonWordContract.LessonWordPresenter initPresenter() {
        return new LessonWordPresenter();
    }

    @Override
    public void showLoading(String msg) {

        fragmentLessonWordBinding.lwPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

        fragmentLessonWordBinding.lwPb.setVisibility(View.GONE);
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getJpWordSentenceComplete(JpWordSentenceBean jpWordSentenceBean) {

        if (lessonWordAdapter.getEmptyViewCount() == 0) {

            lessonWordAdapter.setEmptyView(emptyView);
        }


        if (jpWordSentenceBean != null) {

            lessonWordAdapter.setNewData(jpWordSentenceBean.getData());
        } else {

            empty_tv_content.setText("请求超时，点击重试");
        }
    }

    /**
     * 切换音频调用
     * 切换数据，获取新的数据
     */
    public void switchData() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            sourceid = getArguments().getInt("SOURCEID");
        }
        if(presenter !=null){

            presenter.getJpWordSentence(level, sourceid);
        }
    }
}