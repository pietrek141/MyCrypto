package piotrmroczkowski.mycrypto;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


public class StartActivity extends AppCompatActivity implements MasterFragment.ItemClickListener, MasterFragment.DeleteClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);
        MasterFragment fragment = new MasterFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame_layout_master, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void itemClicked(String symbol) {
        if (findViewById(R.id.frame_layout_detail) == null) {
            Intent intent = DetailActivity.newIntent(this, symbol);
            startActivity(intent);
        } else {
            DetailFragment fragment = DetailFragment.newFragment(symbol);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.frame_layout_detail, fragment)
                    .commit();
        }
    }


    @Override
    public void deleteClicked() {
        if (findViewById(R.id.frame_layout_detail) != null) {
            DetailFragmentNotChosen fragment = DetailFragmentNotChosen.newFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.frame_layout_detail, fragment)
                    .commit();
        }
    }
}
