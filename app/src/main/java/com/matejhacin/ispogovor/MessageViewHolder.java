package com.matejhacin.ispogovor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by matejhacin on 09/01/16.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {

    /*
    Variables
     */

    @Bind(R.id.nameTextView) TextView nameTextView;
    @Bind(R.id.contentTextView) TextView contentTextView;
    @Bind(R.id.profileCircleImageView) CircleImageView profileCircleImageView;

    /*
    Class
     */

    public MessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
