package io.github.iamyours.wandroid.vo.xxmh;

import java.util.Objects;

import io.github.iamyours.wandroid.extension.StringKt;

public class XPicture {
    public long id;
    public String url;
    public int sequence;
    public long chapterId;
    public int chapterSequence;

    public String getUrl() {
        return StringKt.changeExt(url, "html");
    }

    public String getSequenceText() {
        return "" + sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XPicture xPicture = (XPicture) o;
        return id == xPicture.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
