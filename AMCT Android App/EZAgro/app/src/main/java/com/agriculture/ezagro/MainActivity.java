package com.agriculture.ezagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar Toolbar;
    private TabLayout TabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(Toolbar);

        viewPager = (ViewPager2) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout = (TabLayout) findViewById(R.id.tabs);
        TabLayoutMediator tabLayoutMediator= new TabLayoutMediator(TabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull com.google.android.material.tabs.TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 0:
                        tab.setText("HOME");
                        break;
                    case 1:
                        tab.setText("CONTROL");
                        break;
                    case 2:
                        tab.setText("ALERTS");
                        /*
                        BadgeDrawable badgeDrawable=tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.black)
                        );

                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(50);
                        badgeDrawable.setMaxCharacterCount(3);
                        */
                        break;
                    case 3:
                        tab.setText("ANALYSIS");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BadgeDrawable badgeDrawable=TabLayout.getTabAt(position).getOrCreateBadge();
                badgeDrawable.setVisible(false);
            }
        });
    }


    private void setupViewPager(androidx.viewpager2.widget.ViewPager2 viewPager) {
        viewPager.setAdapter(new FagmentPageAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater=getMenuInflater();
        Inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.settings){
            startActivity(new Intent(MainActivity.this, Settings.class));
        }
        return super.onOptionsItemSelected(item);
    }

}