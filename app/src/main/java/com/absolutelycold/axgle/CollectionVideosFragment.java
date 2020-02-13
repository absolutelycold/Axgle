package com.absolutelycold.axgle;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionVideosFragment extends Fragment {

    private VideoCollection videoCollection = null;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private PullRefreshLayout refreshLayout;

    boolean isLoading = false;

    public CollectionVideosFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_all_videos, container, false);


        new AllVideosLoadTask().execute(0, 5);


        refreshLayout = linearLayout.findViewById(R.id.pull_refresh_layout);

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new AllVideosLoadTask().execute(0, 5);
            }
        });

        recyclerView = refreshLayout.findViewById(R.id.all_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));;


        return linearLayout;
    }

    private class AllVideosLoadTask extends AsyncTask<Integer, Void, VideoCollection> {


        @Override
        protected VideoCollection doInBackground(Integer... pages) {
            publishProgress();
            VideoCollection vc = new VideoCollection(pages[0],pages[1]);
            return vc;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(VideoCollection vc) {
            super.onPostExecute(vc);

            videoCollection = vc;
            adapter = new CoverCardAdapter(vc);
            recyclerView.setAdapter(adapter);
            InitScrollListener();
            refreshLayout.setRefreshing(false);
        }
    }

    public class LoadMoreTask extends AsyncTask<Void, Void, Void> {


        private VideoCollection videoCollection;
        private RecyclerView recyclerView;
        private int videoInfoCounts;


        public LoadMoreTask(VideoCollection videoCollection, RecyclerView recyclerView) {
            this.videoCollection = videoCollection;
            this.recyclerView = recyclerView;
            videoInfoCounts = videoCollection.ItemsCount();
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... rvs) {

            videoCollection.LoadMore();
            return null;
        }

        @Override
        protected void onPostExecute(Void rv) {
            super.onPostExecute(rv);
            this.videoCollection.removeItem(videoInfoCounts - 1);
            this.recyclerView.getAdapter().notifyItemRemoved(videoInfoCounts);
            this.recyclerView.getAdapter().notifyDataSetChanged();
            isLoading = false;
        }
    }



    public void InitScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                if (dy > 0) {
//                    int visibleItemCount = layoutManager.getChildCount();
//                    int totalItemsCount = layoutManager.getItemCount();
//                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
//                    if (visibleItemCount + pastVisibleItems >= totalItemsCount) {
//                        LoadMoreTask loadMoreTask = new LoadMoreTask(videoCollection);
//                        loadMoreTask.execute(recyclerView);
//                    }
//                }

                final VideoCollection vc = (VideoCollection) ((CoverCardAdapter)recyclerView.getAdapter()).getVideoInfos();
                if (!isLoading) {
                    if (layoutManager != null && (layoutManager.findLastVisibleItemPosition() == vc.ItemsCount() - 1)) {
                        vc.addItem(null);
                        recyclerView.getAdapter().notifyItemInserted(vc.ItemsCount() - 1);
                        isLoading = true;
                        LoadMoreTask loadMoreTask = new LoadMoreTask(vc, recyclerView);
                        loadMoreTask.execute();
                    };
                }
            }

        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
