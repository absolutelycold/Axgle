package com.absolutelycold.axgle;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllVideosFragment extends Fragment {

    private AllVideos allVideosInfo = null;
    private PullRefreshLayout pullRefreshLayout = null;
    private RecyclerView recyclerView = null;
    private boolean isLoading = false;
    private boolean isRefreshing = false;

    public AllVideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_all_videos, container, false);
        pullRefreshLayout = (PullRefreshLayout) linearLayout.findViewById(R.id.all_videos_refresh);
        recyclerView = (RecyclerView) linearLayout.findViewById(R.id.all_videos_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new LoadAllVideosInfoTask().execute(0, 5);

        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadAllVideosInfoTask().execute(0, 5);
            }
        });

        return linearLayout;
    }

    public class LoadAllVideosInfoTask extends AsyncTask<Integer, Void, Void> {


        @Override
        protected Void doInBackground(Integer... integers) {
            publishProgress();
            allVideosInfo = new AllVideos(integers[0], integers[1]);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (!isRefreshing) {
                pullRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("bind data to adapter: " + allVideosInfo.ItemsCount());
            recyclerView.setAdapter(new CoverCardAdapter(allVideosInfo));
            InitRecyclerViewListener();
            pullRefreshLayout.setRefreshing((false));
            isRefreshing = false;
        }
    }

    public void InitRecyclerViewListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastVisibleItemPosition() == allVideosInfo.ItemsCount() - 1) {
                        isLoading = true;
                        AllVideos allVideos = (AllVideos) ((CoverCardAdapter)recyclerView.getAdapter()).getVideoInfos();
                        allVideos.addItem(null);
                        recyclerView.getAdapter().notifyItemInserted(allVideos.ItemsCount() - 1);
                        new LoadMoreVideosTask().execute();
                    }
                }
            }
        });
    }

    public class LoadMoreVideosTask extends AsyncTask<Void, Void, Void> {

        int beforeAddItemCount;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            beforeAddItemCount = allVideosInfo.ItemsCount();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            allVideosInfo.LoadMore();
            return  null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            allVideosInfo.removeItem(beforeAddItemCount - 1);
            recyclerView.getAdapter().notifyItemRemoved(beforeAddItemCount);
            recyclerView.getAdapter().notifyDataSetChanged();
            isLoading = false;
            super.onPostExecute(aVoid);
        }
    }
}
