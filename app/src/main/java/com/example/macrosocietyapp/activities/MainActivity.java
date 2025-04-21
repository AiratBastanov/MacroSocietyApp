package com.example.macrosocietyapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.fragments.*;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.saveUserRoom.UserRepository;
import com.example.macrosocietyapp.utils.SharedPrefManager;
import com.example.macrosocietyapp.viewmodel.SharedViewModel;
import com.example.macrosocietyapp.viewmodel.SharedViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private SharedViewModel sharedViewModel;
    private User currentUser=null;
    private UserRepository repository;
    private BottomNavigationView bottomNav;

    private Fragment selectedFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Создание ViewModel через ViewModelFactory
        sharedViewModel = new ViewModelProvider(this, new SharedViewModelFactory(getApplication())).get(SharedViewModel.class);
        repository = new UserRepository(this);

        bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setSelectedItemId(R.id.nav_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            handleNavigationItemSelected(item.getItemId());
            return true;
        });

        Executors.newSingleThreadExecutor().execute(() -> {
            currentUser = repository.getCurrentUser();
            runOnUiThread(() -> {
                if (currentUser != null) {
                    sharedViewModel.setUser(currentUser);
                    // Создаем ProfileFragment для текущего пользователя
                    ProfileFragment profileFragment = ProfileFragment.newInstance(
                            currentUser.getId(),
                            false // Это свой профиль, поэтому isFriend = false
                    );
                    replaceFragmentClearingBackStack(profileFragment);
                } else {
                    replaceFragment(new HomeFragment());
                }
            });
        });
    }

    private void handleNavigationItemSelected(int itemId) {
        switch (itemId) {
            case R.id.nav_communities:

                if (selectedFragment instanceof FeedFragment) {
                    return;
                } else {
                    selectedFragment = new FeedFragment();
                }
                break;
            case R.id.nav_people:
                if (selectedFragment instanceof UsersFriendsFragment) {
                    return;
                } else {
                    selectedFragment = new UsersFriendsFragment();// Внутри — табы: "Пользователи", "Друзья"
                }
                break;
            case R.id.nav_messages:
                if (selectedFragment instanceof MessagesFragment) {
                    return;
                }
                else{
                    selectedFragment = new MessagesFragment();
                }
                break;
            case R.id.nav_profile:
                if(selectedFragment instanceof ProfileFragment){
                    return;
                }
                else{
                    selectedFragment = new ProfileFragment();
                }
                break;
        }
        if (selectedFragment != null) {
            replaceFragment(selectedFragment);
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

    public void replaceFragmentClearingBackStack(Fragment fragment) { //Когда выполнен вход в аккаунт
        selectedFragment=fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Очищаем back stack
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainFrameLayout, fragment);
        transaction.commit(); // НЕ добавляем в back stack!
        bottomNav.setVisibility(View.VISIBLE);
    }

    public Integer getUserId(){
        if(currentUser!=null){
            return currentUser.getId();
        }
        else{
            currentUser = repository.getCurrentUser();
            return currentUser.getId();
        }
    }

    public User getUser(){
        if(currentUser!=null){
            return currentUser;
        }
        else{
            currentUser = repository.getCurrentUser();
            return currentUser;
        }
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