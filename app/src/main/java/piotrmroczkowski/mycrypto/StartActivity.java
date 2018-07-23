package piotrmroczkowski.mycrypto;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


public class StartActivity extends AppCompatActivity implements MasterFragment.ItemClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        MasterFragment fragment = new MasterFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame_layout_master, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void itemClicked(String symbol) {

        Intent intent = DetailActivity.newIntent(this, symbol);
        startActivity(intent);
    }
}
