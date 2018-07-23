package piotrmroczkowski.mycrypto;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_SYMBOL = "EXTRA SYMBOL";

    public static Intent newIntent(Context parent, String symbol) {

        Intent intent = new Intent(parent, DetailActivity.class);

        intent.putExtra(EXTRA_SYMBOL, symbol);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String symbol = intent.getStringExtra(EXTRA_SYMBOL);


        DetailFragment fragment = DetailFragment.newFragment(symbol);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.frame_layout_detail, fragment)
                .commit();
    }
}
