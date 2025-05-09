package com.example.macrosocietyapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.models.Community;

import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

    private final List<Community> communityList;
    private final Context context;
    private final OnJoinClickListener listener;
    private final OnOpenCommunityClickListener listenerToOpenCommunity;

    public interface OnJoinClickListener {
        void onJoin(Community community);
    }

    public interface OnOpenCommunityClickListener {
        void onOpenCommunity(Community community);
    }

    public CommunityAdapter(List<Community> communityList, Context context, OnJoinClickListener listener,OnOpenCommunityClickListener listenerToOpenCommunity) {
        this.communityList = communityList;
        this.context = context;
        this.listener = listener;
        this.listenerToOpenCommunity = listenerToOpenCommunity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_community, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Community community = communityList.get(position);
        holder.name.setText(community.getName());
        holder.description.setText(community.getDescription());

        holder.button.setVisibility(View.VISIBLE);
        if (community.isMember()) {
            holder.button.setText("Отписаться");
        } else {
            holder.button.setText("Подписаться");
        }
        holder.button.setOnClickListener(v -> listener.onJoin(community));
        holder.name.setOnClickListener(v -> listenerToOpenCommunity.onOpenCommunity(community));
       /* holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommunityActivity.class);
            intent.putExtra("communityId", community.getId());
            context.startActivity(intent);
        });*/
    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        Button button;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textCommunityName);
            description = itemView.findViewById(R.id.textCommunityDescription);
            button = itemView.findViewById(R.id.buttonJoinLeave);
        }
    }
}

