package com.example.macrosocietyapp.fragments;

import android.os.Bundle;

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
import com.example.macrosocietyapp.adapters.FriendRequestsAdapter;
import com.example.macrosocietyapp.api.MainAPI;
import com.example.macrosocietyapp.api.callbacks.FriendRequestsCallback;
import com.example.macrosocietyapp.api.callbacks.SimpleCallback;
import com.example.macrosocietyapp.models.FriendRequest;
import com.example.macrosocietyapp.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendRequestsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private FriendRequestsAdapter adapter;
    private List<FriendRequest> requestsList = new ArrayList<>();
    private View viewFriendRequestsFragment;

    public FriendRequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendRequestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendRequestsFragment newInstance(String param1, String param2) {
        FriendRequestsFragment fragment = new FriendRequestsFragment();
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
        viewFriendRequestsFragment = inflater.inflate(R.layout.fragment_friend_requests, container, false);
        recyclerView = viewFriendRequestsFragment.findViewById(R.id.recyclerViewFriendRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FriendRequestsAdapter(requestsList, new FriendRequestsAdapter.RequestActionListener() {
            @Override
            public void onAccept(FriendRequest request) {
                String senderEncryptedId = request.getSender().getId();
                String receiverEncryptedId = request.getReceiver().getId();

                MainAPI.acceptRequest(senderEncryptedId, receiverEncryptedId, new SimpleCallback() {
                    @Override
                    public void onSuccess() {
                        loadFriendRequests();
                        Toast.makeText(getContext(), "Заявка принята", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onReject(FriendRequest request) {
                String senderEncryptedId = request.getSender().getId();
                String receiverEncryptedId = request.getReceiver().getId();

                MainAPI.rejectRequest(senderEncryptedId, receiverEncryptedId, new SimpleCallback() {
                    @Override
                    public void onSuccess() {
                        loadFriendRequests();
                        Toast.makeText(getContext(), "Заявка отклонена", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        recyclerView.setAdapter(adapter);
        loadFriendRequests();

        return viewFriendRequestsFragment;
    }

    private void loadFriendRequests() {
        int currentUserId = ((MainActivity) requireActivity()).getUserId();
        MainAPI.getIncomingRequests(currentUserId, new FriendRequestsCallback() {
            @Override
            public void onSuccess(List<FriendRequest> requests) {
                requestsList.clear();
                requestsList.addAll(requests);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}