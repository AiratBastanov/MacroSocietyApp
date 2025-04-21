package com.example.macrosocietyapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.models.Friend;
import com.example.macrosocietyapp.models.User;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {

    private List<User> friends;
    private FriendClickListener clickListener;
    private FriendRemoveListener removeListener;

    public interface FriendClickListener {
        void onFriendClick(User friend);
    }

    public interface FriendRemoveListener {
        void onFriendRemove(User friend);
    }

    public FriendsAdapter(List<User> friends, FriendClickListener clickListener, FriendRemoveListener removeListener) {
        this.friends = friends;
        this.clickListener = clickListener;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        User friend = friends.get(position);
        holder.name.setText(friend.getName());
        holder.email.setText(friend.getEmail());

        holder.itemView.setOnClickListener(v -> clickListener.onFriendClick(friend));
        holder.removeBtn.setOnClickListener(v -> removeListener.onFriendRemove(friend));
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;
        Button removeBtn;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.friendName);
            email = itemView.findViewById(R.id.friendEmail);
            removeBtn = itemView.findViewById(R.id.removeFriendBtn);
        }
    }
}
