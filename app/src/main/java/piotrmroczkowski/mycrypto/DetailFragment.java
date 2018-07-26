package piotrmroczkowski.mycrypto;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static piotrmroczkowski.mycrypto.ViewManager.myCoinRepo;


public class DetailFragment extends Fragment {

    private static final String ARG_SYMBOL = "ARG SYMBOL";


    public DetailFragment() {
    }

    public static DetailFragment newFragment(String symbol) {


        DetailFragment fragment = new DetailFragment();

        Bundle args = new Bundle();
        args.putString(ARG_SYMBOL, symbol);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewHolder holder = new ViewHolder();
        final View view = inflater.inflate(R.layout.fragment_detail, container, false);

        final Bundle args = getArguments();
        final String symbol = " (" + args.getString(ARG_SYMBOL) + ")";

        TextView detailSymbol = view.findViewById(R.id.textview_detail_symbol);
        holder.coinNameDetail = view.findViewById(R.id.textview_detail_name);
        holder.lastPriceDetail = view.findViewById(R.id.textview_detail_LastPrice_value);
        holder.buyPriceDetail = view.findViewById(R.id.textview_detail_BuyPrice_value);
        holder.moneyDetail = view.findViewById(R.id.textview_detail_Money);
        holder.amountInputDetail = view.findViewById(R.id.editText_detail_Amount_Input_value);
        holder.percentDetail = view.findViewById(R.id.textview_detail_Percent_value);
        holder.addAmount = view.findViewById(R.id.button_add_amount);


        final Cursor cursor = ViewManager.initMyCryptoDetailView(args.getString(ARG_SYMBOL));

        updateView(holder, cursor);
        detailSymbol.setText(symbol);

        holder.addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DetailFragment", "AddedAmount: " + holder.amountInputDetail.getText().toString());

                String amountInput = holder.amountInputDetail.getText().toString();
                try {
                    if (amountInput.equals("")) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MyApplication.getAppContext(), "Buy price is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                myCoinRepo.updateAmountInMyCoinBySymbol(Integer.valueOf(amountInput), args.getString(ARG_SYMBOL));
                updateView(holder, ViewManager.initMyCryptoDetailView(args.getString(ARG_SYMBOL)));
                holder.amountInputDetail.getText().clear();
                holder.amountInputDetail.onEditorAction(EditorInfo.IME_ACTION_DONE);
                ViewManager.updateMyCrypto();
            }
        });

        return view;
    }

    public void updateView(ViewHolder holder, Cursor cursor) {

        final Cursor c = cursor;
        //final ViewHolder holder = (ViewHolder) view.getTag();

        String name = c.getString(cursor.getColumnIndex("NAME"));
        String lastPrice = c.getString(cursor.getColumnIndex("LAST_PRICE"));
        String buyPrice = c.getString(cursor.getColumnIndex("BUY_PRICE"));
        int amount = c.getInt(cursor.getColumnIndex("AMOUNT"));

        holder.coinNameDetail.setText(name);
        holder.lastPriceDetail.setText(String.format("$%s", lastPrice));
        holder.buyPriceDetail.setText(String.format("$%s", buyPrice));

        if (buyPrice != null || buyPrice.equals("") && lastPrice != null || lastPrice.equals(""))
            holder.percentDetail.setText(calculatePercent(holder, buyPrice, lastPrice));
        if (lastPrice != null && Integer.valueOf(amount) != null)
            holder.moneyDetail.setText(String.format("$%s", calculateMoney(lastPrice, amount)));
    }

    static String calculateMoney(String buyPrice, int amount) {

        BigDecimal amountBigDecimal = new BigDecimal(amount);
        BigDecimal buyPriceBigDecimal = new BigDecimal(buyPrice);

        DecimalFormat df2 = new DecimalFormat("#.##");

        BigDecimal result = amountBigDecimal.multiply(buyPriceBigDecimal);
        String resultToShow = df2.format(result);

        return resultToShow;
    }

    static String calculatePercent(ViewHolder holder, String buyPrice, String lastPrice) {
        Double percentValue = 0.0;
        try {
            buyPrice.replace(",", ".");
            percentValue = (Double.parseDouble(lastPrice) / Double.parseDouble(buyPrice)) - 1;
        } catch (Exception e) {
            Log.d("CALCULATE PERCENT", "Buy Price is empty");
        }

        percentValue = percentValue * 100;
        DecimalFormat df2 = new DecimalFormat("#.##");

        String result = df2.format(percentValue);
        if (Double.parseDouble(result) >= 0) {
            result = "+" + result + "%";
            holder.percentDetail.setTextColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.Green800));
        } else {
            result = result + "%";
            holder.percentDetail.setTextColor(Color.RED);
        }

        return result;
    }

    class ViewHolder {
        TextView coinNameDetail;
        TextView lastPriceDetail;
        TextView buyPriceDetail;
        TextView moneyDetail;
        EditText amountInputDetail;
        TextView percentDetail;
        ImageButton addAmount;
    }

}



