package com.example.macrosocietyapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.macrosocietyapp.fragments.FriendRequestsListFragment;
import com.example.macrosocietyapp.fragments.FriendsListFragment;
import com.example.macrosocietyapp.fragments.UsersListFragment;

public class UsersAndFriendsPagerAdapter extends FragmentStateAdapter {
    public UsersAndFriendsPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new UsersListFragment();
            case 1: return new FriendsListFragment();
            case 2: return new FriendRequestsListFragment();
            default: return new UsersListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Теперь 3 вкладки
    }
}

