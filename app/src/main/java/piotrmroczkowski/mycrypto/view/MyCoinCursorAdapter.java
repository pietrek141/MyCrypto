package piotrmroczkowski.mycrypto.view;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import piotrmroczkowski.mycrypto.R;
import piotrmroczkowski.mycrypto.app.MyApplication;
import piotrmroczkowski.mycrypto.fragments.MasterFragment;
import piotrmroczkowski.mycrypto.repository.MyCoinRepo;


public class MyCoinCursorAdapter extends CursorAdapter {
    public MyCoinCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    MyCoinRepo myCoinRepo = new MyCoinRepo();
    static TextView percent;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        if (position % 2 == 1) {
            view.setBackgroundColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.brown100));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.brown200));
        }

        return view;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_mycoin_row, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.deleteCryptoButton = view.findViewById(R.id.button_delete_from_MyCrypto);
        holder.coinName = view.findViewById(R.id.textview_myCoinList_row_coinSymbol);
        holder.coinPrice = view.findViewById(R.id.textview_myCoinList_row_coinPrice);
        percent = view.findViewById(R.id.textview_myCoinList_row_percent);
        holder.walletEye = view.findViewById(R.id.imageSwitcherWalletEye);
        holder.walletEye.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(MyApplication.getAppContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;
            }
        });

        holder.myCoinRow = view.findViewById(R.id.myCoinRowLayout);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final Cursor c = cursor;
        final ViewHolder holder = (ViewHolder) view.getTag();

        String symbol = c.getString(cursor.getColumnIndex("SYMBOL"));
        String lastPrice = c.getString(cursor.getColumnIndex("LAST_PRICE"));
        String buyPrice = c.getString(cursor.getColumnIndex("BUY_PRICE"));
        String amount = c.getString(cursor.getColumnIndex("AMOUNT"));

        holder.coinName.setText(symbol);

        if (buyPrice != null || buyPrice.equals("0") && lastPrice != null || lastPrice.equals("0"))
            percent.setText(ViewManager.calculatePercent(buyPrice, lastPrice, percent));

        holder.coinPrice.setText(String.format("$%s", lastPrice));

        if (amount != null)
            holder.walletEye.setImageResource(R.drawable.wallet);
        else
            holder.walletEye.setImageResource(R.drawable.eye);

        holder.deleteCryptoButton.setOnClickListener(new View.OnClickListener() {

            private String coinSymbol = c.getString(c.getColumnIndex("SYMBOL"));

            @Override
            public void onClick(View v) {
                Log.d("MyCursorAdapter", "Deleted: " + coinSymbol);
                myCoinRepo.deleteFromMyCoinByName(coinSymbol);
                ViewManager.updateMyCrypto();
                MasterFragment.dListener.deleteClicked();
            }
        });

        holder.myCoinRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = v.findViewById(R.id.textview_myCoinList_row_coinSymbol);
                Log.d("bla2", String.valueOf(tv.getText()));
                MasterFragment.mListener.itemClicked(String.valueOf(tv.getText()));

                //ViewManager.initMyCryptoDetailView();
            }
        });
    }

    class ViewHolder {
        ImageButton deleteCryptoButton;
        TextView coinName;
        TextView coinPrice;
        ImageSwitcher walletEye;
        LinearLayout myCoinRow;
    }


}
