package w2.g16.odds.ordering;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

import w2.g16.odds.model.Cart;
import w2.g16.odds.R;
import w2.g16.odds.model.Shop;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>  {

    /*private final LayoutInflater layoutInflater;
    private final Vector<Cart> carts;
    //private final RecyclerView recProduct;
    //private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public CartAdapter(LayoutInflater layoutInflater, Vector<Cart> carts) {
        this.layoutInflater = layoutInflater;
        this.carts = carts;
        //this.recProduct = view.findViewById(R.id.recProduct);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(layoutInflater.inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.setShop(carts.get(position));

        //Cart cart = carts.get(position);
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(holder.recProduct.getContext(), LinearLayoutManager.VERTICAL, false);
        //layoutManager.setInitialPrefetchItemCount(cart.getProducts().size());

        //CartChildAdapter cartChildAdapter = new CartChildAdapter(layoutInflater, carts);
        //holder.recProduct.setLayoutManager(layoutManager);
        //holder.recProduct.setAdapter(cartChildAdapter);
        //holder.recProduct.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }*/

    private Activity activity;
    private final Vector<Shop> shops;
    private final Vector<Cart> carts;
    private int selectedPosition = -1;
//    private ArrayList<Integer> selectCheck = new ArrayList<>();

//    private final List<SelectableShop> selectableShops;
//    private boolean isMultiSelectionEnabled = false;
//    CartViewHolder.OnItemSelectedListener listener;

    public CartAdapter(Activity activity, Vector<Shop> shops, Vector<Cart> carts) {
        this.activity = activity;
        this.shops = shops;
        this.carts = carts;

//        this.listener = listener;
//        this.isMultiSelectionEnabled = isMultiSelectionEnabled;
//
//        selectableShops = new ArrayList<>();
//        for (Shop shop : shops) {
//            selectableShops.add(new SelectableShop(shop, false));
//        }

//        for (int i = 0; i < shops.size(); i++) {
//            selectCheck.add(0);
//        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Shop shop = shops.get(position);

        holder.tvShopname.setText(shop.getShopname());

        CartChildAdapter cartChildAdapter = new CartChildAdapter(carts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        holder.recProduct.setLayoutManager(linearLayoutManager);
        holder.recProduct.setAdapter(cartChildAdapter);
        //holder.recProduct.getAdapter().notifyItemInserted(carts.size()-1);

        holder.checkBox.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });

        if (selectedPosition==position){
            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setChecked(false);
        }

/*
        if (selectCheck.get(position) == 1) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int k=0; k<selectCheck.size(); k++) {
                    if(k==position) {
                        selectCheck.set(k,1);
                    } else {
                        selectCheck.set(k,0);
                    }
                }
                notifyDataSetChanged();

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvShopname;
        RecyclerView recProduct;
        private CheckBox checkBox;

//        public static final int SINGLE_SELECTION = 1;
//        SelectableShop selectableShop;
//        OnItemSelectedListener itemSelectedListener;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShopname = itemView.findViewById(R.id.tv_shopname);
            recProduct = itemView.findViewById(R.id.recProduct);
            checkBox = itemView.findViewById(R.id.checkBox_select);
//            itemSelectedListener = listener;

            /*checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (selectableShop.isSelected() && getItemViewType() == SINGLE_SELECTION) {
                        setChecked(false);
                    } else {
                        setChecked(true);
                    }
                    itemSelectedListener.onItemSelected(selectableShop);

                }
            });*/
        }

        /*public void setChecked(boolean value) {
            if (value) {
                checkBox.setBackgroundColor(Color.LTGRAY);
            } else {
                checkBox.setBackground(null);
            }
            selectableShop.setSelected(value);
            checkBox.setChecked(value);
        }

        public interface OnItemSelectedListener {

            void onItemSelected(SelectableShop item);
        }*/
    }

    /*private final Vector<Cart> carts;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public CartAdapter(Vector<Cart> carts) {
        this.carts = carts;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = carts.get(position);

        //Shop shop = cart.getShop();
        holder.tvShopname.setText(cart.getShopname());
        holder.tvName.setText(cart.getProduct_name());
        holder.tvPrice.setText(cart.getPrice());
        holder.tvVariation.setText(cart.getVariation_type());
        holder.quantity.setText(cart.getQuantity());
        Picasso.get()
                .load(cart.getImage())
                .into(holder.img);

//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(holder.recProduct.getContext(), LinearLayoutManager.VERTICAL, false);
//        layoutManager.setInitialPrefetchItemCount(cart.getProducts().size());
//
//        CartChildAdapter cartChildAdapter = new CartChildAdapter(cart.getProducts());
//        holder.recProduct.setLayoutManager(layoutManager);
//        holder.recProduct.setAdapter(cartChildAdapter);
//        holder.recProduct.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        *//*private TextView tvShopname;
        private RecyclerView recProduct;

        CartViewHolder(final View itemView)
        {

            super(itemView);
            this.tvShopname = itemView.findViewById(R.id.tv_shopname);
            this.recProduct = itemView.findViewById(R.id.recProduct);
        }*//*

        private final TextView tvShopname, tvName, tvVariation, tvPrice;
        private final EditText quantity;
        private final ImageView img;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvShopname = itemView.findViewById(R.id.tv_shopname);
            this.tvName = itemView.findViewById(R.id.tv_product_name);
            this.tvPrice = itemView.findViewById(R.id.tv_price);
            this.tvVariation = itemView.findViewById(R.id.tv_variation);
            this.quantity = itemView.findViewById(R.id.txt_quantity);
            this.img = itemView.findViewById(R.id.img_product);
        }
    }*/
}
