package com.example.bedtime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.bedtime.Adapters.CategoriesAdapter;
import com.example.bedtime.Adapters.StoryListingAdapter;
import com.example.bedtime.Api.ApiInterface;
import com.example.bedtime.Api.Client;
import com.example.bedtime.Api.Responses.CategoryAllResponse;
import com.example.bedtime.Api.Responses.StoryAllResponse;
import com.example.bedtime.Database.Helper.BedTimeDbHelper;
import com.example.bedtime.Model.Category;
import com.example.bedtime.Model.Story;
import com.example.bedtime.Model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView mStoriesRecycler,mCategoriesRecycler;
    StoryListingAdapter mAdapter;
    List<Story> mStories;
    List<Category> mCategories;
    CategoriesAdapter mCategoriesAdapter;
    User mUser;
    ImageView mAddNew;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        getSupportActionBar().setElevation(0);
        Intent intent = getIntent();
        if(intent.hasExtra(Config.USER_ID)){
            String id = intent.getStringExtra(Config.USER_ID);
            if(!id.isEmpty()){
                BedTimeDbHelper dbHelper = new BedTimeDbHelper(this);
                mUser = dbHelper.getUserById(id);
            }
        }
        mStoriesRecycler = findViewById(R.id.stories_rv);
        mCategoriesRecycler = findViewById(R.id.cat_rv);
        mProgressBar = findViewById(R.id.progressBar);
        mAddNew = findViewById(R.id.btn_addnew);
        mAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Home.this,AddStoryActivity.class);
                startActivity(intent1);
            }
        });
        mStories = new ArrayList<>();
        mCategories = new ArrayList<>();
        mCategoriesAdapter = new CategoriesAdapter(this,mCategories,"home");
        mCategoriesRecycler.setAdapter(mCategoriesAdapter);
        mAdapter = new StoryListingAdapter(this,mStories);
        mStoriesRecycler.setLayoutManager(new LinearLayoutManager(this));
        mStoriesRecycler.setAdapter(mAdapter);

        loadData();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
//                drawer.openDrawer(Gravity.START);
                return super.onOptionsItemSelected(item);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ImageButton close = navigationView.getHeaderView(0).findViewById(R.id.nav_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);


    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_bookmarks) {

        } else if (id == R.id.nav_categories) {

            //start category activity .
            Intent i = new Intent(getBaseContext(), CategoriesActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_bookmarks) {

        } else if (id == R.id.nav_profile) {

            //start Profile activity .
            if(mUser !=null ){
                Intent i = new Intent(getBaseContext(), ProfileActivity.class);
                i.putExtra(Config.USER_ID,mUser.getId());
                startActivity(i);
            }

        }
//        else if (id == R.id.nav_login) {
//            //start Login activity .
//            Intent i = new Intent(getBaseContext(), Login.class);
//            startActivity(i);
//
//        }
            else if (id == R.id.nav_addstory) {

            //start addstory activity .
            Intent i = new Intent(getBaseContext(), AddStoryActivity.class);
            startActivity(i);

        }

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadData(){
        mProgressBar.setVisibility(View.VISIBLE);
        Client.getInstance().create(ApiInterface.class).getAllCategories().enqueue(new Callback<CategoryAllResponse>() {
            @Override
            public void onResponse(Call<CategoryAllResponse> call, Response<CategoryAllResponse> response) {
                if(response.isSuccessful()){
                    CategoryAllResponse  categoryList = response.body();
                    List<Category> cl= categoryList.getData();
                    if(cl !=null){
                        mCategoriesAdapter.addCategories(cl);
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryAllResponse> call, Throwable t) {
                showNetworkError();
            }
        });
        Client.getInstance().create(ApiInterface.class).getAllStories().enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                if(response.isSuccessful()){
                    StoryAllResponse  storyAllResponse = response.body();
                    List<Story> story = storyAllResponse.getData().getStories();
                    if(story !=null){
                        mAdapter.addStories(story);


                    }
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
                Log.e("Story = " , t.toString());
                showNetworkError();
            }
        });
    }
    public void showNetworkError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Unable to connect")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadData();
                    }
                });
        builder.create().show();

    }
}

