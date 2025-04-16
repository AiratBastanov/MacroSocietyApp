package com.example.macrosocietyapp.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.fragments.HomeFragment;
import com.example.macrosocietyapp.fragments.ProfileFragment;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.saveUserRoom.UserRepository;
import com.example.macrosocietyapp.utils.SharedPrefManager;
import com.example.macrosocietyapp.viewmodel.SharedViewModel;
import com.example.macrosocietyapp.viewmodel.SharedViewModelFactory;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private SharedViewModel sharedViewModel;
    private User currentUser;
    private UserRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Создание ViewModel через ViewModelFactory
        sharedViewModel = new ViewModelProvider(this, new SharedViewModelFactory(getApplication())).get(SharedViewModel.class);
        repository = new UserRepository(this);

        Executors.newSingleThreadExecutor().execute(() -> {
            currentUser = repository.getCurrentUser();
            runOnUiThread(() -> {
                if (currentUser != null) {
                    sharedViewModel.setUser(currentUser);
                    replaceFragmentClearingBackStack(new ProfileFragment());
                } else {
                    replaceFragment(new HomeFragment());
                }
            });
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout, fragment);
        if (!(fragment instanceof HomeFragment)) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void replaceFragmentClearingBackStack(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Очищаем back stack
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainFrameLayout, fragment);
        transaction.commit(); // НЕ добавляем в back stack!
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}