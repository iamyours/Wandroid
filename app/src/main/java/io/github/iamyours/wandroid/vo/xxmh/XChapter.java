package io.github.iamyours.wandroid.vo.xxmh;

import android.os.Parcel;
import android.os.Parcelable;

public class XChapter implements Parcelable {
    public long id;
    public String title;
    public int bookId;
    public String coverUrl;
    public int sequence;
    public int state;//0:没有数据  1:抓取中   2:有数据
    public boolean freeFlag;

    protected XChapter(Parcel in) {
        id = in.readLong();
        title = in.readString();
        bookId = in.readInt();
        coverUrl = in.readString();
        sequence = in.readInt();
        state = in.readInt();
        freeFlag = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeInt(bookId);
        dest.writeString(coverUrl);
        dest.writeInt(sequence);
        dest.writeInt(state);
        dest.writeByte((byte) (freeFlag ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<XChapter> CREATOR = new Creator<XChapter>() {
        @Override
        public XChapter createFromParcel(Parcel in) {
            return new XChapter(in);
        }

        @Override
        public XChapter[] newArray(int size) {
            return new XChapter[size];
        }
    };

    @Override
    public String toString() {
        return "XChapter{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", bookId=" + bookId +
                ", coverUrl='" + coverUrl + '\'' +
                ", sequence=" + sequence +
                '}';
    }
}
