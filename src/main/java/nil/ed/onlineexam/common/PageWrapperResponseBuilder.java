package nil.ed.onlineexam.common;

import java.util.List;

public class PageWrapperResponseBuilder<T> extends ResponseBuilder<PageWrapper<T>> {
    private int pageNo;
    private int pageSize;
    private List<T> data;

    public int getPageNo() {
        return pageNo;
    }

    public PageWrapperResponseBuilder<T> setPageNo(int pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageWrapperResponseBuilder<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public List<T> getPageData() {
        return data;
    }

    public PageWrapperResponseBuilder<T> setPageData(List<T> data) {
        this.data = data;
        return this;
    }

    @Override
    public Response build() {
        return null;
    }
}
