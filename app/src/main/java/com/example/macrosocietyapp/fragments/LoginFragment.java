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
import com.example.macrosocietyapp.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editTextEmail;
    private Button buttonLogin;
    private View viewLoginFragment;
    private String email;
    private ProgressBar progressBar;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        viewLoginFragment = inflater.inflate(R.layout.fragment_login, container, false);
        editTextEmail = viewLoginFragment.findViewById(R.id.editTextEmail);
        progressBar = viewLoginFragment.findViewById(R.id.progressBar);
        buttonLogin = viewLoginFragment.findViewById(R.id.buttonSendCode);
        TextView textViewRegister = viewLoginFragment.findViewById(R.id.textViewRegister);

        buttonLogin.setOnClickListener(v -> sendVerificationCode());
        textViewRegister.setOnClickListener(v -> navigateToRegister());

        return viewLoginFragment;
    }

    private void sendVerificationCode() {
        email = editTextEmail.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Введите email");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        MainAPI.sendVerificationCode(email, new Callback<Void>() {
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
        fragment.setArguments(args);
        ((MainActivity) requireActivity()).replaceFragment(fragment);
    }

    private void navigateToRegister() {
        RegisterFragment fragment = new RegisterFragment();
        ((MainActivity) requireActivity()).replaceFragment(fragment);
    }
}