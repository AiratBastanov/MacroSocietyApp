package com.example.macrosocietyapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.models.Comment;
import com.example.macrosocietyapp.utils.CommentDiffCallback;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private final List<Comment> comments = new ArrayList<>();

    public CommentsAdapter(List<Comment> initialComments) {
        if (initialComments != null) {
            comments.addAll(initialComments);
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvUsername.setText(comment.getUsername());
        holder.tvCommentText.setText(comment.getText());
        holder.tvCommentDate.setText(comment.getFormattedDate());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<Comment> newComments) {
        if (newComments == null) return;
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CommentDiffCallback(comments, newComments));
        comments.clear();
        comments.addAll(newComments);
        diffResult.dispatchUpdatesTo(this);
    }

    public void addCommentAtStart(Comment comment) {
        comments.add(0, comment);
        notifyItemInserted(0);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvCommentText, tvCommentDate;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            tvCommentDate = itemView.findViewById(R.id.tvCommentDate);
        }
    }
}


