package com.absolutelycold.axgle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

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
                    System.out.println("Change to All tab");
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
        switch (item.getItemId()) {
            case R.id.refresh_all_tab:
                currentFragment = ((TabsPagerAdapter)viewPager.getAdapter()).getCurrentFragment();
                if (currentFragment instanceof AllVideosFragment) {
                    ((AllVideosFragment)currentFragment).getRecyclerView().scrollToPosition(0);
                    ((AllVideosFragment)currentFragment).refreshAll();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
