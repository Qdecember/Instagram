package com.example.instagram.comment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagram.R;
import com.example.instagram.view.RoundedTransformation;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = "CommentsAdapter";

    private Context mContext;
    private int itemCount = 0;
    private int lastAnimationedPosition = -1;
    private int mAvatarSize;

    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;

    public CommentsAdapter(Context context) {
        this.mContext = context;
        mAvatarSize = context.getResources().getDimensionPixelSize(R.dimen.btn_fab_size);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
//        runEnterAnimation(holder.itemView, position);
        CommentViewHolder viewHolder = (CommentViewHolder)holder;
        switch (position % 3) {
            case 0:
                viewHolder.tvComment.setText("Lorem ipsum dolor sit amet, consectetur adipisicing elit.");
                break;
            case 1:
                viewHolder.tvComment.setText("Cupcake ipsum dolor sit amet bear claw.");
                break;
            case 2:
                viewHolder.tvComment.setText("Cupcake ipsum dolor sit. Amet gingerbread cupcake. Gummies ice cream dessert icing marzipan apple pie dessert sugar plum.");
                break;

        }
        Picasso.get()
                .load(R.drawable.ic_launcher)
                .centerCrop()
                .resize(mAvatarSize, mAvatarSize)
                .into(viewHolder.ivUserAvatar);

    }

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimationedPosition) {
            lastAnimationedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

    public void updateItems() {
        itemCount  = 10;
        notifyDataSetChanged();
    }

    public void addItem() {
        itemCount ++;
        notifyItemInserted(itemCount - 1);
    }

    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }
    @Override
    public int getItemCount() {
        return itemCount;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView   ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
        TextView tvComment = itemView.findViewById(R.id.tvComment);

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
