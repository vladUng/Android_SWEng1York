package com.example.i2lc.edi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.i2lc.edi.R;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.model.ItemSlideMenu;

import java.util.List;

/**
 * Created by Cosmin Frateanu on 27/05/2017.
 */

public class PresentationItemAdapter extends BaseAdapter{
    private Context context;
    private List<Presentation> presentationList;

    public PresentationItemAdapter(Context context, List<Presentation> presentationList) {
        this.context = context;
        this.presentationList = presentationList;
    }

    @Override
    public int getCount() {
        return presentationList.size();
    }

    @Override
    public Object getItem(int position) {
        return presentationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder{
        ImageView presentationThumbnail;
        TextView presentationTitle;
        TextView presentationAuthor;
        TextView presentationModule;
        TextView presentationDate;
        Button joinButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.presentation_item,null);
            holder = new ViewHolder();
            //We will have to change all these for xml data
            holder.presentationTitle = (TextView) convertView.findViewById(R.id.presentation_title);
            holder.presentationAuthor = (TextView) convertView.findViewById(R.id.presentation_author);
            holder.presentationModule = (TextView) convertView.findViewById(R.id.presentation_module);
            holder.presentationDate = (TextView) convertView.findViewById(R.id.presentation_date);
            holder.presentationThumbnail = (ImageView) convertView.findViewById(R.id.presentation_thumbnail);
            holder.joinButton = (Button) convertView.findViewById(R.id.join_button);

            Presentation presentation = presentationList.get(position);
            holder.presentationThumbnail.setImageResource(R.drawable.edi);//TODO change when thumbnail from xml works
            holder.presentationTitle.setText(presentation.getTitle());
            holder.presentationDate.setText(presentation.getDate());
            holder.presentationAuthor.setText(presentation.getAuthor());
            holder.presentationModule.setText(presentation.getModule());
        }
        return convertView;
    }
}
