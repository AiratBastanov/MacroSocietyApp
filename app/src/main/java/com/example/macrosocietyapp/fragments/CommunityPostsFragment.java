package com.example.macrosocietyapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.activities.MainActivity;
import com.example.macrosocietyapp.adapters.PostsAdapter;
import com.example.macrosocietyapp.api.MainAPI;
import com.example.macrosocietyapp.api.callbacks.AddPostCallback;
import com.example.macrosocietyapp.api.callbacks.PostsCallback;
import com.example.macrosocietyapp.models.Post;
import com.example.macrosocietyapp.models.PostDto;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.utils.AesEncryptionService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityPostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityPostsFragment extends BottomSheetDialogFragment {

    private static final String ARG_COMMUNITY_ID = "community_id";
    private static final String ARG_COMMUNITY_NAME = "community_name";
    private static final String ARG_COMMUNITY_DESCRIPTION = "community_description";

    private int communityId;
    private String name;
    private String desc;

    private RecyclerView postsRecyclerView;
    private PostsAdapter postsAdapter;
    private List<Post> postList = new ArrayList<>();
    private View viewCommunityPostsFragment;
    private FloatingActionButton fabAddPost;

    public CommunityPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CommunityPostsFragment.
     */
    public static CommunityPostsFragment newInstance(int communityId, String name, String description) {
        CommunityPostsFragment fragment = new CommunityPostsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COMMUNITY_ID, communityId);
        args.putString(ARG_COMMUNITY_NAME, name);
        args.putString(ARG_COMMUNITY_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            communityId = getArguments().getInt(ARG_COMMUNITY_ID);
            name = getArguments().getString(ARG_COMMUNITY_NAME);
            desc = getArguments().getString(ARG_COMMUNITY_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewCommunityPostsFragment = inflater.inflate(R.layout.fragment_community_posts, container, false);

        TextView title = viewCommunityPostsFragment.findViewById(R.id.textCommunityTitle);
        TextView description = viewCommunityPostsFragment.findViewById(R.id.communityDescription);
        postsRecyclerView = viewCommunityPostsFragment.findViewById(R.id.recyclerViewPosts);

        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postsAdapter = new PostsAdapter(postList,
                post -> {
                    CommentBottomSheetDialogFragment dialog = CommentBottomSheetDialogFragment.newInstance(post.getId());
                    dialog.show(getParentFragmentManager(), "comments_dialog");
                });
        postsRecyclerView.setAdapter(postsAdapter);

        communityId = getArguments().getInt(ARG_COMMUNITY_ID);
        name = getArguments().getString(ARG_COMMUNITY_NAME);
        desc = getArguments().getString(ARG_COMMUNITY_DESCRIPTION);

        title.setText(name);
        description.setText(desc);

        loadPosts(communityId);

        fabAddPost = viewCommunityPostsFragment.findViewById(R.id.fabAddPost);
        fabAddPost.setOnClickListener(v -> showAddPostDialog());

        return viewCommunityPostsFragment;
    }

    private void loadPosts(int communityId) {
        String encryptedId = AesEncryptionService.encrypt(String.valueOf(communityId));
        MainAPI.getPostsByCommunity(encryptedId, new PostsCallback() {
            @Override
            public void onSuccess(List<Post> posts) {
                postList.clear();
                postList.addAll(posts);
                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Ошибка загрузки постов: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddPostDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_post, null);
        EditText editTextContent = dialogView.findViewById(R.id.editTextPostContent);
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmitPost);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .create();

        buttonSubmit.setOnClickListener(v -> {
            String content = editTextContent.getText().toString().trim();
            if (!content.isEmpty()) {
                dialog.dismiss();
                createPost(content);
            } else {
                Toast.makeText(getContext(), "Введите текст поста", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void createPost(String content) {
        User currentUser = ((MainActivity) requireActivity()).getUser();
        PostDto dto = new PostDto();
        dto.setUserId(AesEncryptionService.encrypt(String.valueOf(currentUser.getId())));
        dto.setCommunityId(AesEncryptionService.encrypt(String.valueOf(communityId)));
        dto.setContent(content);

        MainAPI.addPost(dto, new AddPostCallback() {
            @Override
            public void onSuccess(String encryptedPostId, String createdAt) {
                dto.setPostId(encryptedPostId); // добавляем зашифрованный ID
                Post newPost = new Post();
                newPost.setId(dto.getPostId());
                newPost.setCommunityId(dto.getCommunityId());
                newPost.setUserId(dto.getUserId());
                newPost.setUsername(currentUser.getName());
                newPost.setContent(dto.getContent());

                // Преобразование строки в Date
                SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                parser.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    Date createdDate = parser.parse(createdAt);
                    newPost.setCreatedAt(createdDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    newPost.setCreatedAt(new Date()); // запасной вариант
                }

                postList.add(0, newPost); // добавляем в начало списка
                postsAdapter.notifyItemInserted(0);
                postsRecyclerView.scrollToPosition(0);
            }
            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Ошибка при добавлении поста: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openCommentsPost(){

    }
}