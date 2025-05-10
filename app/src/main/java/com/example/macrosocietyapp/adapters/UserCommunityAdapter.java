package com.example.macrosocietyapp.adapters;

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

public class UserCommunityAdapter extends RecyclerView.Adapter<UserCommunityAdapter.CommunityViewHolder> {

    private final CommunityAdapter.OnOpenCommunityClickListener listenerToOpenCommunity;

    public interface OnDeleteClickListener {
        void onDelete(Community community);
    }

    public interface OnOpenCommunityClickListener {
        void onOpenCommunity(Community community);
    }

    private final List<Community> communities;
    private final OnDeleteClickListener clickListener;

    public UserCommunityAdapter(List<Community> communities, OnDeleteClickListener clickListener, CommunityAdapter.OnOpenCommunityClickListener listenerToOpenCommunity) {
        this.communities = communities;
        this.clickListener = clickListener;
        this.listenerToOpenCommunity = listenerToOpenCommunity;
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_community, parent, false);
        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        Community community = communities.get(position);
        holder.name.setText(community.getName());
        holder.description.setText(community.getDescription());

        holder.name.setOnClickListener(v -> listenerToOpenCommunity.onOpenCommunity(community));

        holder.button.setVisibility(View.VISIBLE);
        holder.button.setText("Удалить");
        holder.button.setOnClickListener(v -> clickListener.onDelete(community));
    }

    @Override
    public int getItemCount() {
        return communities.size();
    }

    public static class CommunityViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        Button button;

        CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textCommunityName);
            description = itemView.findViewById(R.id.textCommunityDescription);
            button = itemView.findViewById(R.id.buttonJoinLeave);
        }
    }
}
