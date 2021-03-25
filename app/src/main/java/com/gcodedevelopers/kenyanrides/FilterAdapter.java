package com.gcodedevelopers.kenyanrides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FilterAdapter extends BaseAdapter {

    Context context;
    String[] carTypes;
    LayoutInflater inflater;

    public FilterAdapter(Context applicationContext, String[] carTypes) {
        this.context = applicationContext;
        this.carTypes = carTypes;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return carTypes.length;
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
        return null;
    }
}
