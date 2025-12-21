package com.lxsc.commons;

/**
 * 封装分页数据，该类携带某一个页面的数据，并会记录当前页码、单页大小、总页码、总数据、跳过的数据，
 * 前端每次翻页都会重新发请求
 *
 * @param <T> 具体数据类型
 */
public class PageBean<T> {

    private Long pageNo;      // 当前页码

    private Long pageSize;    // 单页大小

    private Long totalNum;    // 数据总数量

    private Long pageCount;   // 总页码

    private Long skipNum;     // 跳过的数量

    private T data;           // 数据

    /**
     * 构造器，传入需要的页码和单页大小即可
     *
     * @param pageNo   当前页码
     * @param pageSize 单页大小
     */
    public PageBean(Long pageNo, Long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.skipNum = (pageNo - 1) * pageSize;   // 计算出跳过的数据条数
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    /**
     * 设置totalNum（总数据），同时自动计算出pageCount（总页码）
     *
     * @param totalNum 总数据条数
     */
    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
        if (totalNum == null || totalNum == 0 || pageSize == null || pageSize == 0) {
            this.pageCount = 0L;
            return;
        }
        this.pageCount = totalNum / pageSize;
        if (totalNum % pageSize >= 1) {
            this.pageCount++;
        }
    }

    public Long getPageCount() {
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public Long getSkipNum() {
        return skipNum;
    }

    public void setSkipNum(Long skipNum) {
        this.skipNum = skipNum;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}