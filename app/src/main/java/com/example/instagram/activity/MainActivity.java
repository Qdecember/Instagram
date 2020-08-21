package com.example.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import com.example.instagram.adapter.FeedAdapter;
import com.example.instagram.R;
import com.example.instagram.view.FeedContextMenuManager;
import com.example.instagram.util.Utils;
import com.example.instagram.widget.FeedContextMenu;

public class MainActivity extends AppCompatActivity implements FeedAdapter.OnFeedItemClickListener, FeedContextMenu.OnFeedContextMenuItemClickListener{

    public static String TAG = "MainActivity";
    private MenuItem mInBoxMenuItem;
    private RecyclerView mRecyclerView;
    private FeedAdapter mFeedAdapter;
    private boolean mPendingIntroAnimation;
    private static final int ANIM_DURATION_TOOBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;
    private ImageButton mbtnCreate;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (savedInstanceState == null) {
            mPendingIntroAnimation = true;
        }

        setupFeed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mInBoxMenuItem = menu.findItem(R.id.action_inbox);
        mInBoxMenuItem.setActionView(R.layout.menu_item_view);
        if (mPendingIntroAnimation) {
            mPendingIntroAnimation = false;
            startIntroAnimation();

        }
        return true;
    }




    private void initView() {
        mToolbar= findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white);
        mToolbar.setLogo(R.drawable.img_toolbar_logo);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mRecyclerView = findViewById(R.id.rvFeed);
        mbtnCreate = findViewById(R.id.btnCreate);
    }

    private void setupFeed() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mFeedAdapter = new FeedAdapter(this);
        mRecyclerView.setAdapter(mFeedAdapter);
        mFeedAdapter.setOnFeedItemClicklistener(this);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void startIntroAnimation() {
//        mbtnCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));
        mbtnCreate.setScaleX(0f);
        mbtnCreate.setScaleY(0f);

        int actionbarSize = Utils.dpToPx(56);
        mToolbar.setTranslationY(-actionbarSize);
        mInBoxMenuItem.getActionView().setTranslationY(-actionbarSize);

        mToolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOBAR)
                .setStartDelay(300);

        mInBoxMenuItem.getActionView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOBAR)
                .setStartDelay(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                }).start();
    }

    private void startContentAnimation() {
        mbtnCreate.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .start();
        mFeedAdapter.updateItems();
    }

    @Override
    public void onCommentsClick(View v, int postion) {
        Intent intent = new Intent(this, CommentActivity.class);
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        intent.putExtra(CommentActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onMoreClick(View v, int position) {
       FeedContextMenuManager.getInstance().toggleContextMenuFromView(v, position, this);
    }

    @Override
    public void onProfileClick(View v) {

    }





    @Override
    public void onReportClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onSharePhotoClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCopyShareUrlClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCancelClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }
}
