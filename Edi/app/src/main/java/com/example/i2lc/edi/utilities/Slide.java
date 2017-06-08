package com.example.i2lc.edi.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.i2lc.edi.dbClasses.InteractiveElement;


import java.util.ArrayList;

/**
 * Created by habl on 23/02/2017.
 */
public class Slide implements Parcelable {
    protected ArrayList<InteractiveElement> slideElementList;
    protected int slideID;

    public Slide(Parcel in) {
        this();
        slideID = in.readInt();
        in.readTypedList(slideElementList, InteractiveElement.CREATOR);
    }

    public static final Creator<Slide> CREATOR = new Creator<Slide>() {
        @Override
        public Slide createFromParcel(Parcel in) {
            return new Slide(in);
        }

        @Override
        public Slide[] newArray(int size) {
            return new Slide[size];
        }
    };

    public Slide() {
        slideElementList = new ArrayList<>();
    }

    public ArrayList<InteractiveElement> getSlideElementList() {
        return slideElementList;
    }

    public void setSlideElementList(ArrayList<InteractiveElement> slideElementList) {
        this.slideElementList = slideElementList;
    }

    public int getSlideID() {
        return slideID;
    }

    public void setSlideID(int slideID) {
        this.slideID = slideID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(slideID);
        dest.writeTypedList(slideElementList);
    }
}

