package com.absolutelycold.axgle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OrderDialogFragment.Listener{

    private ViewPager viewPager;
    private Fragment currentFragment;
    private MenuItem refreshAllItem;
    private MenuItem sortAllItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("All")) {
                    //System.out.println("Change to All tab");
                    refreshAllItem.setVisible(true);
                    sortAllItem.setVisible(true);
                }
                else {
                    refreshAllItem.setVisible(false);
                    sortAllItem.setVisible(false);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action_bar, menu);
        sortAllItem = menu.findItem(R.id.action_sort_all);
        refreshAllItem = menu.findItem(R.id.refresh_all_tab);
        refreshAllItem.setVisible(false);
        sortAllItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        currentFragment = ((TabsPagerAdapter)viewPager.getAdapter()).getCurrentFragment();
        switch (item.getItemId()) {
            case R.id.refresh_all_tab:
                if (currentFragment instanceof AllVideosFragment) {
                    ((AllVideosFragment)currentFragment).getRecyclerView().scrollToPosition(0);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(int position) {
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
}
