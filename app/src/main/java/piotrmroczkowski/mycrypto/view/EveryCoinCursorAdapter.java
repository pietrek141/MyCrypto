package piotrmroczkowski.mycrypto.view;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import me.grantland.widget.AutofitTextView;
import piotrmroczkowski.mycrypto.R;
import piotrmroczkowski.mycrypto.app.MyApplication;
import piotrmroczkowski.mycrypto.repository.MyCoinRepo;


public class EveryCoinCursorAdapter extends CursorAdapter {
    public EveryCoinCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    String coinPrice;
    MyCoinRepo myCoinRepo = new MyCoinRepo();
    ViewManager viewManager = new ViewManager();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        if (position % 2 == 1) {
            view.setBackgroundColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.Lime100));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.Lime200));
        }

        return view;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d("EveryCoinCursorAdapter", "calling new view here");
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listview_everycoin_row, parent, false);
        holder.coinName = view.findViewById(R.id.autoFitTextView_list_row_coinName);
        holder.coinSymbol = view.findViewById(R.id.textview_list_row_coinSymbol);
        holder.addCryptoButton = view.findViewById(R.id.button_add_to_MyCrypto);
        holder.buyPriceInput = view.findViewById(R.id.editText_buyPrice);
        holder.buyPriceInput = view.findViewById(R.id.editText_buyPrice);
        view.setTag(holder);

        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d("EveryCoinCursorAdapter", "calling bind view here");
        final Cursor c = cursor;
        final ViewHolder holder = (ViewHolder) view.getTag();

        String name = cursor.getString(cursor.getColumnIndex("NAME"));
        String symbol = cursor.getString(cursor.getColumnIndex("SYMBOL"));

        holder.buyPriceInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                coinPrice = holder.buyPriceInput.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                coinPrice = holder.buyPriceInput.getText().toString();
            }
        });

        holder.coinName.setText(name);
        holder.coinSymbol.setText(symbol);

        holder.addCryptoButton.setOnClickListener(new View.OnClickListener() {

            private String coinSymbol = c.getString(c.getColumnIndex("SYMBOL"));
            private String coinName = c.getString(c.getColumnIndex("NAME"));

            @Override
            public void onClick(View v) {
                Log.d("MyCursorAdapter", "Added: " + coinSymbol);

                coinPrice = holder.buyPriceInput.getText().toString();
                myCoinRepo.insertToMyCoinByName(coinName, coinSymbol, coinPrice);
                ViewManager.updateMyCrypto();
                ViewManager.everyCoinListView.setVisibility(View.GONE);
                ViewManager.myCoinListView.setVisibility(View.VISIBLE);
                holder.buyPriceInput.getText().clear();
                holder.buyPriceInput.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }

        });

    }

    class ViewHolder {
        AutofitTextView coinName;
        TextView coinSymbol;
        EditText buyPriceInput;
        ImageButton addCryptoButton;
    }
}

