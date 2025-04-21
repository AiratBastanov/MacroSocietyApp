package com.example.macrosocietyapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrosocietyapp.R;

/*public class CommunitiesAdapter extends RecyclerView.Adapter<CommunitiesAdapter.CommunityViewHolder> {

    private List<Community> communities;
    private Consumer<Community> onClick;

    public CommunitiesAdapter(List<Community> communities, Consumer<Community> onClick) {
        this.communities = communities;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community, parent, false);
        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        Community community = communities.get(position);
        holder.nameTextView.setText(community.getName());
        holder.descriptionTextView.setText(community.getDescription());

        holder.itemView.setOnClickListener(v -> onClick.accept(community));
    }

    @Override
    public int getItemCount() {
        return communities.size();
    }

    static class CommunityViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView;

        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.community_name);
            descriptionTextView = itemView.findViewById(R.id.community_description);
        }
    }
}*/

