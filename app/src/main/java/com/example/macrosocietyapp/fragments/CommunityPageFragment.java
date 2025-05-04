package com.example.macrosocietyapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.adapters.CommunityPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private CommunityPagerAdapter pagerAdapter;
    private View viewCommunityPageFragment;

    public CommunityPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityPageFragment newInstance(String param1, String param2) {
        CommunityPageFragment fragment = new CommunityPageFragment();
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
        viewCommunityPageFragment = inflater.inflate(R.layout.fragment_community_page, container, false);
        tabLayout = viewCommunityPageFragment.findViewById(R.id.tabLayoutCommunity);
        viewPager = viewCommunityPageFragment.findViewById(R.id.viewPagerCommunity);

        pagerAdapter = new CommunityPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Все сообщества");
                            break;
                        case 1:
                            tab.setText("Мои сообщества");
                            break;
                        case 2:
                            tab.setText("Создать сообщество");
                            break;
                    }
                }).attach();

        return viewCommunityPageFragment;
    }
}