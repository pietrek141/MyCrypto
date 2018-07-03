package piotrmroczkowski.mycrypto;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MyCoinCursorAdapter extends CursorAdapter {
    public MyCoinCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    MyCoinRepo myCoinRepo = new MyCoinRepo(MyApplication.getAppContext());
    TextView percent;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.listview_mycoin_row, parent, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final Cursor c = cursor;

        ImageButton deleteCryptoButton = (ImageButton) view.findViewById(R.id.button_delete_from_MyCrypto);

        TextView coinName = (TextView) view.findViewById(R.id.textview_myCoinList_row_coinSymbol);
        TextView coinPrice = (TextView) view.findViewById(R.id.textview_myCoinList_row_coinPrice);
        percent = (TextView) view.findViewById(R.id.textview_myCoinList_row_percent);

        String symbol = c.getString(cursor.getColumnIndex("SYMBOL"));
        String lastPrice = c.getString(cursor.getColumnIndex("LAST_PRICE"));
        String buyPrice = c.getString(cursor.getColumnIndex("BUY_PRICE"));

        coinName.setText(symbol);
        coinPrice.setText(lastPrice);
        if (buyPrice != null && lastPrice != null)
            percent.setText(calculatePercent(buyPrice, lastPrice));

        deleteCryptoButton.setOnClickListener(new View.OnClickListener() {

            private String coinSymbol = c.getString(c.getColumnIndex("SYMBOL"));

            @Override
            public void onClick(View v) {
                Log.d("MyCursorAdapter", "Deleted: " + coinSymbol);
                Log.d("Some stuff", "bla: " + coinSymbol);
                Log.d("Some pull request", "bla: " + coinSymbol);

                myCoinRepo.deleteFromMyCoinByName(coinSymbol);

                StartActivity.updateMyCrypto();
            }
        });


    }

    String calculatePercent(String buyPrice, String lastPrice) {

        buyPrice.replace(",", ".");
        Double percentValue = (Double.parseDouble(lastPrice) / Double.parseDouble(buyPrice)) - 1;

        DecimalFormat df2 = new DecimalFormat("#.##");

        String result = df2.format(percentValue);
        if (Double.parseDouble(result) >= 0) {
            result = "+" + result + "%";
            percent.setTextColor(Color.GREEN);
        } else {
            result = "-" + result + "%";
            percent.setTextColor(Color.RED);
        }

        return result;
    }
}
