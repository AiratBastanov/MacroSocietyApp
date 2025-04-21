package com.example.macrosocietyapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.adapters.UsersAndFriendsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFriendsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private View viewUsersFriendsFragment;

    public UsersFriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersFriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersFriendsFragment newInstance(String param1, String param2) {
        UsersFriendsFragment fragment = new UsersFriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewUsersFriendsFragment = inflater.inflate(R.layout.fragment_users_and_friends, container, false);

        viewPager = viewUsersFriendsFragment.findViewById(R.id.viewPager);
        tabLayout = viewUsersFriendsFragment.findViewById(R.id.tabLayout);

        UsersAndFriendsPagerAdapter adapter = new UsersAndFriendsPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Пользователи");
                            break;
                        case 1:
                            tab.setText("Друзья");
                            break;
                        case 2:
                            tab.setText("Заявки");
                            break;
                    }
                }).attach();

        return viewUsersFriendsFragment;
    }
}