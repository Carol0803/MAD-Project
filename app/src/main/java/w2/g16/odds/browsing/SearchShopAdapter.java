package w2.g16.odds.browsing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import w2.g16.odds.R;
import w2.g16.odds.model.Shop;

public class SearchShopAdapter extends ArrayAdapter<Shop> {

    public SearchShopAdapter(Context context, int resource, List<Shop> shopList)
    {
        super(context,resource,shopList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Shop shop = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.tvItemSearch);
        tv.setText(shop.getShopname());

        return convertView;
    }
}
