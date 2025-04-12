package com.example.macrosocietyapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

        User user = new User(name, email);
        String userJson = new Gson().toJson(user);
        String encryptedData = AesEncryptionService.encrypt(userJson);

        if (encryptedData == null) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Ошибка шифрования данных", Toast.LENGTH_SHORT).show();
            return;
        }

        MainAPI.registerUser(encryptedData, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    String decryptedResponse = AesEncryptionService.decrypt(response.body());
                    if (decryptedResponse != null) {
                        User registeredUser = new Gson().fromJson(decryptedResponse, User.class);
                        SharedPrefManager.getInstance(requireContext()).saveUser(registeredUser);
                        navigateToCodeVerification(registeredUser.email);
                    } else {
                        Toast.makeText(getContext(), "Ошибка расшифровки ответа", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String error = "Ошибка регистрации";
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            String decryptedError = AesEncryptionService.decrypt(errorBody);
                            if (decryptedError != null) {
                                error = decryptedError;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToCodeVerification(String email) {
        CodeVerificationFragment fragment = new CodeVerificationFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        fragment.setArguments(args);
        ((MainActivity) requireActivity()).replaceFragment(fragment);
    }

    private void navigateToLogin() {
        LoginFragment fragment = new LoginFragment();
        ((MainActivity) requireActivity()).replaceFragment(fragment);
    }
}