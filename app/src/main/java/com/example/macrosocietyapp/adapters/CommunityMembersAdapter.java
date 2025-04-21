package com.example.macrosocietyapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrosocietyapp.R;

/*public class CommunityMembersAdapter extends RecyclerView.Adapter<CommunityMembersAdapter.MemberViewHolder> {

    private List<CommunityMember> members;

    public CommunityMembersAdapter(List<CommunityMember> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        CommunityMember member = members.get(position);
        holder.nameTextView.setText(member.getUser().getName());
        holder.roleTextView.setText(member.getRole());
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, roleTextView;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.member_name);
            roleTextView = itemView.findViewById(R.id.member_role);
        }
    }
}*/
