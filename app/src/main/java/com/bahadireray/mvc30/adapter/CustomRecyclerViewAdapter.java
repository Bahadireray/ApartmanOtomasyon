package com.bahadireray.mvc30.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bahadireray.mvc30.R;
import com.bahadireray.mvc30.model.MyModel;

import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int TYPE_EMPTY = 0, TYPE_DEFAULT = 1;

    private List<Object> objectList;
    private Context context;

    public CustomRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setObjectFeed(List<Object> list) {
        this.objectList = list;
    }


    @Override
    public int getItemViewType(int position) {
        if(objectList == null ){
            return  TYPE_EMPTY;
        }
        else {
            if (objectList.get(position) instanceof MyModel) {
                return TYPE_DEFAULT;
            }
            /*else if(objectList.get(position) instanceof digermodel){}*/
        }
        return -1;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case TYPE_EMPTY:
                ((EmptyViewHolder) holder).showView();

                break;
            case TYPE_DEFAULT:
                MyModel myModel = (MyModel) objectList.get(position);
                ((DefaultViewHolder) holder).showView(myModel);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return objectList == null ? 1 : objectList.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case TYPE_EMPTY:
                viewHolder = new EmptyViewHolder(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_empty, parent, false));

                break;
            case TYPE_DEFAULT:
                viewHolder = new DefaultViewHolder( LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_mymodel, parent, false));
                break;

            default:
                viewHolder = null;
                break;
        }


        return viewHolder;
    }




    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }

        private void showView() {

        }
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder {
        private TextView etId, etTitle;
        public DefaultViewHolder(@NonNull View view) {
            super(view);
            etId = (TextView) view.findViewById(R.id.tvId);
            etTitle = (TextView) view.findViewById(R.id.tvTitle);

        }

        private void showView( MyModel myModel) {
            etId.setText(myModel.getId());
            etTitle.setText(myModel.getTitle());

        }


    }


}