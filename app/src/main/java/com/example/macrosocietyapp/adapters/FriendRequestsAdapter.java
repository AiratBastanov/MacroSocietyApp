package com.example.macrosocietyapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.models.FriendRequest;
import com.example.macrosocietyapp.models.SimpleUser;
import com.example.macrosocietyapp.models.User;

import java.util.List;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.FriendRequestViewHolder> {

    private List<FriendRequest> requests;
    private RequestActionListener listener;

    public interface RequestActionListener {
        void onAccept(FriendRequest request);
        void onReject(FriendRequest request);
    }

    public FriendRequestsAdapter(List<FriendRequest> requests, RequestActionListener listener) {
        this.requests = requests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_request, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        FriendRequest request = requests.get(position);
        SimpleUser sender = request.getSender();
        holder.name.setText(sender.getName());
        holder.email.setText(sender.getEmail());

        holder.acceptBtn.setOnClickListener(v -> listener.onAccept(request));
        holder.rejectBtn.setOnClickListener(v -> listener.onReject(request));
    }


    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;
        Button acceptBtn, rejectBtn;

        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.requestUserName);
            email = itemView.findViewById(R.id.requestUserEmail);
            acceptBtn = itemView.findViewById(R.id.acceptRequestBtn);
            rejectBtn = itemView.findViewById(R.id.rejectRequestBtn);
        }
    }
}
