package com.example.retrofitassignment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    boolean isBought;
    ListMarsFragment listMarsFragment = ListMarsFragment.newInstance(isBought);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, listMarsFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rent_mar:
                isBought = false;
                listMarsFragment.changeMarsList(isBought);
                return true;
            case R.id.buy_mar:
                isBought = true;
                listMarsFragment.changeMarsList(isBought);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void displayListFragment() {
//        Fragment currentFragment = getSupportFragmentManager()
//                .findFragmentById(R.id.fragmentContainer);
//        if(currentFragment==null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.fragmentContainer, listMarsFragment)
//                    .commit();
//        } else{
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragmentContainer, ListMarsFragment.newInstance(isBought))
//                    .commit();
//        }

}

