package com.smartcity.getitdoneandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.smartcity.getitdoneandroid.complaint.AddComplaintScreen;
import com.smartcity.getitdoneandroid.complaint.ComplaintList;
import com.smartcity.getitdoneandroid.complaint.MyItemRecyclerViewAdapter;
import com.smartcity.getitdoneandroid.complaint.dummy.DummyContent;
import com.smartcity.getitdoneandroid.helpers.CallWebService;
import com.smartcity.getitdoneandroid.helpers.ComplexPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private  Toolbar toolbar;
    private TextView txtUserName;
    private TextView textViewEmail;
    private ImageView imageView;
    private NavigationView navigationView;
    private RecyclerView recyclerComplaints;
    private ArrayList<ComplaintList> complaints;
    private AppUser appUser;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Get It Done");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveToAddComplaintScreen = new Intent(DrawerActivity.this, AddComplaintScreen.class);
                startActivity(moveToAddComplaintScreen);

            }
        });

        fab.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_complaint);

        recyclerComplaints = (RecyclerView)findViewById(R.id.recyclerComplaints);
        recyclerComplaints.setLayoutManager(new LinearLayoutManager(this));
        recyclerComplaints.addItemDecoration(new VerticalSpaceItemDecoration(8));

        fetchCurrentUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchComplaints();
    }

    private void fetchComplaints() {


       final ProgressDialog pd = ProgressDialog.show(DrawerActivity.this,"Please wait","Loading..",true);

        new CallWebService("http://getitdonee.azurewebsites.net/post?facebook_user_id="+appUser.getFbid(), CallWebService.TYPE_JSONARRAY) {
            @Override
            public void response(String response) {

                pd.dismiss();
                Log.e("Response from net",response);

                Type listType = new TypeToken<List<ComplaintList>>() {}.getType();

                complaints = new GsonBuilder().create().fromJson(response,listType);

                myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(getBaseContext(), complaints);
                recyclerComplaints.setAdapter(myItemRecyclerViewAdapter);
                myItemRecyclerViewAdapter.filterResolved(false);
            }

            @Override
            public void error(VolleyError error) {
                pd.dismiss();
            }
        }.start();




    }

    private void fetchCurrentUser() {

        ComplexPreferences preferences = ComplexPreferences.getComplexPreferences(DrawerActivity.this,"userpref",MODE_PRIVATE);
        appUser = preferences.getObject("appuser",AppUser.class);

        if(appUser != null){

            txtUserName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.textView);
            textViewEmail = (TextView)navigationView.getHeaderView(0).findViewById(R.id.textViewEmail);
            txtUserName.setText(appUser.getFullName());
            textViewEmail.setText(appUser.getEmail());
            imageView = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.imageView);



            String profilepic = "https://graph.facebook.com/"+appUser.getFbid()+"/picture?type=small";
            //Glide.with(DrawerActivity.this).load(profilepic).asBitmap().into(imageView);

            Glide.with(DrawerActivity.this).load(profilepic).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_complaint){
            myItemRecyclerViewAdapter.filterResolved(false);
        }else if(id == R.id.nav_resolved){
            myItemRecyclerViewAdapter.filterResolved(true);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }
}
