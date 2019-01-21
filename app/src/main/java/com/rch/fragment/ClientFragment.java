package com.rch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rch.R;
import com.rch.activity.BookingorderAct;
import com.rch.activity.CustomerAct;
import com.rch.activity.LoginActivity;
import com.rch.base.BaseFragment;

/**
 * Created by Administrator on 2018/7/27.
 */

public class ClientFragment extends BaseFragment implements View.OnClickListener{
    boolean isVisible;
    View view;
    LinearLayout lListOne,lListTwo,lListThree;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null)
            view=inflater.inflate(R.layout.client_fragment,container,false);
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
        lListOne= (LinearLayout)view.findViewById(R.id.client_list_one);
        lListTwo= (LinearLayout) view.findViewById(R.id.client_list_two);
        lListThree= (LinearLayout) view.findViewById(R.id.client_list_three);

        //101-自有车商，1021-集团所属分销商，1022-非集团所属分销商，103-入驻车商
//        String state=getUserInfo().getUserResultState();
        String sCertifiedStatus = getUserInfo().getUserRoleType();
        String tate=getUserInfo().getUserRoleType();
//        if(state.equals("101")||state.equals("1021")||state.equals("1022")||state.equals("103")) {
//            lListOne.setVisibility(View.VISIBLE);
//        }

        if (!isLogin()||tate.equals("4")||tate.equals("8")||sCertifiedStatus.equals("201")||sCertifiedStatus.equals("109") || sCertifiedStatus.equals("204")||sCertifiedStatus.equals("108") || sCertifiedStatus.equals("202")){
            lListOne.setVisibility(View.GONE);
        }else {
            lListOne.setVisibility(View.VISIBLE);
        }


        lListOne.setOnClickListener(this);
        lListTwo.setOnClickListener(this);
        lListThree.setOnClickListener(this);
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
            case R.id.client_list_one:
                if(isLogin()) {
                    startActivity(new Intent(getActivity(), BookingorderAct.class));
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.client_list_two:
                break;
            case R.id.client_list_three:
                startActivity(new Intent(getActivity(), CustomerAct.class));
                break;
        }
    }
}
