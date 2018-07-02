package piotrmroczkowski.mycrypto;

import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import static piotrmroczkowski.mycrypto.MyApplication.getAppContext;


public class EveryCoinCursorAdapter extends CursorAdapter {
    public EveryCoinCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /*    String[] columns = {"NAME", "SYMBOL"};
        int[] resourceIds = {R.id.textview_list_row_coinName, R.id.textview_list_row_coinSymbol};
        ImageButton addCryptoButton;*/
    String coinPrice;
    MyCoinRepo myCoinRepo = new MyCoinRepo(getAppContext());

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d("EveryCoinCursorAdapter", "calling new view here");
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listview_everycoin_row, parent, false );
        holder.coinName = (TextView) view.findViewById(R.id.textview_list_row_coinName);
        holder.coinSymbol = (TextView) view.findViewById(R.id.textview_list_row_coinSymbol);
        holder.addCryptoButton =  (ImageButton) view.findViewById(R.id.button_add_to_MyCrypto);
        holder.buyPriceInput = (EditText) view.findViewById(R.id.editText_buyPrice);
/*        holder.buyPriceInput.setEnabled(true);
        holder.buyPriceInput.setFocusable(true);
        holder.buyPriceInput.setFocusableInTouchMode(true);
        holder.buyPriceInput.setClickable(true);
        holder.buyPriceInput.setCursorVisible(true);
        holder.buyPriceInput.setPressed(true);
        holder.buyPriceInput.setTextIsSelectable(true);
        holder.buyPriceInput.setInputType(InputType.TYPE_NULL);*/
        view.setTag(holder);

        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d("EveryCoinCursorAdapter", "calling bind view here");
        final Cursor c = cursor;
        final ViewHolder holder = (ViewHolder)view.getTag();
/*        ImageButton addCryptoButton = (ImageButton) view.findViewById(R.id.button_add_to_MyCrypto);
        final EditText buyPriceInput = (EditText) view.findViewById(R.id.editText_buyPrice);
        TextView coinName = (TextView) view.findViewById(R.id.textview_list_row_coinName);
        TextView coinSymbol = (TextView) view.findViewById(R.id.textview_list_row_coinSymbol);*/

        String name = cursor.getString(cursor.getColumnIndex("NAME"));
        String symbol = cursor.getString(cursor.getColumnIndex("SYMBOL"));
        holder.buyPriceInput = (EditText) view.findViewById(R.id.editText_buyPrice);
/*        holder.buyPriceInput.setEnabled(true);
        holder.buyPriceInput.setFocusable(true);
        holder.buyPriceInput.setFocusableInTouchMode(true);
        holder.buyPriceInput.setClickable(true);
        holder.buyPriceInput.setCursorVisible(true);
        holder.buyPriceInput.setPressed(true);*/
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
/*        holder.buyPriceInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {


            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    holder.buyPriceInput.setText("");
                }
                else if(hasFocus){ }



            }
        });*/
        holder.coinName.setText(name);
        holder.coinSymbol.setText(symbol);
/*        coinName.setText(name);
        coinSymbol.setText(symbol);*/

        holder.addCryptoButton.setOnClickListener(new View.OnClickListener() {

            private String coinSymbol = c.getString(c.getColumnIndex("SYMBOL"));

            @Override
            public void onClick(View v) {
                Log.d("MyCursorAdapter", "Added: " + coinSymbol);

                coinPrice = holder.buyPriceInput.getText().toString();
                myCoinRepo.insertToMyCoinByName(coinSymbol, coinPrice);
                StartActivity.updateMyCrypto();
            }

        });


    }





    class ViewHolder{
        TextView coinName;
        TextView coinSymbol;
        EditText buyPriceInput;
        ImageButton addCryptoButton;
    }
}

