package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.TimePareUtil;
import com.rch.common.ToastTool;
import com.rch.entity.Children;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.io.Serializable;
import java.util.Date;

/**
 * 写跟进
 * Created by Administrator on 2018/10/10.
 */

public class FollowupActivity extends SecondBaseActivity {

    @ViewInject(R.id.et_customer_level)
    EditText et_customer_level;
    @ViewInject(R.id.et_followup_approach)
    EditText et_followup_approach;
    @ViewInject(R.id.et_followup_results)
    EditText et_followup_results;
    @ViewInject(R.id.et_next_visit_time)
    EditText et_next_visit_time;
    @ViewInject(R.id.et_content)
    EditText et_content;
    private String userId;
    private String enterpriseId;//车商id
    private String str_customer_level,str_followup_approach,str_followup_results,str_next_visit_time,str_content;
    private String str_customer_level_key,str_followup_approach_key,str_followup_results_key;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_follow_up);
    }

    @Override
    public void init(Bundle savedInstanceState) {

        if (getIntent()!=null) {
            enterpriseId = getIntent().getStringExtra("enterpriseId");
            userId = getIntent().getStringExtra("userId");
        }
    }

    //写跟进
    public void writeFollow(String userId,String customerLevel,String followResult,String followWay,String nextDate,String notes) {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken() == null ? "" : getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken() == null ? "" : getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        if (!TextUtils.isEmpty(userId))
            param.add("userId", userId);
        if (!TextUtils.isEmpty(customerLevel))
            param.add("customerLevel", customerLevel);
        if (!TextUtils.isEmpty(followResult))
            param.add("followResult", followResult);
        if (!TextUtils.isEmpty(followWay))
            param.add("followWay", followWay);
        if (!TextUtils.isEmpty(nextDate))
            param.add("nextDate", nextDate);
        if (!TextUtils.isEmpty(notes))
            param.add("notes", notes);
        if (!TextUtils.isEmpty(enterpriseId))
            param.add("enterpriseId",enterpriseId);
        OkHttpUtils.post(Config.WRITE_FOLLOW, param, new OkHttpCallBack(this, "加载中...") {

            @Override
            public void onSuccess(String data) {
                //更新客户详情数据
                sendBroadcast(new Intent(CustomerDetailActivity.CUSTOMER_DETAIL));
                finish();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(mContext, error);
            }
        });
    }
    final int LEVEL=0001;
    final int WAY=0002;
    final int RESULT=0003;
    @OnClick({R.id.tv_cancle,R.id.tv_save,R.id.rl_customer_level,R.id.rl_followup_approach,R.id.rl_followup_results,R.id.rl_next_visit_time})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_cancle:
                finish();
                break;

            case R.id.tv_save:
                if (isAvailable())
                    writeFollow(userId,str_customer_level_key,str_followup_results_key,str_followup_approach_key,str_next_visit_time,str_content);
                break;
            case R.id.rl_customer_level:
                Intent intent1=new Intent(mContext,SelectAct.class).putExtra("from","8");
                startActivityForResult(intent1,LEVEL);
                break;

            case R.id.rl_followup_approach:
                Intent intent2=new Intent(mContext,SelectAct.class).putExtra("from","9");
                startActivityForResult(intent2,WAY);
                break;
            case R.id.rl_followup_results:
                Intent intent3=new Intent(mContext,SelectAct.class).putExtra("from","10");
                startActivityForResult(intent3,RESULT);
                break;

            case R.id.rl_next_visit_time:
                showDateDialog();
                break;
        }
    }
    private String time="";
    private Date startDate;
    private void showDateDialog() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                int days= (int) TimePareUtil.getDayDiff(TimePareUtil.getCurrentDate("yyyy-MM-dd"),date);
                if(date.before(TimePareUtil.getCurrentDate("yyyy-MM-dd HH:mm"))||days>30){
                    ToastTool.show(mContext, "请重新选择");
//                    showDateDialog();//再默认
                    time="";
                }else {
                    time = TimePareUtil.getTimeForDate("yyyy-MM-dd HH:mm", date);
                    et_next_visit_time.setText(time);
//                Toast.makeText(PreOrderActivity.this, time, Toast.LENGTH_SHORT).show();
                    startDate = TimePareUtil.getDateTimeForTime("yyyy-MM-dd HH:mm", time);
                }
            }
        }).setType(new boolean[]{true, true, true, true, false, false})
                .isDialog(true)
                .isCenterLabel(false)//年月日不显示在中间
                .isDialog(false)
                .setTitleColor(getResources().getColor(R.color.white))
                .setLineSpacingMultiplier(2.0f)
                .gravity(Gravity.CENTER)
                .setDate(TimePareUtil.getCalendarForDate(TimePareUtil.getCurrentDate("yyyy-MM-dd HH")))
                .build();

        pvTime.setKeyBackCancelable(false);
        if(startDate!=null) {
            pvTime.setDate(TimePareUtil.getCalendarForDate(startDate));
        }
        pvTime.show();


    }
    private boolean isAvailable(){
        str_customer_level = et_customer_level.getText().toString().trim();
        str_followup_approach = et_followup_approach.getText().toString().trim();
        str_followup_results = et_followup_results.getText().toString().trim();
        str_next_visit_time = et_next_visit_time.getText().toString().trim();
        str_content = et_content.getText().toString().trim();

        if (TextUtils.isEmpty(str_customer_level)){
            ToastTool.show(mContext,"请选择客户级别");
            return false;
        }
        if (TextUtils.isEmpty(str_followup_approach)){
            ToastTool.show(mContext,"请选择跟进方式");
            return false;
        }
        if (TextUtils.isEmpty(str_followup_results)){
            ToastTool.show(mContext,"请选择跟进结果");
            return false;
        }
//        if (TextUtils.isEmpty(str_next_visit_time)){
//            ToastTool.show(mContext,"请选择下次回访时间");
//            return false;
//        }
//        if (TextUtils.isEmpty(str_content)){
//            ToastTool.show(mContext,"请输入跟进内容");
//            return false;
//        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null)
            return;
            switch (requestCode) {
                case LEVEL:
                    Children level = (Children) data.getExtras().getSerializable("select");
                    et_customer_level.setText(level.getValue());
                    str_customer_level_key = level.getKey();
                    break;
                case WAY:
                    Children way = (Children) data.getExtras().getSerializable("select");
                    et_followup_approach.setText(way.getValue());
                    str_followup_approach_key = way.getKey();
                    break;
                case RESULT:
                    Children result = (Children) data.getExtras().getSerializable("select");
                    et_followup_results.setText(result.getValue());
                    str_followup_results_key = result.getKey();
                    break;
            }

    }

}
