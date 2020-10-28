package com.example.uplan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationEncuentros extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View navigationEncuentrosView = inflater.inflate(R.layout.layout_navigation_encuentros, container, false);
        BottomNavigationView navi = navigationEncuentrosView.findViewById(R.id.nav_bar_encuentros);
        navi.setOnNavigationItemSelectedListener(navListener);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container2,new EncuentrosActivity()).commit();

        return navigationEncuentrosView;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_mis_encuentros:
                            selectedFragment = new EncuentrosActivity();
                            break;
                        case R.id.nav_invitaciones:
                            selectedFragment = new InvitationsActivity();
                            break;
                    }
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container2,
                            selectedFragment).commit();
                    return true;
                }
            };

}
