package com.example.macrosocietyapp.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.fragments.HomeFragment;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.utils.SharedPrefManager;
import com.example.macrosocietyapp.viewmodel.SharedViewModel;

public class MainActivity extends AppCompatActivity {

    private SharedViewModel sharedViewModel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // Проверяем, есть ли сохраненный пользователь
        user = SharedPrefManager.getInstance(this).getUser();
        if (user != null) {
            // Переходим на главный экран приложения
            // replaceFragment(new MainAppFragment());
        } else {
            // Показываем стартовый экран
            replaceFragment(new HomeFragment());
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout, fragment);
        if (!(fragment instanceof HomeFragment)) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
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