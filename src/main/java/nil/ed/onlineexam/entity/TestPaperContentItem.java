package nil.ed.onlineexam.entity;

public class TestPaperContentItem {
    private Integer id;

    private Integer pid;

    private Integer qid;

    private Integer firstSeq;

    private Integer secSeq;

    private Integer score;

    private Long createTime;

    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getQid() {
        return qid;
    }

    public void setQid(Integer qid) {
        this.qid = qid;
    }

    public Integer getFirstSeq() {
        return firstSeq;
    }

    public void setFirstSeq(Integer firstSeq) {
        this.firstSeq = firstSeq;
    }

    public Integer getSecSeq() {
        return secSeq;
    }

    public void setSecSeq(Integer secSeq) {
        this.secSeq = secSeq;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}