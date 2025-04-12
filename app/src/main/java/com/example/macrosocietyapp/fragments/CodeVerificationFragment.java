package com.example.macrosocietyapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.macrosocietyapp.api.MainAPI;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CodeVerificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CodeVerificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editTextCode;
    private Button buttonVerify,buttonResendCode;
    private String email;
    private ProgressBar progressBar;
    private View viewCodeVerificationFragment;

    public CodeVerificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CodeVerificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CodeVerificationFragment newInstance(String param1, String param2) {
        CodeVerificationFragment fragment = new CodeVerificationFragment();
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
        viewCodeVerificationFragment = inflater.inflate(R.layout.fragment_code_verification, container, false);
        return viewCodeVerificationFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            email = getArguments().getString("email");
        }

        TextView textViewEmail = view.findViewById(R.id.textViewEmail);
        editTextCode = view.findViewById(R.id.editTextCode);
        buttonVerify = view.findViewById(R.id.buttonVerify);
        buttonResendCode = view.findViewById(R.id.buttonResendCode);
        progressBar = view.findViewById(R.id.progressBar);

        textViewEmail.setText("Код отправлен на: " + email);

        buttonVerify.setOnClickListener(v -> {
            String code = editTextCode.getText().toString().trim();
            if (code.isEmpty()) {
                editTextCode.setError("Введите код");
                editTextCode.requestFocus();
                return;
            }
            verifyCode(email, code);
        });

        buttonResendCode.setOnClickListener(v -> resendCode());
    }

    private void verifyCode(String email, String code) {
        progressBar.setVisibility(View.VISIBLE);

        MainAPI.loginWithCode(email, code, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // Успешная авторизация
                    Toast.makeText(getContext(), "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                    // Здесь можно перейти на главный экран приложения
                    // ((MainActivity) requireActivity()).replaceFragment(new MainAppFragment());
                } else {
                    Toast.makeText(getContext(), "Неверный код или срок его действия истек", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendCode() {
        progressBar.setVisibility(View.VISIBLE);

        MainAPI.sendVerificationCode(email, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Код отправлен повторно", Toast.LENGTH_SHORT).show();
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
}