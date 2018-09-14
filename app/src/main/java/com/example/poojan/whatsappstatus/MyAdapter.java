package com.example.poojan.whatsappstatus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter{

    ArrayList<String> username;
    Context context;
    public MyAdapter(@NonNull Context context, ArrayList<String> captain_names) {
        super(context,R.layout.listview_item);
        this.username = captain_names;
        this.context = context;
    }

    @Override
    public int getCount() {
        return username.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewholder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
            viewholder.img = (ImageView) convertView.findViewById(R.id.imageView2);
            viewholder.txt = (TextView) convertView.findViewById(R.id.textView3);
            convertView.setTag(viewholder);
        }else{
            viewholder = (ViewHolder)convertView.getTag();
        }
        //viewholder.img.setImageResource(storyimg[position][0]);
        viewholder.txt.setText(username.get(position));
        return convertView;

    }
    static class ViewHolder {
        ImageView img;
        TextView txt;
    }
}
