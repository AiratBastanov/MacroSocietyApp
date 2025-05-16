package com.example.macrosocietyapp.fragments;

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
import android.widget.Toast;

import com.example.macrosocietyapp.R;
import com.example.macrosocietyapp.activities.MainActivity;
import com.example.macrosocietyapp.adapters.CommentsAdapter;
import com.example.macrosocietyapp.api.MainAPI;
import com.example.macrosocietyapp.api.callbacks.BaseCommentCallback;
import com.example.macrosocietyapp.api.callbacks.CommentsCallback;
import com.example.macrosocietyapp.models.Comment;
import com.example.macrosocietyapp.models.CommentDto;
import com.example.macrosocietyapp.models.CreateCommentRequest;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.utils.AesEncryptionService;
import com.example.macrosocietyapp.utils.DateUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentBottomSheetDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_POST_ID = "post_id";

    private String encryptedPostId;

    private RecyclerView recyclerComments;
    private Button btnSendComment;
    private EditText etNewComment;
    private View viewCommentBottomSheetDialogFragment;

    private CommentsAdapter commentsAdapter;
    private List<Comment> commentList = new ArrayList<>();

    public CommentBottomSheetDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CommentBottomSheetDialogFragment.
     */
    public static CommentBottomSheetDialogFragment newInstance(String encryptedPostId) {
        CommentBottomSheetDialogFragment fragment = new CommentBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POST_ID, encryptedPostId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            encryptedPostId = getArguments().getString(ARG_POST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewCommentBottomSheetDialogFragment = inflater.inflate(R.layout.fragment_comment_bottom_sheet_dialog, container, false);

        recyclerComments = viewCommentBottomSheetDialogFragment.findViewById(R.id.recyclerComments);
        btnSendComment = viewCommentBottomSheetDialogFragment.findViewById(R.id.btnSendComment);
        etNewComment = viewCommentBottomSheetDialogFragment.findViewById(R.id.etNewComment);

        recyclerComments.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsAdapter = new CommentsAdapter(commentList);
        recyclerComments.setAdapter(commentsAdapter);

        btnSendComment.setOnClickListener(v -> {
            String text = etNewComment.getText().toString().trim();
            if (!text.isEmpty()) {
                sendComment(text);
            }
        });

        loadComments(encryptedPostId);

        return viewCommentBottomSheetDialogFragment;
    }

    private void loadComments(String postId) {
        MainAPI.getCommentsByPost(postId, new CommentsCallback() {
            @Override
            public void onSuccess(List<Comment> comments) {
                commentsAdapter.setComments(comments);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Ошибка загрузки комментариев: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendComment(String content) {
        User currentUser = ((MainActivity) requireActivity()).getUser();

        String decryptedPostIdStr = AesEncryptionService.decrypt(encryptedPostId);
        if (decryptedPostIdStr == null) {
            Toast.makeText(getContext(), "Ошибка расшифровки postId", Toast.LENGTH_SHORT).show();
            return;
        }

        int decryptedPostId;
        try {
            decryptedPostId = Integer.parseInt(decryptedPostIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Некорректный postId", Toast.LENGTH_SHORT).show();
            return;
        }

        CreateCommentRequest request = new CreateCommentRequest(
                encryptedPostId,
                currentUser.getId(),
                content
        );

        MainAPI.createComment(request, new BaseCommentCallback() {
            @Override
            public void onSuccess(Integer commentId, String createdAt) {
                Comment comment = new Comment();
                comment.setUserId(currentUser.getId());
                comment.setPostId(decryptedPostId);
                comment.setContent(content);
                comment.setId(commentId);
                comment.setUsername(currentUser.getName());
                try {
                    comment.setCreatedAt(DateUtils.parseUtc(createdAt));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                commentsAdapter.addCommentAtStart(comment);
                recyclerComments.scrollToPosition(0);
                etNewComment.setText("");
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Ошибка отправки комментария: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        /*Comment comment = new Comment();
        comment.setUserId(currentUser.getId());
        comment.setPostId(decryptedPostId);
        comment.setContent(content);

        Log.e("sad",comment.getContent());
        Log.e("sad", String.valueOf(comment.getPostId()));
        Log.e("sad", String.valueOf(comment.getUserId()));

        MainAPI.addComment(comment, new BaseCommentCallback() {
            @Override
            public void onSuccess(int commentId, Date createdAt) {
                comment.setId(commentId);
                comment.setUsername(currentUser.getName());
                comment.setCreatedAt(createdAt);

                commentsAdapter.addCommentAtStart(comment);
                recyclerComments.scrollToPosition(0);
                etNewComment.setText("");
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Ошибка при добавлении комментария: " + error, Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}