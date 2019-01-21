package com.rch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rch.R;
import com.rch.activity.FinancialSubmitActivity;
import com.rch.activity.FinancialTwoSubmitActivity;

import com.rch.base.BaseFragment;

/**
 * Created by Administrator on 2018/4/13.
 */

public class FinancialFragment extends BaseFragment implements View.OnClickListener {
    boolean isVisible;
    View view;

    TextView tvOneOk,tvTwoOk,tvThreeOk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null)
            view=inflater.inflate(R.layout.financial_fragment,container,false);
        init();
        return view;
    }


    private void init() {
        if(isVisible) {
            if(view==null)
                return;
            initControl();
        }
    }

    private void initControl() {
        tvOneOk= (TextView) view.findViewById(R.id.financial_fragment_one_ok);
        tvTwoOk= (TextView) view.findViewById(R.id.financial_fragment_two_ok);
        tvThreeOk= (TextView) view.findViewById(R.id.financial_fragment_three_ok);

        tvOneOk.setOnClickListener(this);
        tvTwoOk.setOnClickListener(this);
        tvThreeOk.setOnClickListener(this);
    }


    @Override
    protected void onVisible() {
        isVisible =true;
        init();
    }

    @Override
    protected void onInvisible() {
        isVisible =false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.financial_fragment_one_ok://二手
            case R.id.financial_fragment_three_ok://汽车
                String type=String.valueOf(v.getTag());
                startActivity(new Intent(getActivity(),FinancialTwoSubmitActivity.class).putExtra("type",type));
                break;
            case R.id.financial_fragment_two_ok:
                startActivity(new Intent(getActivity(),FinancialSubmitActivity.class));
                break;
        }
    }
}
