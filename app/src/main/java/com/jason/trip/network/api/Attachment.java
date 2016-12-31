package com.jason.trip.network.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:Attachment
 * Description:
 * Created by Jason on 16/12/18.
 */

public class Attachment implements Parcelable {

    @SerializedName("desc")
    public String description;

    public String href;

    public String image;

    public String title;

    public String type;


    public static final Parcelable.Creator<Attachment> CREATOR =
            new Parcelable.Creator<Attachment>() {
                public Attachment createFromParcel(Parcel source) {
                    return new Attachment(source);
                }
                public Attachment[] newArray(int size) {
                    return new Attachment[size];
                }
            };

    public Attachment() {}

    protected Attachment(Parcel in) {
        description = in.readString();
        href = in.readString();
        image = in.readString();
        title = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(href);
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(type);
    }
}
