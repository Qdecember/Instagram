package com.example.instagram.comment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.instagram.R;
import com.example.instagram.util.Utils;

public class CommentActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private LinearLayout mContentRoot;
    private RecyclerView mRvComments;
    private LinearLayout mllAddComment;
    private int drawingStartLocation;
    private CommentsAdapter mCommentAdapter;
    private Button mBtnSendComment;
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        initView();
        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        if (savedInstanceState == null) {
            mContentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mContentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }
        setUpComments();

    }

    private void initView() {
        mToolbar= findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white);
        mToolbar.setLogo(R.drawable.img_toolbar_logo);
        mToolbar.setTitle("");
        mContentRoot = findViewById(R.id.contentRoot);
        mllAddComment = findViewById(R.id.llAddComment);
        mRvComments = findViewById(R.id.rv_Comments);
        mBtnSendComment = findViewById(R.id.btnSendComment);
        mBtnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendCommentClick();
            }
        });
        setSupportActionBar(mToolbar);
    }

    private void startIntroAnimation() {
        mContentRoot.setScaleY(0.1f);
        mContentRoot.setPivotY(drawingStartLocation);
        mllAddComment.setTranslationY(100);

        mContentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateContent();
                    }
                }).start();
    }

    private void setUpComments() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvComments.setLayoutManager(linearLayoutManager);
        mRvComments.setHasFixedSize(true);
        mCommentAdapter = new CommentsAdapter(this);
        mRvComments.setAdapter(mCommentAdapter);
        mRvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRvComments.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    mCommentAdapter.setAnimationsLocked(true);

                }
            }
        });
    }

    private void animateContent() {
        mCommentAdapter.updateItems();
        mllAddComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }

    void onSendCommentClick() {
        mCommentAdapter.addItem();
        mCommentAdapter.setAnimationsLocked(false);
        mCommentAdapter.setDelayEnterAnimation(false);
        mRvComments.smoothScrollBy(0, mRvComments.getChildAt(0).getHeight() *  mCommentAdapter.getItemCount());
    }

    @Override
    public void onBackPressed() {
        mContentRoot.animate()
                .translationY(Utils.getScreenHeight(this))
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CommentActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                }).start();
    }
}
