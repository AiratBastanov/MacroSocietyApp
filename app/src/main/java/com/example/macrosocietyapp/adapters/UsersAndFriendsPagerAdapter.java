package com.example.macrosocietyapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.macrosocietyapp.fragments.FriendRequestsFragment;
import com.example.macrosocietyapp.fragments.FriendsFragment;
import com.example.macrosocietyapp.fragments.UsersFragment;

public class UsersAndFriendsPagerAdapter extends FragmentStateAdapter {
    public UsersAndFriendsPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new UsersFragment();
            case 1: return new FriendsFragment();
            case 2: return new FriendRequestsFragment();
            default: return new UsersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Теперь 3 вкладки
    }
}

