package com.example.macrosocietyapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.adapters.CommunityAdapter;
import com.example.macrosocietyapp.adapters.UserCommunityAdapter;
import com.example.macrosocietyapp.api.MainAPI;
import com.example.macrosocietyapp.api.callbacks.CommunityListCallback;
import com.example.macrosocietyapp.api.callbacks.SimpleCallback;
import com.example.macrosocietyapp.models.Community;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCommunitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCommunitiesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private UserCommunityAdapter adapter;
    private List<Community> myCommunities = new ArrayList<>();
    private SharedViewModel sharedViewModel;
    private Context context;
    private View viewMyCommunitiesFragment;

    public MyCommunitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCommunitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCommunitiesFragment newInstance(String param1, String param2) {
        MyCommunitiesFragment fragment = new MyCommunitiesFragment();
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
        viewMyCommunitiesFragment = inflater.inflate(R.layout.fragment_my_communities, container, false);
        context = viewMyCommunitiesFragment.getContext();

        recyclerView = viewMyCommunitiesFragment.findViewById(R.id.recyclerMyCommunities);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new UserCommunityAdapter(myCommunities, this::showActionDialog);
        recyclerView.setAdapter(adapter);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                loadUserCommunities(user.getId());
            }
        });


        return viewMyCommunitiesFragment;
    }

    private void loadUserCommunities(int userId) {
        MainAPI.getUserCommunities(userId, new CommunityListCallback() {
            @Override
            public void onSuccess(List<Community> communities) {
                myCommunities.clear();
                myCommunities.addAll(communities);
                requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Ошибка загрузки сообществ: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showActionDialog(Community community) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Управление сообществом");
        builder.setItems(new CharSequence[]{"Передать права", "Удалить сообщество"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    transferOwnership(community);
                    break;
                case 1:
                    deleteCommunity(community);
                    break;
            }
        });
        builder.show();
    }

    private void transferOwnership(Community community) {
        MainAPI.transferOwnership(community.getId(), new SimpleCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Права успешно переданы", Toast.LENGTH_SHORT).show();
                User user = sharedViewModel.getUser().getValue();
                if (user != null) {
                    loadUserCommunities(user.getId());
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Ошибка передачи прав: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCommunity(Community community) {
        MainAPI.deleteCommunity(community.getId(), new SimpleCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Сообщество удалено", Toast.LENGTH_SHORT).show();
                myCommunities.remove(community);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Ошибка удаления: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}