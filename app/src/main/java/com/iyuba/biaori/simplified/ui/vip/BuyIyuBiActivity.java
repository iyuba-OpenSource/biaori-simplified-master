package com.iyuba.biaori.simplified.ui.vip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.databinding.ActivityBuyIyuBiBinding;

/**
 * 购买爱语币
 */
public class BuyIyuBiActivity extends AppCompatActivity implements View.OnClickListener {

    private String uname;

    private ActivityBuyIyuBiBinding activityBuyIyuBiBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBuyIyuBiBinding = ActivityBuyIyuBiBinding.inflate(getLayoutInflater());
        setContentView(activityBuyIyuBiBinding.getRoot());

        uname = getIntent().getStringExtra("uname");

        initOperation();
    }

    private void initOperation() {

        activityBuyIyuBiBinding.buyBi1.setOnClickListener(this);
        activityBuyIyuBiBinding.buyBi2.setOnClickListener(this);
        activityBuyIyuBiBinding.buyBi3.setOnClickListener(this);
        activityBuyIyuBiBinding.buyBi4.setOnClickListener(this);
        activityBuyIyuBiBinding.buyBi5.setOnClickListener(this);
        activityBuyIyuBiBinding.toolbar.toolbarIvBack.setOnClickListener(v -> finish());
        activityBuyIyuBiBinding.toolbar.toolbarIvTitle.setText("爱语币");
        activityBuyIyuBiBinding.toolbar.toolbarIvRight.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra("cate", "爱语币");
        intent.putExtra("uname", uname);
        if (id == R.id.bar_back) {
            onBackPressed();
        } else if (id == R.id.buy_bi1) {
            intent.putExtra("price", "19.9");
            intent.putExtra("month", 210);
            intent.putExtra("desc", "210爱语币");
            startActivity(intent);
            this.finish();
        } else if (id == R.id.buy_bi2) {
            intent.putExtra("price", "59.9");
            intent.putExtra("month", 650);
            intent.putExtra("desc", "650爱语币");
            startActivity(intent);
            this.finish();
        } else if (id == R.id.buy_bi3) {
            intent.putExtra("price", "99.9");
            intent.putExtra("month", 1100);
            intent.putExtra("desc", "1100爱语币");
            startActivity(intent);
            this.finish();
        } else if (id == R.id.buy_bi4) {
            intent.putExtra("price", "599");
            intent.putExtra("month", 6600);
            intent.putExtra("desc", "6600爱语币");
            startActivity(intent);
            this.finish();
        } else {
            intent.putExtra("price", "999");
            intent.putExtra("month", 12000);
            intent.putExtra("desc", "12000爱语币");
            startActivity(intent);
            this.finish();
        }
    }
}