package com.example.vezba5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CharacterAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Character> mCharacters;
    public static class ViewHolder{
        ImageView mImage;
        TextView mName;
    }

    public CharacterAdapter(Context context){
        mContext = context;
        mCharacters = new ArrayList<Character>();
    }

    public void addCharacter(Character c){
        mCharacters.add(c);
        notifyDataSetChanged(); // Obavestava da je doslo do promene u podacima, kako bi se azurirao prikaz.
    }

    @Override
    public int getCount() {
        return mCharacters.size();
    }

    @Override
    public Object getItem(int i) {
        Object o = null;
        try {
            o = mCharacters.get(i);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public long getItemId(int i) {
        return i;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = view;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_element, null);
            ViewHolder holder = new ViewHolder();
            holder.mImage = (ImageView) convertView.findViewById(R.id.contact_image);
            holder.mName = (TextView) convertView.findViewById(R.id.contact_name);
            convertView.setTag(holder);
        }
        Character character = (Character) getItem(i);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.mImage.setImageDrawable(character.mImage);
        holder.mName.setText(character.mName);
        return convertView;
    }
}
