package com.example.macrosocietyapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
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
import com.example.macrosocietyapp.saveUserRoom.UserRepository;
import com.example.macrosocietyapp.utils.SharedPrefManager;
import com.example.macrosocietyapp.viewmodel.SharedViewModel;
import com.example.macrosocietyapp.viewmodel.SharedViewModelFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CodeVerificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CodeVerificationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText editTextCode;
    private Button buttonVerify,buttonResendCode;
    private String email,state;
    private ProgressBar progressBar;
    private View viewCodeVerificationFragment;
    private SharedViewModel sharedViewModel;
    private UserRepository userRepository;

    private CountDownTimer countDownTimer;
    private final long RESEND_TIMEOUT_MS = 2 * 60 * 1000; // 2 минуты


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
        // Используем ViewModel с Factory
        sharedViewModel = new ViewModelProvider(requireActivity(), new SharedViewModelFactory(requireActivity().getApplication())).get(SharedViewModel.class);
        userRepository = new UserRepository(requireContext());
        return viewCodeVerificationFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            email = getArguments().getString("email");
            state = getArguments().getString("state");
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
            } else {
                if(state.equals("register")){
                    User user = sharedViewModel.getUser().getValue();
                    if (user != null) {
                        registerUserInDb(user, code);
                    } else {
                        Toast.makeText(getContext(), "Не удалось получить пользователя", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    User user = sharedViewModel.getUser().getValue();
                    if (user != null) {
                        logInUser(user.getEmail(), code);
                    } else {
                        Toast.makeText(getContext(), "Не удалось получить пользователя", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonResendCode.setOnClickListener(v -> resendCode());

        startResendTimer(); // Стартуем таймер при открытии
    }

    private void registerUserInDb(User user, String code) {
        progressBar.setVisibility(View.VISIBLE);

        MainAPI.registerUser(user, code, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    User registeredUser = response.body();
                    sharedViewModel.saveUserToDb(registeredUser);
                    SharedPrefManager.getInstance(requireContext()).clear();
                    Toast.makeText(getContext(), "Регистрация завершена", Toast.LENGTH_SHORT).show();
                    // Создаем ProfileFragment для нового зарегистрированного пользователя
                    ProfileFragment profileFragment = ProfileFragment.newInstance(
                            registeredUser.getId(),
                            false // Это свой профиль
                    );
                    ((MainActivity) requireActivity()).replaceFragmentClearingBackStack(profileFragment);
                } else {
                    Toast.makeText(getContext(), "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logInUser(String email, String code) {
        progressBar.setVisibility(View.VISIBLE);

        MainAPI.loginWithCode(email, code, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    User loggedInUser = response.body();
                    sharedViewModel.saveUserToDb(loggedInUser);
                    SharedPrefManager.getInstance(requireContext()).clear();
                    Toast.makeText(getContext(), "Вход выполнен", Toast.LENGTH_SHORT).show();
                    ProfileFragment profileFragment = ProfileFragment.newInstance(
                            loggedInUser.getId(),
                            false // Это свой профиль
                    );
                    ((MainActivity) requireActivity()).replaceFragmentClearingBackStack(profileFragment);
                } else {
                    Toast.makeText(getContext(), "Ошибка входа", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendCode() {
        progressBar.setVisibility(View.VISIBLE);
        MainAPI.sendVerificationCode(email,state, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Код отправлен повторно", Toast.LENGTH_SHORT).show();
                    startResendTimer(); // Запускаем таймер после успешной отправки
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

    private void startResendTimer() {
        buttonResendCode.setEnabled(false);
        countDownTimer = new CountDownTimer(RESEND_TIMEOUT_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                buttonResendCode.setText("Повторить через " + secondsLeft + " сек");
            }

            @Override
            public void onFinish() {
                buttonResendCode.setEnabled(true);
                buttonResendCode.setText("Отправить код повторно");
            }
        }.start();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}