package com.example.macrosocietyapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.activities.MainActivity;
import com.example.macrosocietyapp.adapters.FriendsAdapter;
import com.example.macrosocietyapp.api.MainAPI;
import com.example.macrosocietyapp.api.callbacks.FriendListCallback;
import com.example.macrosocietyapp.api.callbacks.SimpleCallback;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private FriendsAdapter adapter;
    private List<User> friendsList = new ArrayList<>();
    private View viewFriendsFragment;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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
        viewFriendsFragment = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView = viewFriendsFragment.findViewById(R.id.recyclerViewFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FriendsAdapter(friendsList,
                friend -> {
                    // открыть профиль друга (check = true)
                    UserProfileFragment fragment = UserProfileFragment.newInstance(friend.getId(), true);
                    fragment.show(getChildFragmentManager(), "UserProfileFragment");
                },
                friend -> {
                    int currentUserId = ((MainActivity) requireActivity()).getUserId();
                    MainAPI.removeFriend(currentUserId, friend.getId(), new SimpleCallback() {
                        @Override
                        public void onSuccess() {
                            friendsList.remove(friend);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Друг удалён", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(getContext(), "Ошибка удаления: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        );

        recyclerView.setAdapter(adapter);
        loadFriends();

        return viewFriendsFragment;
    }

    private void loadFriends() {
        int currentUserId = ((MainActivity) requireActivity()).getUserId();
        MainAPI.getFriends(currentUserId, new FriendListCallback() {
            @Override
            public void onSuccess(List<User> friends) {
                friendsList.clear();
                friendsList.addAll(friends);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}