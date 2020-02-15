package com.absolutelycold.axgle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OrderDialogFragment.Listener, CategoryListDialogFragment.Listener{

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Fragment currentFragment;
    private MenuItem refreshAllItem;
    private MenuItem sortAllItem;
    private MenuItem categoryItem;
    private ArrayList<String> categoriesData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    //System.out.println("Change to All tab");
                    refreshAllItem.setVisible(true);
                    sortAllItem.setVisible(true);
                    categoryItem.setVisible(true);
                }
                else {
                    refreshAllItem.setVisible(false);
                    sortAllItem.setVisible(false);
                    categoryItem.setVisible(false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        getSupportActionBar().setElevation(0);
        new LoadAllCategories().execute();
    }

    public class LoadAllCategories extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {

        @Override
        protected ArrayList<HashMap<String, Object>> doInBackground(Void... voids) {
            return AvgleApiHelper.getCategories();
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, Object>> arrayList) {
            //System.out.println("Get categories Out: " + arrayList);
            if (arrayList != null) {
                categoriesData = new ArrayList<>();
                for (HashMap<String, Object> singleCategory : arrayList) {
                    String categoryText = (String)singleCategory.get("name") + " : " + singleCategory.get("total_videos");
                    categoriesData.add(categoryText);
                }
                categoriesData.add("All : too many");
            }
            super.onPostExecute(arrayList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action_bar, menu);
        sortAllItem = menu.findItem(R.id.action_sort_all);
        categoryItem = menu.findItem(R.id.action_category);
        refreshAllItem = menu.findItem(R.id.refresh_all_tab);
        refreshAllItem.setVisible(false);
        sortAllItem.setVisible(false);
        categoryItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        currentFragment = ((TabsPagerAdapter)viewPager.getAdapter()).getCurrentFragment();
        switch (item.getItemId()) {
            case R.id.refresh_all_tab:
                if (currentFragment instanceof AllVideosFragment) {
                    //((AllVideosFragment)currentFragment).getRecyclerView().scrollToPosition(0);
                    ((AllVideosFragment)currentFragment).refreshAll();
                }
                return true;
            case R.id.action_sort_all:
                ArrayList<String> order_options = new ArrayList<>();
                order_options.add("Latest");
                order_options.add("Most views");
                order_options.add("Top rated");
                order_options.add("Most fav");
                OrderDialogFragment.newInstance(order_options).show(getSupportFragmentManager(), OrderDialogFragment.TAG);
                return true;
            case R.id.action_category:
                if (categoriesData == null) {
                    new LoadAllCategories().execute();
                    CategoryListDialogFragment.newInstance(null).show(getSupportFragmentManager(), CategoryListDialogFragment.TAG);
                }
                else {
                    System.out.println("Bind Categories : " + categoriesData);
                    CategoryListDialogFragment.newInstance(categoriesData).show(getSupportFragmentManager(), CategoryListDialogFragment.TAG);
                }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSortOptionSelected(int position) {
        //something to to after listening
        String order = "mr";
        switch (position) {
            case 0:
                order = "mr";
                break;
            case 1:
                order = "mv";
                break;
            case 2:
                order = "tr";
                break;
            case 3:
                order = "tf";
                break;
        }
        if (currentFragment instanceof AllVideosFragment) {
            ((AllVideosFragment)currentFragment).refreshUsingNewOrder(order);
        }
        System.out.println(position + "is clicked");
    }

    @Override
    public void onCategoryClicked(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        if (position == categoriesData.size() - 1) {
            ((AllVideosFragment)currentFragment).refreshUsingNewCHID(null);
            tab.setText("All");
        }
        else {
            ((AllVideosFragment)currentFragment).refreshUsingNewCHID(position + 1);
            tab.setText(categoriesData.get(position).split(":")[0]);
        }


    }
}
