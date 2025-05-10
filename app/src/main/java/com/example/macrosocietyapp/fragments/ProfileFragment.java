package com.example.macrosocietyapp.fragments;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.activities.MainActivity;
import com.example.macrosocietyapp.adapters.PostsAdapter;
import com.example.macrosocietyapp.api.MainAPI;
import com.example.macrosocietyapp.api.callbacks.PostsCallback;
import com.example.macrosocietyapp.api.callbacks.SimpleCallback;
import com.example.macrosocietyapp.api.callbacks.UserCallback;
import com.example.macrosocietyapp.api.callbacks.UserStatsCallback;
import com.example.macrosocietyapp.models.Post;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.models.UserStats;
import com.example.macrosocietyapp.saveUserRoom.UserRepository;
import com.example.macrosocietyapp.utils.AesEncryptionService;
import com.example.macrosocietyapp.utils.CircleTransform;
import com.example.macrosocietyapp.utils.SharedPrefManager;
import com.example.macrosocietyapp.viewmodel.SharedViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_IS_FRIEND = "is_friend";

    private User user;
    private boolean isCurrentUser;
    private boolean isFriend;
    private int userId;
    private SharedViewModel sharedViewModel;

    private ImageView profileImage;
    private TextView profileName, profileEmail, profileBio;
    private TextView friendsCount, postsCount, communitiesCount;
    private View viewProfileFragment;
    Button logoutButton;
    private RecyclerView postsRecyclerView;
    private PostsAdapter postsAdapter;
    private List<Post> postList = new ArrayList<>();

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @param isFriend Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(int userId, boolean isFriend) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        args.putBoolean(ARG_IS_FRIEND, isFriend);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID, -1);
            isFriend = getArguments().getBoolean(ARG_IS_FRIEND, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewProfileFragment = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews(viewProfileFragment);

        sharedViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null && user.getId() == userId) {
                isCurrentUser = true;
                this.user = user;
                setupProfile();
            }
        });

        if (!isCurrentUser) {
            loadUserFromServer(userId);
        }

        return viewProfileFragment;
    }

    private void initViews(View view) {
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        friendsCount = view.findViewById(R.id.friendsCount);
        postsCount = view.findViewById(R.id.postsCount);
        communitiesCount = view.findViewById(R.id.communitiesCount);

        postsRecyclerView = view.findViewById(R.id.postsRecyclerView);

        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postsAdapter = new PostsAdapter(postList);
        postsRecyclerView.setAdapter(postsAdapter);

        logoutButton = view.findViewById(R.id.button_logout);

        logoutButton.setOnClickListener(v -> {
            logout();
        });
    }

    private void setupProfile() {
        if (user != null) {
            profileName.setText(user.getName());
            profileEmail.setText(user.getEmail());

            /*if (isCurrentUser) {
                messageBtn.setVisibility(View.GONE);
                addFriendBtn.setVisibility(View.GONE);
                removeFriendBtn.setVisibility(View.GONE);
            } else if (isFriend) {
                messageBtn.setVisibility(View.VISIBLE);
                removeFriendBtn.setVisibility(View.VISIBLE);
                addFriendBtn.setVisibility(View.GONE);
            } else {
                messageBtn.setVisibility(View.GONE);
                removeFriendBtn.setVisibility(View.GONE);
                addFriendBtn.setVisibility(View.VISIBLE);
            }*/

            loadUserStats(user.getId());
        }
    }

    private void loadUserFromServer(int userId) {
        MainAPI.getUserById(userId, new UserCallback() {
            @Override
            public void onSuccess(User fetchedUser) {
                user = fetchedUser;
                isCurrentUser = false;
                requireActivity().runOnUiThread(() -> setupProfile());
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserStats(int userId) {
        // Показываем состояние загрузки
        friendsCount.setText("...");
        postsCount.setText("...");
        communitiesCount.setText("...");

        MainAPI.getUserStats(userId, new UserStatsCallback() {
            @Override
            public void onSuccess(UserStats stats) {
                requireActivity().runOnUiThread(() -> {
                    friendsCount.setText(String.valueOf(stats.getFriendsCount()));
                    postsCount.setText(String.valueOf(stats.getPostsCount()));
                    communitiesCount.setText(String.valueOf(stats.getCommunitiesCount()));
                });
            }

            @Override
            public void onError(String error) {
                requireActivity().runOnUiThread(() -> {
                    friendsCount.setText("-");
                    postsCount.setText("-");
                    communitiesCount.setText("-");
                });
            }
        });
        String encryptedId = AesEncryptionService.encrypt(String.valueOf(userId));
        MainAPI.getPostsByUser(encryptedId, new PostsCallback() {
            @Override
            public void onSuccess(List<Post> posts) {
                postList.clear();
                postList.addAll(posts);
                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Ошибка загрузки постов: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        ((MainActivity) requireActivity()).logoutAndReturnToHome();
    }
}