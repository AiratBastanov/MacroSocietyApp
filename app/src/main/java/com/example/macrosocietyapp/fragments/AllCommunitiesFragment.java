package com.example.macrosocietyapp.fragments;

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
import com.example.macrosocietyapp.api.MainAPI;
import com.example.macrosocietyapp.api.callbacks.CommunityListCallback;
import com.example.macrosocietyapp.api.callbacks.SimpleCallback;
import com.example.macrosocietyapp.models.Community;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllCommunitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllCommunitiesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private CommunityAdapter adapter;
    private List<Community> communities = new ArrayList<>();
    private MainAPI api;
    private View viewAllCommunitiesFragment;

    private SharedViewModel sharedViewModel;

    public AllCommunitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllCommunitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllCommunitiesFragment newInstance(String param1, String param2) {
        AllCommunitiesFragment fragment = new AllCommunitiesFragment();
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
        viewAllCommunitiesFragment = inflater.inflate(R.layout.fragment_all_communities, container, false);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        recyclerView = viewAllCommunitiesFragment.findViewById(R.id.recyclerViewCommunities);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CommunityAdapter(communities, getContext(), community -> {
            User user = sharedViewModel.getUser().getValue();// получи ID пользователя
            if (user.getId() != null) {
                MainAPI.subscribeToCommunity(user.getId(), community.getId(), new SimpleCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "Вы подписались на: " + community.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(getContext(), "Ошибка: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(getContext(), "Ошибка! Попробуйте позже", Toast.LENGTH_SHORT).show();
            }
        });


        recyclerView.setAdapter(adapter);

        loadCommunities();

        return viewAllCommunitiesFragment;
    }

    private void loadCommunities() {
        MainAPI.getAllCommunities(new CommunityListCallback() {
            @Override
            public void onSuccess(List<Community> result) {
                communities.clear();
                communities.addAll(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Ошибка загрузки сообществ: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}