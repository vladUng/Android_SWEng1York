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

    /**
     *
     * @param type, the type of the element that is retrieved from Database
     */
    public QueryFields(String type) {

        sb = new StringBuilder();
        fields = new ArrayList<>();

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
                fields.add("presentation_id");
                fields.add("module_id");
                fields.add("xml_url");
                fields.add("live");
                fields.add("current_slide_number");
                break;

            case "InteractiveElement":
                fields.add("interactive_element_id");
                fields.add("presentation_id");
                fields.add("interactive_element_data");
                fields.add("type");
                fields.add("live");
                fields.add("slide_number");
                fields.add("xml_element_id");
                break;

            case "Interaction":
                fields.add("interaction_id");
                fields.add("user_id");
                fields.add("interactive_element_id");
                fields.add("interaction_data");
                fields.add("time_created");
                break;

            case "Question":
                fields.add("question_id");
                fields.add("user_id");
                fields.add("presentation_id");
                fields.add("time_created");
                fields.add("time_answered");
                fields.add("question_data");
                fields.add("slide_number");
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
