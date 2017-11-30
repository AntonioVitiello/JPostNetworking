package com.example.jpost.networking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antlap on 22/11/2017.
 *  “@SerializedName” : Is an annotation from gson library that maps the variable to the json key.
 *  “@Expose” : It is an annotation from gson library. It makes only the variable with expose to be available for parsing.
 */
//public class GitReposContainer implements Parcelable {
public class GitReposContainer implements Parcelable {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    @SerializedName("items")
    @Expose
    List<GitRepo> items;

    @SerializedName("default_branch")
    @Expose
    String defaultBranch;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<GitRepo> getItems() {
        return items;
    }

    public void setItems(ArrayList<GitRepo> items) {
        this.items = items;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{totalCount=" + totalCount +
                ", defaultBranch='" + defaultBranch + '\'' +
                ", items=" + items +
                '}';
    }


    protected GitReposContainer(Parcel in) {
        if (in.readByte() == 0) {
            totalCount = null;
        } else {
            totalCount = in.readInt();
        }
        items = in.createTypedArrayList(GitRepo.CREATOR);
        defaultBranch = in.readString();
    }

    public static final Creator<GitReposContainer> CREATOR = new Creator<GitReposContainer>() {
        @Override
        public GitReposContainer createFromParcel(Parcel in) {
            return new GitReposContainer(in);
        }

        @Override
        public GitReposContainer[] newArray(int size) {
            return new GitReposContainer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (totalCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalCount);
        }
        dest.writeTypedList(items);
        dest.writeString(defaultBranch);
    }

}