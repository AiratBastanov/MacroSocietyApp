package com.example.macrosocietyapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.activities.MainActivity;
import com.example.macrosocietyapp.api.MainAPI;
import com.example.macrosocietyapp.api.callbacks.CommunityCallback;
import com.example.macrosocietyapp.api.callbacks.SimpleCallback;
import com.example.macrosocietyapp.models.Community;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCommunityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editCommunityName, editCommunityDescription;
    private Button buttonCreateCommunity;
    private View viewCreateCommunityFragment;

    public CreateCommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateCommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateCommunityFragment newInstance(String param1, String param2) {
        CreateCommunityFragment fragment = new CreateCommunityFragment();
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
        viewCreateCommunityFragment = inflater.inflate(R.layout.fragment_create_community, container, false);

        editCommunityName = viewCreateCommunityFragment.findViewById(R.id.editCommunityName);
        editCommunityDescription = viewCreateCommunityFragment.findViewById(R.id.editCommunityDescription);
        buttonCreateCommunity = viewCreateCommunityFragment.findViewById(R.id.buttonCreateCommunity);

        buttonCreateCommunity.setOnClickListener(v -> createCommunity());

        return viewCreateCommunityFragment;
    }

    private void createCommunity() {
        String name = editCommunityName.getText().toString().trim();
        String description = editCommunityDescription.getText().toString().trim();

        if (name.isEmpty()) {
            editCommunityName.setError("Введите название сообщества");
            return;
        }

        int currentUserId = ((MainActivity) requireActivity()).getUserId();

        Community community = new Community();
        community.setName(name);
        community.setDescription(description);
        community.setCreatorId(currentUserId);

        MainAPI.createCommunity(community, new CommunityCallback() {
            @Override
            public void onSuccess(Community createdCommunity) {
                Toast.makeText(getContext(), "Сообщество создано: " + createdCommunity.getName(), Toast.LENGTH_SHORT).show();
                editCommunityName.setText("");
                editCommunityDescription.setText("");
                // Дополнительно: можно перейти к списку сообществ или обновить UI
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Ошибка создания: " + error, Toast.LENGTH_SHORT).show();
                Log.e("213",error.toString());
            }
        });
    }
}