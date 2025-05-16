package com.example.macrosocietyapp.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.example.macrosocietyapp.models.Comment;

import java.util.List;

public class CommentDiffCallback extends DiffUtil.Callback {
    private final List<Comment> oldList;
    private final List<Comment> newList;

    public CommentDiffCallback(List<Comment> oldList, List<Comment> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Comment oldItem = oldList.get(oldItemPosition);
        Comment newItem = newList.get(newItemPosition);
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}

