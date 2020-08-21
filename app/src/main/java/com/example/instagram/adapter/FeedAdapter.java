package com.example.instagram.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.example.instagram.R;
import com.example.instagram.util.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int ANIMATED_TIMES_COUNT = 2;
    private int lastAnimationPosition = -1;
    private Context mContext;
    private int mItemsCount = 0;
    private OnFeedItemClickListener mOnFeedItemClicklistener;

    public FeedAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
         ViewHolder viewHolder = (ViewHolder)holder;

         if (position % 2 == 0) {
             viewHolder.centerImage.setImageResource(R.drawable.img_feed_center_1);
             viewHolder.bottomImage.setImageResource(R.drawable.img_feed_bottom_1);

         } else {
             viewHolder.centerImage.setImageResource(R.drawable.img_feed_center_2);
             viewHolder.bottomImage.setImageResource(R.drawable.img_feed_bottom_2);
         }
         viewHolder.bottomImage.setOnClickListener(this);
         viewHolder.btnLike.setOnClickListener(this);
         viewHolder.btnComment.setOnClickListener(this);
         viewHolder.btnMore.setOnClickListener(this);
         viewHolder.bottomImage.setTag(position);

    }

    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_TIMES_COUNT - 1) {
            return;
        }

        if (position > lastAnimationPosition) {
            lastAnimationPosition = position;
            view.setTranslationY(Utils.getScreenHeight(mContext));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.0f))
                    .setDuration(700)
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return mItemsCount;
    }

    public void updateItems() {
        mItemsCount = 10;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (mOnFeedItemClicklistener != null) {
            switch (v.getId()) {
                case R.id.ivFeedBottom:
                    mOnFeedItemClicklistener.onCommentsClick(v, (Integer)v.getTag());
                    break;

                case R.id.btnMore:
                    mOnFeedItemClicklistener.onMoreClick(v, (Integer)v.getId());
                    break;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView centerImage = itemView.findViewById(R.id.ivFeedCenter);
        ImageView bottomImage = itemView.findViewById(R.id.ivFeedBottom);
        ImageView btnLike = itemView.findViewById(R.id.btnLike);
        ImageView btnComment = itemView.findViewById(R.id.btnComment);
        ImageView btnMore = itemView.findViewById(R.id.btnMore);


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnFeedItemClicklistener(OnFeedItemClickListener onFeedItemClicklistener) {
        this.mOnFeedItemClicklistener = onFeedItemClicklistener;
    }

    public interface  OnFeedItemClickListener {
        void onCommentsClick(View v, int postion);

        void onMoreClick(View v, int position);

        void onProfileClick(View v);
    }
}
