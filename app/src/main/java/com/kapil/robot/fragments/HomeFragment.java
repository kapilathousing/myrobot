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

import com.kapil.robot.db.dao.DAOFeed;
import com.kapil.robot.model.Feed;
import com.kapil.robot.ui.R;
import com.kapil.robot.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment  {
    Activity mActivity;
    int feedType=1;

    RecyclerView rv;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_home, container, false);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));

       // setupRecyclerView(rv);
        return rv;


    }

  /*  private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                getRandomSublist(Cheeses.sCheeseStrings, 30)));
    }
*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DAOFeed daoFeeds = new DAOFeed(mActivity);
        ArrayList<Feed> mfeed=daoFeeds.getAllFeeds(feedType);
        if(mfeed!=null)
        rv.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), mfeed));
    }

    public void setFeedType(int feedType){
        this.feedType=feedType;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;
        private ArrayList<Feed> mFeed;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
           /* public final ImageView mImageView;*/
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                /*mImageView = (ImageView) view.findViewById(R.id.avatar);*/
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

       /* public String getValueAt(int position) {
            return mValues.get(position);
        }*/

        public SimpleStringRecyclerViewAdapter(Context context, ArrayList<Feed> mFeed) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
           this.mFeed=mFeed;

        }

        public SimpleStringRecyclerViewAdapter(Context context, List<String> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
           // holder.mBoundString = mValues.get(position);

            Feed lFeed=mFeed.get(position);
            holder.mBoundString=lFeed.getLink();
            holder.mTextView.setText(lFeed.getTitle());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    /*Intent intent = new Intent(context, CheeseDetailActivity.class);
                    intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);

                    context.startActivity(intent);*/

                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(holder.mBoundString));
                    context.startActivity(intent);


                }
            });

            /*Glide.with(holder.mImageView.getContext())
                    .load(Cheeses.getRandomCheeseDrawable())
                    .fitCenter()
                    .into(holder.mImageView);*/
        }

        @Override
        public int getItemCount() {
            return mFeed.size();
        }
    }



}
