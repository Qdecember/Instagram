package com.example.instagram.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.instagram.R;
import com.example.instagram.util.Utils;

import androidx.annotation.Nullable;

public class FeedContextMenu extends LinearLayout implements View.OnClickListener {

    private static final int CONTEXT_MENU_WIDTH = Utils.dpToPx(240);

    private int feedItem = -1;

    private OnFeedContextMenuItemClickListener mOnFeedItemClickListener;

    public FeedContextMenu(Context context) {
        super(context);
        init();
    }

    public FeedContextMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FeedContextMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_context_menu, this, true);
        setBackgroundResource(R.drawable.bg_container_shadow);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(CONTEXT_MENU_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
        setClick();
    }


    public void bindToItem(int feedItem) {
        this.feedItem = feedItem;
    }


    public void onReportClick() {
        if (mOnFeedItemClickListener != null) {
            mOnFeedItemClickListener.onReportClick(feedItem);
        }
    }

    public void onSharePhotoClick() {
        if (mOnFeedItemClickListener != null) {
            mOnFeedItemClickListener.onSharePhotoClick(feedItem);
        }
    }

    public void onCopyShareUrlClick() {
        if (mOnFeedItemClickListener != null) {
            mOnFeedItemClickListener.onCopyShareUrlClick(feedItem);
        }
    }

    public void onCancelClick() {
        if (mOnFeedItemClickListener != null) {
            mOnFeedItemClickListener.onCancelClick(feedItem);
        }
    }


    public void dismiss() {
        ((ViewGroup)getParent()).removeView(this);
    }

    public void setOnFeedMenuItemClickListener(OnFeedContextMenuItemClickListener onItemClickListener) {
        this.mOnFeedItemClickListener = onItemClickListener;
    }

    void setClick() {
        findViewById(R.id.btnCancel).setOnClickListener(this);
        findViewById(R.id.btnSharePhoto).setOnClickListener(this);
        findViewById(R.id.btnReport).setOnClickListener(this);
        findViewById(R.id.btnCopyShareUrl).setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReport:
                onReportClick();
                break;

            case R.id.btnSharePhoto:
                onSharePhotoClick();
                break;

            case R.id.btnCopyShareUrl:
                onCopyShareUrlClick();
                break;

            case R.id.btnCancel:
                onCancelClick();
                break;
        }
    }

    public interface OnFeedContextMenuItemClickListener{
        void onReportClick(int feedItem);

        void onSharePhotoClick(int feedItem);

        void onCopyShareUrlClick(int feedItem);

        void onCancelClick(int feedItem);
    }
}