/*    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_detail, parent, false);
        MyCoinDetailCursorAdapter.ViewHolder holder = new MyCoinDetailCursorAdapter.ViewHolder();

        holder.coinNameDetail = view.findViewById(R.id.textview_detail_name);
        holder.coinSymbolDetail = view.findViewById(R.id.textview_detail_symbol);
        holder.lastPriceDetail = view.findViewById(R.id.textview_detail_LastPrice_value);
        holder.buyPriceDetail = view.findViewById(R.id.textview_detail_BuyPrice_value);
        holder.moneyDetail = view.findViewById(R.id.textview_detail_Money);
        holder.amountInputDetail = view.findViewById(R.id.editText_detail_Amount_Input_value);
        percentDetail = view.findViewById(R.id.textview_detail_Percent);



        return view;
    }*/

/*    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final Cursor c = cursor;
        final MyCoinDetailCursorAdapter.ViewHolder holder = (MyCoinDetailCursorAdapter.ViewHolder) view.getTag();

        String name = c.getString(cursor.getColumnIndex("NAME"));
        String symbol = c.getString(cursor.getColumnIndex("SYMBOL"));
        String lastPrice = c.getString(cursor.getColumnIndex("LAST_PRICE"));
        String buyPrice = c.getString(cursor.getColumnIndex("BUY_PRICE"));
        int amount = c.getInt(cursor.getColumnIndex("AMOUNT"));

        holder.coinNameDetail.setText(name);
        holder.coinSymbolDetail.setText(symbol);
        holder.lastPriceDetail.setText(lastPrice);
        holder.buyPriceDetail.setText(buyPrice);

        if (buyPrice != null || buyPrice.equals("") && lastPrice != null || lastPrice.equals(""))
            percentDetail.setText(calculatePercent(buyPrice, lastPrice));
        if (buyPrice != null || buyPrice.equals("") && Integer.valueOf(amount) != null ||  Integer.valueOf(amount).equals(""))
            holder.moneyDetail.setText(String.valueOf(calculateMoney(buyPrice, amount)));
    }*/

/*    private BigDecimal calculateMoney(String buyPrice, int amount) {


        BigDecimal amountBigDecimal = new BigDecimal(amount);
        BigDecimal buyPriceBigDecimal = new BigDecimal(buyPrice);

        return amountBigDecimal.multiply(buyPriceBigDecimal);

    }

    String calculatePercent(String buyPrice, String lastPrice) {
        Double percentValue = 0.0;
        try {
            buyPrice.replace(",", ".");
            percentValue = (Double.parseDouble(lastPrice) / Double.parseDouble(buyPrice)) - 1;
        } catch (Exception e) {
            Log.d("CALCULATE PERCENT", "Buy Price is empty");
        }

        percentValue = percentValue * 100;
        DecimalFormat df2 = new DecimalFormat("#.##");

        String result = df2.format(percentValue);
        if (Double.parseDouble(result) >= 0) {
            result = "+" + result + "%";
            percentDetail.setTextColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.Green800));
        } else {
            result = result + "%";
            percentDetail.setTextColor(Color.RED);
        }

        return result;
    }*/

/*class ViewHolder {
    TextView coinNameDetail;
    TextView coinSymbolDetail;
    TextView lastPriceDetail;
    TextView buyPriceDetail;
    TextView moneyDetail;
    EditText amountInputDetail;
}*/


