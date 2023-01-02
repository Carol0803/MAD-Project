package w2.g16.odds.product_browsing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import w2.g16.odds.R;
import w2.g16.odds.model.Products;

public class SearchPorductAdapter extends ArrayAdapter<Products> {

    public SearchPorductAdapter(Context context, int resource, List<Products> productsList)
    {
        super(context,resource,productsList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Products product = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.tvItemSearch);
        tv.setText(product.getProduct_name());

        return convertView;
    }
}
