package piotrmroczkowski.mycrypto;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class MyCoinDetailCursorAdapter extends CursorAdapter {
    public MyCoinDetailCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    MyCoinRepo myCoinRepo = new MyCoinRepo();
    TextView percentDetail;

/*  @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        if (position % 2 == 1) {
            view.setBackgroundColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.brown100));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.brown200));
        }

        return view;
    }*/

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_detail, parent, false);
        ViewHolder holder = new ViewHolder();

        holder.coinNameDetail = view.findViewById(R.id.textview_detail_name);
        holder.coinSymbolDetail = view.findViewById(R.id.textview_detail_symbol);
        holder.lastPriceDetail = view.findViewById(R.id.textview_detail_LastPrice_value);
        holder.buyPriceDetail = view.findViewById(R.id.textview_detail_BuyPrice_value);
        holder.moneyDetail = view.findViewById(R.id.textview_detail_Money);
        holder.amountInputDetail = view.findViewById(R.id.editText_detail_Amount_Input_value);
        percentDetail = view.findViewById(R.id.textview_detail_Percent);


        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final Cursor c = cursor;
        final ViewHolder holder = (ViewHolder) view.getTag();

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
        if (buyPrice != null || buyPrice.equals("") && Integer.valueOf(amount) != null || Integer.valueOf(amount).equals(""))
            holder.moneyDetail.setText(String.valueOf(calculateMoney(buyPrice, amount)));
    }

    private BigDecimal calculateMoney(String buyPrice, int amount) {


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
    }

    class ViewHolder {
        TextView coinNameDetail;
        TextView coinSymbolDetail;
        TextView lastPriceDetail;
        TextView buyPriceDetail;
        TextView moneyDetail;
        EditText amountInputDetail;
    }
}
