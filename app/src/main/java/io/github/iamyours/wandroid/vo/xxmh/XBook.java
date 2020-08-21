package io.github.iamyours.wandroid.vo.xxmh;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.github.iamyours.wandroid.extension.StringKt;

@Entity
public class XBook implements Parcelable {
    @PrimaryKey
    public int id;
    public String name;
    public String author;
    public String description;
    public String keywords;
    public int categoryId;
    public String category;
    public String coverUrl;
    public String extensionUrl;
    public int chapterCount;
    public double score;
    public int chapterIndex;
    public long lastReadTime;

    public XBook() {
    }

    public String getCoverUrl() {
        return StringKt.changeExt(coverUrl, "html");
    }

    public String getExtensionUrl() {
        return StringKt.changeExt(extensionUrl, "html");
    }

    protected XBook(Parcel in) {
        id = in.readInt();
        name = in.readString();
        author = in.readString();
        description = in.readString();
        keywords = in.readString();
        categoryId = in.readInt();
        category = in.readString();
        coverUrl = in.readString();
        extensionUrl = in.readString();
        chapterCount = in.readInt();
        score = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(keywords);
        dest.writeInt(categoryId);
        dest.writeString(category);
        dest.writeString(coverUrl);
        dest.writeString(extensionUrl);
        dest.writeInt(chapterCount);
        dest.writeDouble(score);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<XBook> CREATOR = new Creator<XBook>() {
        @Override
        public XBook createFromParcel(Parcel in) {
            return new XBook(in);
        }

        @Override
        public XBook[] newArray(int size) {
            return new XBook[size];
        }
    };

    @Override
    public String toString() {
        return "XBook{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", keywords='" + keywords + '\'' +
                ", categoryId=" + categoryId +
                ", category='" + category + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", extensionUrl='" + extensionUrl + '\'' +
                ", chapterCount=" + chapterCount +
                ", score=" + score +
                '}';
    }

    public String getSimpleDesc() {
        return "共" + chapterCount + "话，评分:" + score;
    }
}
