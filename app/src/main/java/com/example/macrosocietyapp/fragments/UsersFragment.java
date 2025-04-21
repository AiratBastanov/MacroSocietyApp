package com.example.macrosocietyapp.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.macrosocietyapp.adapters.UsersAdapter;
import com.example.macrosocietyapp.api.MainAPI;
import com.example.macrosocietyapp.api.callbacks.SimpleCallback;
import com.example.macrosocietyapp.api.callbacks.UsersCallback;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.utils.AesEncryptionService;
import com.example.macrosocietyapp.utils.SharedPrefManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private List<User> userList = new ArrayList<>();
    private View viewUsersFragment;

    public UsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersFragment newInstance(String param1, String param2) {
        UsersFragment fragment = new UsersFragment();
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
        viewUsersFragment = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = viewUsersFragment.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new UsersAdapter(userList, new UsersAdapter.UserClickListener() {
            @Override
            public void onUserClick(User user) {
                boolean isFriend = false; // логика, определяющая друга

                // Создаём экземпляр фрагмента
                UserProfileFragment fragment = UserProfileFragment.newInstance(user.getId(), isFriend);

                // Показываем как BottomSheet
                fragment.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), fragment.getTag());
                if(fragment.isHidden()){
                    fragment.dismiss();
                }
            }

            @Override
            public void onAddFriendClick(User user) {
                int currentUserId = ((MainActivity) requireActivity()).getUserId();
                MainAPI.sendFriendRequest(currentUserId, user.getId(), new SimpleCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "Заявка отправлена", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        recyclerView.setAdapter(adapter);
        loadUsers();

        return viewUsersFragment;
    }

    private void loadUsers() {
        // Загрузка пользователей с сервера
        Log.e("f",AesEncryptionService.encrypt(String.valueOf(((MainActivity) requireActivity()).getUserId())));
        MainAPI.getAllUsers(AesEncryptionService.encrypt(String.valueOf(((MainActivity) requireActivity()).getUserId())), new UsersCallback() {
            @Override
            public void onSuccess(List<User> users) {
                userList.clear();
                userList.addAll(users);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}