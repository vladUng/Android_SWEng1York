package com.example.i2lc.edi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.i2lc.edi.R;
import com.example.i2lc.edi.dbClasses.Interaction;
import com.example.i2lc.edi.dbClasses.User;

import org.w3c.dom.Text;

/**
 * Created by Cosmin Frateanu on 01/06/2017.
 */

public class AnswerItemAdapter extends BaseAdapter {
    private Context context;
    private String[] answersList;
    private User user;
    private int interActiveElementID;
    private TextView answerTextView;


    public AnswerItemAdapter(Context context, String[] answersList, User user, int interactiveElementID){
        this.context = context;
        this.answersList = answersList;
        this.user = user;
        this.interActiveElementID = interactiveElementID;
    }
    @Override
    public int getCount() {
        return answersList.length;
    }

    @Override
    public Object getItem(int position) {
        return answersList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.answer_item, null);
            answerTextView = (TextView) convertView.findViewById(R.id.answerItem);
        }
        convertView.setTag(position);
        answerTextView.setText(answersList[position]);
        //Interaction interaction = new Interaction(user.getUserID(), liveElement.getInteractiveElementID(), answer);
        return convertView;
    }
}
