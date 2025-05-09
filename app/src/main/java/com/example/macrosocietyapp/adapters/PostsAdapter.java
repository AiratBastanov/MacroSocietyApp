package com.example.macrosocietyapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.models.Post;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private List<Post> posts;

    public PostsAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.textUsername.setText(post.getUsername());
        holder.textContent.setText(post.getContent());
        if (post.getCreatedAt() != null) {
            String formattedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                    .format(post.getCreatedAt());
            holder.textCreatedAt.setText(formattedDate);
        } else {
            holder.textCreatedAt.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textUsername, textCreatedAt, textContent;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textUsername = itemView.findViewById(R.id.textUsername);
            textCreatedAt = itemView.findViewById(R.id.textCreatedAt);
            textContent = itemView.findViewById(R.id.textContent);
        }
    }
}

