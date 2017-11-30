package com.example.jpost.networking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by antlap on 22/11/2017.
 *  “@SerializedName” : Is an annotation from gson library that maps the variable to the json key.
 *  “@Expose” : It is an annotation from gson library. It makes only the variable with expose to be available for parsing.
 */
public class GitRepo implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("html_url")
    @Expose
    private String url;

    @SerializedName("size")
    @Expose
    private Integer size;

    @SerializedName("description")
    @Expose
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getDescription() {
        return description != null ? description : "Not valued";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id = " + id
                + ", name = " + name
                + ", URL = " + url
                + ", size = " + size
                + ", description = " + description
                + "}";
    }


    protected GitRepo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        url = in.readString();
        if (in.readByte() == 0) {
            size = null;
        } else {
            size = in.readInt();
        }
        description = in.readString();
    }

    public static final Creator<GitRepo> CREATOR = new Creator<GitRepo>() {
        @Override
        public GitRepo createFromParcel(Parcel in) {
            return new GitRepo(in);
        }

        @Override
        public GitRepo[] newArray(int size) {
            return new GitRepo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(url);
        if (size == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(size);
        }
        dest.writeString(description);
    }
}