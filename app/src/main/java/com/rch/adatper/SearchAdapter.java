package com.rch.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rch.R;
import com.rch.activity.SearchActivity;
import com.rch.entity.SearchEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/23.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyHolder> {

    Context context;
    List<SearchEntity> list=new ArrayList<>();
    SearchTitleOnClickLister searchTitleOnClickLister;

    public void setSearchTitleOnClickLister(SearchTitleOnClickLister searchTitleOnClickLister) {
        this.searchTitleOnClickLister = searchTitleOnClickLister;
    }

    public SearchAdapter(Context context)
    {
        this.context=context;
    }

    @Override
    public SearchAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.search_adapter_layout,parent,false);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(SearchAdapter.MyHolder holder, int position) {
        holder.bindView(position);
        SearchEntity searchEntity=list.get(position);
        holder.tvTitle.setText(searchEntity.getShowName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        int dex;
        public MyHolder(View itemView) {
            super(itemView);
            tvTitle= (TextView) itemView.findViewById(R.id.search_adapter_title);
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchEntity searchEntity=list.get(dex);
                    if(searchTitleOnClickLister!=null)
                        searchTitleOnClickLister.onItem(searchEntity);
                }
            });

        }

        /**
         * 此方法实现列表数据的绑定和item的展开/关闭
         */
        public void bindView(int pos) {
             this.dex=pos;
        }
    }

    public void updateSearchData(List<SearchEntity> list)
    {
        this.list=list;
        notifyDataSetChanged();
    }

    public interface SearchTitleOnClickLister
    {
        public void onItem(SearchEntity entity);
    }
}
