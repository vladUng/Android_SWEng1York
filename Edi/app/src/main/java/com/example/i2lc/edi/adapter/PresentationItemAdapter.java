package com.example.i2lc.edi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.i2lc.edi.PresentationActivity;
import com.example.i2lc.edi.R;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.dbClasses.User;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Cosmin Frateanu on 27/05/2017.
 */

public class PresentationItemAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Presentation> presentationList;
    private ViewHolder holder;
    private User user;

    /**
     * PresentationItemAdapter
     * @param context - context
     * @param presentationList - list of presentations ready to be displayed to the user
     * @param user - user logged in
     */
    public PresentationItemAdapter(Context context, ArrayList<Presentation> presentationList, User user) {
        this.context = context;
        this.presentationList = presentationList;
        this.user = user;
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

    /**
     * Private View Holder Class
     * This holds custom presentation view
     */
    private class ViewHolder{
        ImageView presentationThumbnail;
        TextView presentationTitle;
        TextView presentationAuthor;
        TextView presentationModule;
        TextView presentationDescription;
        Button joinButton;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.presentation_item, null);
            holder = new ViewHolder();
            holder.presentationTitle = (TextView) convertView.findViewById(R.id.presentation_title);
            holder.presentationAuthor = (TextView) convertView.findViewById(R.id.presentation_author);
            holder.presentationModule = (TextView) convertView.findViewById(R.id.presentation_module);
            holder.presentationDescription = (TextView) convertView.findViewById(R.id.presentation_description);
            holder.presentationThumbnail = (ImageView) convertView.findViewById(R.id.presentation_thumbnail);
            holder.joinButton = (Button) convertView.findViewById(R.id.joinButton);
            holder.joinButton.setTag(position);
            holder.joinButton.setBackgroundResource(R.color.liveColor);
            //Listener to start PresentationActivity with the user and list of presentations
            holder.joinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    int position = (Integer) arg0.getTag();
                    if(presentationList.size() > 0) {
                        Intent intent = new Intent(context, PresentationActivity.class);
                        intent.putExtra("presentation", presentationList.get(position));
                        intent.putExtra("user", user);
                        context.startActivity(intent);
                        System.out.println("Joined Presentation ID: " + Integer.toString(presentationList.get(position).getPresentationID()));
                    }else{
                        System.out.print("Presentation list is Empty!");
                    }
                }
            });

            if (presentationList.size() > 0) {
                Presentation presentation = presentationList.get(position);
                holder.presentationTitle.setText("Title: " + presentation.getTitle());
                holder.presentationDescription.setText("Description: " + presentation.getDescription());
                holder.presentationAuthor.setText("Author: " + presentation.getAuthor());
                holder.presentationModule.setText("Module: " + presentation.getModuleName());

                //Set ImageView to be presentation thumbnail from path
                if (presentation.getThumbnailPath() != null) {
                    File thumbnailFile = new File(presentation.getThumbnailPath());
                    if (isValidImageFile(thumbnailFile)) {
                        Uri uri = Uri.fromFile(thumbnailFile);
                        holder.presentationThumbnail.setImageURI(uri);
                        holder.presentationThumbnail.setAdjustViewBounds(true);
                    } else {
                        System.out.println("Could not load image file!");
                    }
                } else {
                    holder.presentationThumbnail.setImageResource(R.drawable.edi);
                }
            } else {
                System.out.println("Presentation List is Empty!");
            }
        }
        return convertView;
    }

    /**
     * Checks if file is a valid image file
     * @param file - file to be checked
     * @return true if the file is .jpg, .png, .gif or .jpeg
     */
    private boolean isValidImageFile (File file) {
        String[] imageExtensions = new String[] {"jpg", "png", "gif", "jpeg"};
        boolean isValidImageFile = false;
        for (String extension : imageExtensions) {
            if (file.getAbsolutePath().endsWith(extension) && file.exists()) {
                isValidImageFile = true;
            }
        }
        return isValidImageFile;
    }
}

