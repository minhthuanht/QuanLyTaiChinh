package com.minhthuanht.quanlytaichinh.wallet.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.common.Constants;
import com.minhthuanht.quanlytaichinh.model.Wallet;

import java.util.List;

public class WalletListAdapter extends ArrayAdapter<Wallet> {
    private final Activity mContext;
    private final int mResource;
    private final List<Wallet> objects;

    public WalletListAdapter(Activity context, int resource, List<Wallet> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.objects = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null)
            convertView = inflater.inflate(this.mResource, null);
        ImageView imganhItem = convertView.findViewById(R.id.imgWalletLogo);
        TextView txtTenitem = convertView.findViewById(R.id.txtWalletTitle);
        TextView txtBalacne = convertView.findViewById(R.id.txtWalletBalance);

        Wallet wallet = this.objects.get(position);
        txtTenitem.setText(wallet.getWalletName());
        txtBalacne.setText(String.format(Constants.PRICE_FORMAT,wallet.getWalletBalance()));
        if(wallet.getWalletBalance() >= 0) {
            txtBalacne.setTextColor(getContext().getResources().getColor(R.color.colorMoneyTradingPositive));
        }
        else {
            txtBalacne.setTextColor(getContext().getResources().getColor(R.color.colorMoneyTradingNegative));
        }

        // lấy id của ảnh
        int idImg = R.drawable.ic_account_balance_wallet_black_24dp;
        imganhItem.setImageResource(idImg);

        return convertView;
    }
}
