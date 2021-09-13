package com.agriculture.ezagro;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FagmentPageAdapter extends FragmentStateAdapter {

    public FagmentPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new Home_Fragment();
            case 1:
                return new Control_Fragment();
            case 2:
                return new Notification_Fragment();
            default:
                return new Analysis_Fragment();
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
