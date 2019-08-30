package nil.ed.onlineexam.common;

import java.util.List;

public class PageResultResponseBuilder<T> extends ResponseBuilder<PageResult<T>> {
    private int pageNo;
    private int pageSize;
    private int total;
    private List<T> data;

    public int getTotal() {
        return total;
    }

    public PageResultResponseBuilder<T> setTotal(int total) {
        this.total = total;
        return this;
    }

    public int getPageNo() {
        return pageNo;
    }

    public PageResultResponseBuilder<T> setPageNo(int pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageResultResponseBuilder<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public List<T> getPageData() {
        return data;
    }

    public PageResultResponseBuilder<T> setPageData(List<T> data) {
        this.data = data;
        return this;
    }

    @Override
    public Response<PageResult<T>> build() {
        Response<PageResult<T>> response = new Response<>();
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setPageNo(pageNo);
        pageResult.setPageSize(pageSize);
        pageResult.setData(data);
        pageResult.setTotal(total);
        response.setCode(code);
        response.setData(pageResult);
        return response;
    }
}
