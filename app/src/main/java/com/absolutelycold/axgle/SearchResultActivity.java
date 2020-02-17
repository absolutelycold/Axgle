package com.absolutelycold.axgle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity implements OrderDialogFragment.Listener, CategoryListDialogFragment.Listener{

    String searchContent;
    FrameLayout frameLayout;
    MenuItem searchIcon;
    MenuItem backIcon;
    AllVideosFragment searchResultFragment;
    private static final int CONTENT_VIEW_ID = 5200;
    private ArrayList<String> categoriesData = null;
    private ArrayList<String> categoriesDataWithoutNum = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();

        searchContent = intent.getStringExtra("search_content");
        categoriesData = intent.getStringArrayListExtra("categories_data");

        if (categoriesData != null) {
            categoriesDataWithoutNum = new ArrayList<>();
            for (String singleCategory : categoriesData) {
                categoriesDataWithoutNum.add(singleCategory.split(":")[0]);
            }
        }

        //System.out.println("SearchResultActivity: " + searchContent);
        setTitle(":" + searchContent);
        searchResultFragment = AllVideosFragment.newInstance(AllVideosFragment.FRAGMENT_SEARCH, searchContent);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.search_result_container, searchResultFragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action_bar, menu);
        searchIcon = menu.findItem(R.id.action_search);
        searchIcon.setVisible(false);
        //menu.findItem(R.id.action_category).setVisible(false);
        if (categoriesData == null) {
            menu.findItem(R.id.action_category).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_all_tab:
                searchResultFragment.refreshAll();
                break;
            case R.id.action_sort_all:
                ArrayList<String> order_options = new ArrayList<>();
                order_options.add("Latest");
                order_options.add("Most views");
                order_options.add("Top rated");
                order_options.add("Most fav");
                OrderDialogFragment.newInstance(order_options).show(getSupportFragmentManager(), OrderDialogFragment.TAG);
                break;
            case R.id.action_category:
                if (categoriesDataWithoutNum == null) {
                    CategoryListDialogFragment.newInstance(null).show(getSupportFragmentManager(), CategoryListDialogFragment.TAG);
                }
                else {
                    CategoryListDialogFragment.newInstance(categoriesDataWithoutNum).show(getSupportFragmentManager(), CategoryListDialogFragment.TAG);
                }
                break;
            case R.id.action_back:
                finish();
                break;
            case R.id.give_me_a_star:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/absolutelycold/axgle"));
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSortOptionSelected(int position) {
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
        searchResultFragment.refreshUsingNewOrder(order);
    }

    @Override
    public void onCategoryClicked(int position) {
        if (position == categoriesData.size() - 1) {
            searchResultFragment.refreshUsingNewCHID(null);
        }
        else {
            searchResultFragment.refreshUsingNewCHID(position + 1);
        }
    }
}
