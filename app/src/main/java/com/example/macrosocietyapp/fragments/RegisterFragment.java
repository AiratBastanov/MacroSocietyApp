package com.example.macrosocietyapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.activities.MainActivity;
import com.example.macrosocietyapp.api.MainAPI;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.utils.AesEncryptionService;
import com.example.macrosocietyapp.utils.SharedPrefManager;
import com.example.macrosocietyapp.viewmodel.SharedViewModel;
import com.example.macrosocietyapp.viewmodel.SharedViewModelFactory;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editTextName, editTextEmail;
    private Button buttonRegister;
    private View viewRegisterFragment;
    private ProgressBar progressBar;
    private User user;

    private SharedViewModel sharedViewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        viewRegisterFragment = inflater.inflate(R.layout.fragment_register, container, false);
        editTextName = viewRegisterFragment.findViewById(R.id.editTextName);
        editTextEmail = viewRegisterFragment.findViewById(R.id.editTextEmail);
        progressBar = viewRegisterFragment.findViewById(R.id.progressBar);
        buttonRegister = viewRegisterFragment.findViewById(R.id.buttonRegister);
        TextView loginLink = viewRegisterFragment.findViewById(R.id.loginLink);

        // Используем ViewModel с Factory
        sharedViewModel = new ViewModelProvider(requireActivity(), new SharedViewModelFactory(requireActivity().getApplication())).get(SharedViewModel.class);

        buttonRegister.setOnClickListener(v -> registerUser());
        loginLink.setOnClickListener(v -> navigateToLogin());

        return viewRegisterFragment;
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Введите имя");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Введите email");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Сохраняем данные пользователя во ViewModel
        user = new User(name, email);
        sharedViewModel.setUser(user);

        // Отправляем код подтверждения
        MainAPI.sendVerificationCode(email,"register", new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    navigateToCodeVerification(email);
                } else {
                    Toast.makeText(getContext(), "Ошибка при отправке кода", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToCodeVerification(String email) {
        CodeVerificationFragment fragment = new CodeVerificationFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("state", "register");
        fragment.setArguments(args);
        ((MainActivity) requireActivity()).replaceFragment(fragment);
    }

    private void navigateToLogin() {
        LoginFragment fragment = new LoginFragment();
        ((MainActivity) requireActivity()).replaceFragment(fragment);
    }
}