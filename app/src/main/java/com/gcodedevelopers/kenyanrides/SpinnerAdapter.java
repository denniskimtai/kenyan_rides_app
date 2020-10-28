package com.gcodedevelopers.kenyanrides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    List<Brands> list;

    public SpinnerAdapter(Context context, List<Brands> list ){
        this.list =list;
        this.context =context;
        inflater = (LayoutInflater.from(context));

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_item, null);

        TextView spinnerText = view.findViewById(R.id.spinnerText);
        spinnerText.setText(list.get(i).getBrandName());

        return view;
    }
}
