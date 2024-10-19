package com.example.viber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setSubtitle("Welcome, Usman");
       // getSupportActionBar().setSubtitle("VIBER");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00dddd")));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#515151'>VIBER</font>"));
       // getSupportActionBar().setIcon(R.drawable.baseline_person_24);

        //set adapter to the ViewPager
        ViewPager2 viewpage1=findViewById(R.id.viewpagerhome);
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        viewpage1.setAdapter(adapter);

        //scroll tabs onscrolling of viewpager

        TabLayout tabs=findViewById(R.id.tablayouthome);
     viewpage1.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
         @Override
         public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
             super.onPageScrolled(position, positionOffset, positionOffsetPixels);
             tabs.selectTab(tabs.getTabAt(position));
         }
     });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.optionmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.Logout)
        {
            //statement executes on click of input optionment
            FirebaseAuth.getInstance().signOut();
            Intent in=new Intent(this,MainActivity.class);
            startActivity(in);
        } else if (item.getItemId()==R.id.Setting)
        {
         //statement executes on click of setting optionment
        }

        return super.onOptionsItemSelected(item);
    }
}

//Create an adapter for ViewPager

class ViewPagerAdapter extends FragmentStateAdapter
{

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0)
            return new ChatFragment();
        else if (position==1)
            return new StoryFragment();
        else
            return new ProfileFragment();
    }

    @Override
    public int getItemCount() {

        return 3;
    }
}