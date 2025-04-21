package com.example.macrosocietyapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.macrosocietyapp.api.MainAPI;
import com.example.macrosocietyapp.api.callbacks.SimpleCallback;
import com.example.macrosocietyapp.api.callbacks.UserCallback;
import com.example.macrosocietyapp.api.callbacks.UserStatsCallback;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.models.UserStats;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends BottomSheetDialogFragment {

    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_CHECK = "check";

    private int userId;
    private boolean check;

    private ImageView profileImage;
    private TextView profileName, profileEmail, profileBio;
    private TextView friendsCount, postsCount, communitiesCount;
    private Button messageBtn, addFriendBtn, removeFriendBtn;

    private User user;

    private View viewUserProfileFragment;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(int userId, boolean check) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        args.putBoolean(ARG_CHECK, check);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID);
            check = getArguments().getBoolean(ARG_CHECK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewUserProfileFragment = inflater.inflate(R.layout.fragment_user_profile, container, false);
        Log.e("das21", String.valueOf(userId));
        initViews(viewUserProfileFragment);
        loadUserData();
        return viewUserProfileFragment;
    }

    private void initViews(View view) {
        profileImage = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileBio = view.findViewById(R.id.profileBio);
        friendsCount = view.findViewById(R.id.friendsCount);
        postsCount = view.findViewById(R.id.postsCount);
        communitiesCount = view.findViewById(R.id.communitiesCount);
        messageBtn = view.findViewById(R.id.messageBtn);
        addFriendBtn = view.findViewById(R.id.addFriendBtn);
        //removeFriendBtn = view.findViewById(R.id.removeFriendBtn);

        if (check) {
            // показываем кнопки для чужого пользователя
            messageBtn.setVisibility(View.VISIBLE);
            addFriendBtn.setVisibility(View.VISIBLE);
            //removeFriendBtn.setVisibility(View.GONE); // пока не друг
        } else {
            // скрываем ненужные кнопки
            messageBtn.setVisibility(View.GONE);
            addFriendBtn.setVisibility(View.GONE);
            //removeFriendBtn.setVisibility(View.GONE);
        }

        addFriendBtn.setOnClickListener(v -> {
            int myId = ((MainActivity) requireActivity()).getUserId();
            MainAPI.sendFriendRequest(myId, userId, new SimpleCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getContext(), "Заявка отправлена", Toast.LENGTH_SHORT).show();
                    addFriendBtn.setVisibility(View.GONE);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        messageBtn.setOnClickListener(v -> {
            // логика открытия чата
            Toast.makeText(getContext(), "Открытие чата...", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserData() {
        MainAPI.getUserById(userId, new UserCallback() {
            @Override
            public void onSuccess(User u) {
                user = u;
                Log.e("das",user.getId().toString());
                Log.e("das",u.getId().toString());
                updateUI();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        MainAPI.getUserStats(userId, new UserStatsCallback() {
            @Override
            public void onSuccess(UserStats stats) {
                friendsCount.setText(String.valueOf(stats.getFriendsCount()));
                postsCount.setText(String.valueOf(stats.getPostsCount()));
                communitiesCount.setText(String.valueOf(stats.getCommunitiesCount()));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Ошибка загрузки статистики", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        profileName.setText(user.getName());
        profileEmail.setText(user.getEmail());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Очистка ссылок на View и данные пользователя
        profileImage = null;
        profileName = null;
        profileEmail = null;
        profileBio = null;
        friendsCount = null;
        postsCount = null;
        communitiesCount = null;
        messageBtn = null;
        addFriendBtn = null;
        //removeFriendBtn = null;

        user = null;
    }
}