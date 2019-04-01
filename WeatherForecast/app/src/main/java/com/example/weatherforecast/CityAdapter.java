package com.example.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CityAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CityOption> mCityOptions;
    private static class ViewHolder{
        //public RadioButton rbutton;
        public TextView cname;
    }

    public CityAdapter(Context context){
        mContext = context;
        mCityOptions = new ArrayList<CityOption>();
        mCityOptions.add(new CityOption("Novi Sad"));
        mCityOptions.add(new CityOption("Beograd"));
        mCityOptions.add(new CityOption("Subotica"));
    }

    @Override
    public int getCount() {
        return mCityOptions.size();
    }

    @Override
    public Object getItem(int i) throws IndexOutOfBoundsException{
        Object oc = null;
        try{
            oc = mCityOptions.get(i);
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return oc;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void addItem(CityOption co){
        mCityOptions.add(co);
        notifyDataSetChanged();
    }

    public boolean removeItem(CityOption co){
        boolean ret = mCityOptions.remove(co);
        notifyDataSetChanged();
        return ret;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = view;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.city_element, null);
            ViewHolder holder = new ViewHolder();
           // holder.rbutton = (RadioButton) convertView.findViewById(R.id.element_button);
            holder.cname = (TextView) convertView.findViewById(R.id.element_name);
            convertView.setTag(holder);
        }
        CityOption option = (CityOption) getItem(i);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.cname.setText(option.getCityName(), TextView.BufferType.NORMAL);
        return convertView;
    }
}
