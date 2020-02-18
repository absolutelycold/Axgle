package com.absolutelycold.axgle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OrderDialogFragment.Listener, CategoryListDialogFragment.Listener, NavigationView.OnNavigationItemSelectedListener{

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Fragment currentFragment;
    private MaterialSearchBar materialSearchBar;
    private MenuItem refreshAllItem;
    private MenuItem sortAllItem;
    private MenuItem categoryItem;
    private MenuItem search;
    private ArrayList<String> categoriesData = null;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Switch blurSwitch;
    private Boolean needBlur;
    private CollectionVideosFragment collectionVideosFragment;
    private AllVideosFragment allVideosFragment;
    private Switch timeSwitch;
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //System.out.println("Search Content:" + text);
                String searchContent = text.toString();
                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                intent.putExtra("search_content", searchContent);
                intent.putStringArrayListExtra("categories_data", categoriesData);
                intent.putExtra("needBlur", needBlur);
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        setNeedBlur(true);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager(), needBlur));

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
                    //search.setVisible(true);
                }
                else {
                    refreshAllItem.setVisible(false);
                    sortAllItem.setVisible(false);
                    categoryItem.setVisible(false);
                    //search.setVisible(false);
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

        drawerLayout = findViewById(R.id.main_activity_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView = findViewById(R.id.main_activity_drawer);
        blurSwitch = navigationView.getMenu().findItem(R.id.blur_toggle_button).getActionView().findViewById(R.id.drawer_switch_toggle);
        timeSwitch = navigationView.getMenu().findItem(R.id.show_status_bar_text).getActionView().findViewById(R.id.drawer_switch_toggle);
        navigationView.setNavigationItemSelectedListener(this);

        final SparseArray<Fragment> tabsFragments = ((TabsPagerAdapter)viewPager.getAdapter()).getFragments();
        blurSwitch.setChecked(true);
        blurSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setNeedBlur(b);

                collectionVideosFragment = (CollectionVideosFragment) tabsFragments.get(0);
                allVideosFragment = (AllVideosFragment) tabsFragments.get(1);
                collectionVideosFragment.setNeedBlur(needBlur);
                collectionVideosFragment.refreshCollections();
                allVideosFragment.setNeedBlur(needBlur);
                allVideosFragment.refreshAll();
            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        timeSwitch.setChecked(true);
        timeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                if (!b) {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

        }
        return false;
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
                categoriesData.add("All・全部 : too many");
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
        search = menu.findItem(R.id.action_search);
        menu.findItem(R.id.action_back).setVisible(false);
        refreshAllItem.setVisible(false);
        sortAllItem.setVisible(false);
        categoryItem.setVisible(false);
        //search.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        currentFragment = ((TabsPagerAdapter)viewPager.getAdapter()).getCurrentFragment();
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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
                break;
            case R.id.action_search:
                if (materialSearchBar.getVisibility() == View.GONE) {
                    materialSearchBar.setVisibility(View.VISIBLE);
                }else if (materialSearchBar.getVisibility() == View.VISIBLE) {
                    materialSearchBar.setVisibility(View.GONE);
                }
                break;
            case R.id.give_me_a_star:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/absolutelycold/Axgle"));
                startActivity(intent);
                break;
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
            tab.setText("All・全部");
        }
        else {
            ((AllVideosFragment)currentFragment).refreshUsingNewCHID(position + 1);
            tab.setText(categoriesData.get(position).split(":")[0]);
        }


    }

    public void setNeedBlur(Boolean needBlur) {
        this.needBlur = needBlur;
    }
}
