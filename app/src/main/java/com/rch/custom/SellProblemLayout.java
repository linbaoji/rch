package com.rch.custom;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rch.R;
import com.rch.entity.ProblemEntity;
import com.rch.entity.SellBean;
import com.rch.entity.SellCatEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/30.
 */

public class SellProblemLayout extends LinearLayout implements View.OnClickListener{
    Context context;
    List<View> isPanlViewList;//隐藏的卖内容面板
    List<ImageView> arrowViewList;
    public SellProblemLayout(Context context) {
        super(context);
        this.context=context;
    }

    public SellProblemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public SellProblemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }
    View view;
    LinearLayout problem_layout;
    TextView problem_length,problem_title,problem_content;
    ImageView problem_arrow;
    public void initData(JSONArray data) throws JSONException {
        Gson gson;
        gson=new Gson();
        removeAllViews();
        if(data!=null&&data.length()>0) {
//            List<ProblemEntity> list=data.get(0).getList();
//            if(list!=null&&list.size()>0) {
                isPanlViewList=new ArrayList<>();
                arrowViewList=new ArrayList<>();
                arrowViewList.clear();
                isPanlViewList.clear();
                for (int i=0;i<data.length();i++) {
                    view = LayoutInflater.from(context).inflate(R.layout.sell_problem_layout,null,false);
                    problem_length = (TextView) view.findViewById(R.id.problem_length);
                    problem_title = (TextView) view.findViewById(R.id.problem_title);
                    problem_content = (TextView) view.findViewById(R.id.problem_content);
                    problem_arrow= (ImageView) view.findViewById(R.id.problem_arrow);
                    problem_layout = (LinearLayout) view.findViewById(R.id.problem_layout);


                    JSONObject object= (JSONObject) data.get(i);
                    String akey=object.getString("akey");
                    problem_length.setText("Q" + i+1);
                    problem_title.setText(akey);

                    JSONArray array=object.getJSONArray("aValue");
                    if(array.length()>0) {//只有一个
                        if (array.length() == 1){
                            List<String>strlist=gson.fromJson(array.toString(),new TypeToken<List<String>>(){}.getType());
                            problem_content.setText(strlist.get(0));
                        }else {//有很多
                            List<SellBean>list=gson.fromJson(array.toString(),new TypeToken<List<SellBean>>(){}.getType());
                            if(list!=null&&list.size()>0){
                                StringBuilder builder=new StringBuilder();
                                for(int j=0;j<list.size();j++){
                                    builder.append(list.get(j).getAkey() + "\n\n"+list.get(j).getaValue().get(0)+"\n\n\n");
                                }
                                problem_content.setText(builder.toString());
                            }
                        }
                    }


//
                    problem_layout.setTag(i);
                    problem_content.setTag("false");

                    isPanlViewList.add(problem_content);
                    arrowViewList.add(problem_arrow);
                    problem_layout.setOnClickListener(this);

                    addView(view);
                }
            }
        }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.problem_layout:
                String lenth=String.valueOf(v.getTag());
                View view=isPanlViewList.get(Integer.parseInt(lenth));
                ImageView imageView=arrowViewList.get(Integer.parseInt(lenth));
                boolean isViewShow=Boolean.valueOf(String.valueOf(view.getTag()));
                showFilterPanel(!isViewShow,v,view,imageView);//==false 显示面板
                break;
        }
    }

    /*打开关闭动画*/
    public void showFilterPanel(final boolean isAnim,final View clickView,final View animView,ImageView imageView)
    {
        clickView.setEnabled(false);
        Animation animation;
        if(isAnim) {
            animation = AnimationUtils.loadAnimation(context, R.anim.record_open);
            animView.setClickable(true);
            imageView.setImageResource(R.mipmap.up_arrow);
        }
        else {
            animation = AnimationUtils.loadAnimation(context, R.anim.record_close);
            animView.setClickable(false);
            imageView.setImageResource(R.mipmap.down_arrow);
        }
        animView.clearAnimation();
        animView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clickView.setEnabled(true);
                animView.setTag(isAnim);
                if(isAnim) {
                    animView.setVisibility(View.VISIBLE);
                }
                else {
                    animView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
