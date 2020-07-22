package io.github.iamyours.wandroid.vo.xxmh;

import java.util.List;

public class XPage<T> {
    public int pageNum;
    public int pageSize;
    public int total;
    public int pages;
    public boolean isLastPage;
    public boolean hasNextPage;
    public List<T> list;
}
