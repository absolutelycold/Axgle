package com.absolutelycold.axgle;


import android.content.Intent;
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

    public static final int FRAGMENT_ALL = 0;
    public static final int FRAGMENT_SEARCH = 1;

    private AllVideos allVideosInfo = null;
    private PullRefreshLayout pullRefreshLayout = null;
    private RecyclerView recyclerView = null;
    private boolean isLoading = false;
    private boolean isRefreshing = false;
    private String order = "mr";
    private Integer CHID = null;
    private int fragmentType;
    private String searchContent;
    private Boolean needBlur;

    public AllVideosFragment() {
        // Required empty public constructor
    }

    public static AllVideosFragment newInstance(int type, String searchContent, Boolean needBlur) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragment_type", type);
        bundle.putString("search_content", searchContent);
        bundle.putBoolean("need_blur", needBlur);
        AllVideosFragment allVideosFragment = new AllVideosFragment();
        allVideosFragment.setArguments(bundle);
        return allVideosFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_all_videos, container, false);
        fragmentType = getArguments().getInt("fragment_type");
        searchContent = getArguments().getString("search_content");
        needBlur = getArguments().getBoolean("need_blur");
        pullRefreshLayout = (PullRefreshLayout) linearLayout.findViewById(R.id.all_videos_refresh);
        recyclerView = (RecyclerView) linearLayout.findViewById(R.id.all_videos_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        if (fragmentType == FRAGMENT_ALL) {
            new LoadAllVideosInfoTask().execute(0, 20, CHID, order);
        }
        else {
            new LoadAllVideosInfoTask().execute(searchContent, 0, 20, CHID, order);
        }


        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (fragmentType == FRAGMENT_ALL) {
                    new LoadAllVideosInfoTask().execute(0, 20, CHID, order);
                }
                else {
                    new LoadAllVideosInfoTask().execute(searchContent, 0, 20, CHID, order);
                }

            }
        });

        return linearLayout;
    }

    public class LoadAllVideosInfoTask extends AsyncTask<Object, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isRefreshing) {
                pullRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        protected Void doInBackground(Object... objects) {
            publishProgress();
            if (fragmentType == FRAGMENT_ALL) {
                allVideosInfo = new AllVideos((Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (String) objects[3]);
            }
            else {
                allVideosInfo = new VideoSearch((String)objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3], (String) objects[4]);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CoverCardAdapter coverCardAdapter = new CoverCardAdapter(allVideosInfo, needBlur);
            coverCardAdapter.SetAllVideoListener(new CoverCardAdapter.AllVideoListener() {
                @Override
                public void onSingleVideoChoose(int position) {
                    Intent intent = new Intent(getActivity(), ShowVideoPreviewActivity.class);
                    intent.putExtra("preview_url", allVideosInfo.getPreviewUrl(position));
                    intent.putExtra("video_url",allVideosInfo.getVideoUrl(position));
                    intent.putExtra("video_embedded_url", allVideosInfo.getEmbeddedVideoUrl(position));
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(coverCardAdapter);
            recyclerView.getAdapter().notifyDataSetChanged();
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

                        System.out.println("Is Reash End??: " + allVideos.isReachEnd());
                        if (!allVideos.isReachEnd()) {
                            new LoadMoreVideosTask().execute();
                        }
                        else {
                            int beforeAddItemCount = allVideosInfo.ItemsCount();
                            allVideosInfo.removeItem(beforeAddItemCount - 1);
                            recyclerView.getAdapter().notifyItemRemoved(beforeAddItemCount);
                            recyclerView.getAdapter().notifyDataSetChanged();
                            isLoading = false;
                        }
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
            if (fragmentType == FRAGMENT_ALL) {
                allVideosInfo.LoadMore();
            }
            else {
                allVideosInfo.LoadMoreSearch(searchContent);
            }

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

    public void refreshAll() {
        recyclerView.scrollToPosition(0);
        if (fragmentType == FRAGMENT_ALL) {
            new LoadAllVideosInfoTask().execute(0, 20, CHID, order);
        }
        else {
            new LoadAllVideosInfoTask().execute(searchContent, 0, 20, CHID, order);
        }
    }

    public void refreshUsingNewOrder(String order) {
        this.order = order;
        recyclerView.scrollToPosition(0);
        if (fragmentType == FRAGMENT_ALL) {
            new LoadAllVideosInfoTask().execute(0, 20, CHID, order);
        }
        else {
            new LoadAllVideosInfoTask().execute(searchContent, 0, 20, CHID, order);
        }
    }

    public void refreshUsingNewCHID(Integer CHID) {
        //System.out.println("Select CHID: " + CHID);
        this.CHID = CHID;
        recyclerView.scrollToPosition(0);
        if (fragmentType == FRAGMENT_ALL) {
            new LoadAllVideosInfoTask().execute(0, 20, CHID, order);
        }
        else {
            new LoadAllVideosInfoTask().execute(searchContent, 0, 20, CHID, order);
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setNeedBlur(Boolean needBlur) {
        this.needBlur = needBlur;
    }
}
