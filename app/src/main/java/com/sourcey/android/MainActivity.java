package com.sourcey.android;

import java.util.ArrayList;
import java.util.Collections;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sourcey.android.entity.CategoryDto;

import com.sourcey.android.entity.Pager;
import com.sourcey.android.utility.Constants;
import com.sourcey.android.utility.EndPointApi;
import com.sourcey.android.utility.Utility;

import java.util.List;
import java.util.Map;

import butterknife.Bind;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @Bind(R.id.spn_category) Spinner _spnCategory;
    @Bind(R.id.spn_subcategory) Spinner _spnSubCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Utility.Bearer.isEmpty()){
            goToActiviry(LoginActivity.class,null);
        }else{
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            if(Utility.categoryDtoList.size() == 0) getCategory();
            initializeData();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void initializeData() {
        try {
            List<String> list = new ArrayList<String>();
            Utility.categoryDtoList.forEach(w-> list.add(w.getCategoryCode()));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            _spnCategory.setAdapter(dataAdapter);
        }catch(Exception e){
            Utility.notify(getBaseContext(),e.getMessage());
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

        if (id == R.id.nav_dashboard) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_user) {

        } else if (id == R.id.nav_Setting) {

        } else if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_Logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToActiviry(Class<?> cls, Map<String,String> maps){
        final Intent intent = new Intent(getApplicationContext(), cls);
        if(maps!=null)
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                intent.putExtra(entry.getKey(),entry.getValue());
            }
        startActivityForResult(intent, Constants.ZERO);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private boolean getCategory(){
        try{
            Pager pager = Utility.restTemplate().getForObject(EndPointApi.CATEGORY.getLink(), Pager.class);
            return true;
        }catch (Exception e){
            Utility.notify(getBaseContext(), e.getMessage());
            return false;
        }

    }

}
