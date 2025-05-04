package com.example.macrosocietyapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.macrosocietyapp.fragments.AllCommunitiesFragment;
import com.example.macrosocietyapp.fragments.CreateCommunityFragment;
import com.example.macrosocietyapp.fragments.FriendRequestsListFragment;
import com.example.macrosocietyapp.fragments.FriendsListFragment;
import com.example.macrosocietyapp.fragments.MyCommunitiesFragment;
import com.example.macrosocietyapp.fragments.UsersListFragment;

public class CommunityPagerAdapter extends FragmentStateAdapter {
    public CommunityPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new AllCommunitiesFragment();
            case 1: return new MyCommunitiesFragment();
            case 2: return new CreateCommunityFragment();
            default: return new AllCommunitiesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
