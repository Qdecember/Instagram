package com.example.instagram.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;

import com.example.instagram.util.Utils;
import com.example.instagram.widget.FeedContextMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedContextMenuManager extends RecyclerView.OnScrollListener implements View.OnAttachStateChangeListener {

    private static String TAG = "FeedContextMenuManager";

    private static FeedContextMenuManager instance; //管理pop菜单的单例

    private FeedContextMenu contextMenu;

    private boolean isContextMenuDismissing;

    private boolean isContextMenuShowing;

    public static FeedContextMenuManager getInstance() {
        if (instance == null) {
            instance = new FeedContextMenuManager();
        }
        return instance;
    }


    public FeedContextMenuManager() {

    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        contextMenu = null;
    }

    public void toggleContextMenuFromView(View openingView, int feetItem, FeedContextMenu.OnFeedContextMenuItemClickListener listener) {
        if (contextMenu == null) {
            showContextMenuFromView(openingView, feetItem, listener);
        } else {
            hideContextMenu();
        }
    }

    //菜单初始化
    private void showContextMenuFromView(final View openingView, int feedItem, FeedContextMenu.OnFeedContextMenuItemClickListener listener) {
     if (!isContextMenuShowing) {
         isContextMenuShowing = true;
         //构造出来菜单对象，
         contextMenu = new FeedContextMenu(openingView.getContext());
         contextMenu.bindToItem(feedItem); //与item绑定
         contextMenu.addOnAttachStateChangeListener(this);
         contextMenu.setOnFeedMenuItemClickListener(listener);

         //找到根视图并添加view
         ((ViewGroup)openingView.getRootView().findViewById(android.R.id.content)).addView(contextMenu);

         contextMenu.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
             @Override
             public boolean onPreDraw() {
                 contextMenu.getViewTreeObserver().removeOnPreDrawListener(this);
                 //必须在onPreDraw中进行回调
                 setUpContextMenuInitialPosition(openingView);
                 performShowAnamation();
                 return false;

             }
         });
     }

    }

    //菜单定位
    private void setUpContextMenuInitialPosition(View openingView) {
        final int[] openingViewLocation = new int[2];
        openingView.getLocationOnScreen(openingViewLocation);
        int additionalBottomMargin = Utils.dpToPx(16);
        contextMenu.setTranslationX(openingViewLocation[0] - contextMenu.getWidth() / 3);
        contextMenu.setTranslationY(openingViewLocation[1] - contextMenu.getHeight() - additionalBottomMargin);
    }


    //设置菜单显示的动画
    private void performShowAnamation() {
        //设置轴点
        contextMenu.setPivotX(contextMenu.getWidth() / 2);
        contextMenu.setPivotY(contextMenu.getHeight());
        contextMenu.setScaleX(0.1f);
        contextMenu.setScaleY(0.1f);
        contextMenu.animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(150)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isContextMenuShowing = false;
                    }
                });
    }


    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (contextMenu != null) {
            hideContextMenu();
            contextMenu.setTranslationY(contextMenu.getTranslationY() - dy);
        }
    }


    public void hideContextMenu() {
        if (!isContextMenuDismissing) {
            isContextMenuDismissing = true;
            performDismissAnimation();
        }
    }

    private void performDismissAnimation() {
        contextMenu.setPivotX(contextMenu.getWidth() / 2);
        contextMenu.setPivotY(contextMenu.getHeight());

        contextMenu.animate()
                .scaleX(0f).scaleY(0f)
                .setStartDelay(100)
                .setStartDelay(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (contextMenu != null) {
                            contextMenu.dismiss();
                        }
                        isContextMenuDismissing = false;
                    }
                }).start();
    }

}

