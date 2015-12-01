package com.kapil.robot.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kapil.robot.db.dao.DAOAirNews;
import com.kapil.robot.model.AirNews;
import com.kapil.robot.ui.R;
import com.kapil.robot.util.DividerItemDecoration;
import com.kapil.robot.util.MyLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment {
    Activity mActivity;
    RecyclerView rv;
    public NewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_news, container, false);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));

        // setupRecyclerView(rv);
        return rv;

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DAOAirNews daoAirNews = new DAOAirNews(mActivity);
        ArrayList<AirNews> mAirNews=daoAirNews.getAllNews();
        MyLog.debugLog("Air news size:"+mAirNews.size());
        rv.setAdapter(new AirnewsAdapter(getActivity(), mAirNews));

    }


    public static class AirnewsAdapter
            extends RecyclerView.Adapter<AirnewsAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;
        private ArrayList<AirNews> mAirnews;

        public static class ViewHolder extends RecyclerView.ViewHolder {


            public final View mView;
            /* public final ImageView mImageView;*/
            public final TextView mTxtTitle;
            public final TextView mTxtCreated;
            public final TextView mTxtReady;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                mTxtTitle = (TextView) view.findViewById(R.id.txtTitle);
                mTxtCreated = (TextView) view.findViewById(R.id.txtCreated);
                mTxtReady = (TextView) view.findViewById(R.id.txtReady);

            }


        }

       /* public String getValueAt(int position) {
            return mValues.get(position);
        }*/

        public AirnewsAdapter(Context context, ArrayList<AirNews> mAirnews) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            this.mAirnews=mAirnews;

        }

        public AirnewsAdapter(Context context, List<String> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // holder.mBoundString = mValues.get(position);

            final AirNews lAirnews=mAirnews.get(position);
            holder.mTxtTitle.setText(lAirnews.getName());
            holder.mTxtCreated.setText(lAirnews.getCreated());
            if(lAirnews.getIsReady()==1)
                holder.mTxtReady.setText("Ready");
            else
                holder.mTxtReady.setText("Downloading..");


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  Context cv=v.getContext();
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    File file = new File("sdcard/AirNews/"+lAirnews.getName());
                    MyLog.debugLog("Playing:"+Uri.fromFile(file));
                    intent.setDataAndType(Uri.fromFile(file), "audio/*");
                    cv.startActivity(intent);


                }
            });

            /*Glide.with(holder.mImageView.getContext())
                    .load(Cheeses.getRandomCheeseDrawable())
                    .fitCenter()
                    .into(holder.mImageView);*/
        }

        @Override
        public int getItemCount() {
            return mAirnews.size();
        }
    }



}
