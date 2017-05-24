package com.example.i2lc.edi.backend;

import java.util.ArrayList;

/**
 * The purpose of this class is to create, return and store the fields required to build a StringBuilder,
 * which are needed for each sql query sent to the server.
 *
 * Created by vlad on 24/05/2017.
 */

public class QueryFields {

    protected StringBuilder sb;
    protected ArrayList<String> fields;

    public QueryFields(String type) {

        sb = new StringBuilder();
        fields = new ArrayList<String>();

        switch (type) {
            case "User":
                fields.add("user_id");
                fields.add("user_type");
                fields.add("username");
                fields.add("first_name");
                fields.add("last_name");
                fields.add("email_address");
                fields.add("active_presentation_id");
                break;

            case "Module":
                fields.add("module_id");
                fields.add("module_name");
                fields.add("subject");
                fields.add("description");
                fields.add("time_last_updated");
                fields.add("time_created");
                break;

            case "Presentation":
                break;

            case "InteractiveElement":
                break;

            case "Interaction":
                break;

            case "Question":
                break;

            case "Thumbnail":
                break;
        }

        //construct the SB from the columns passed
        for (int idx=0; idx<fields.size()-1; idx++) {
            sb.append(" " + fields.get(idx) + ",");
        }

        //last element doesn't have a , at the end
        sb.append(" " + fields.get(fields.size()-1));
    }

    public StringBuilder getSb() {
        return sb;
    }

    public ArrayList<String> getFields() {
        return fields;
    }
}
