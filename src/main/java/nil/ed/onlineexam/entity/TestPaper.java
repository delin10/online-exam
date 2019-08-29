package nil.ed.onlineexam.entity;

public class TestPaper {
    private Integer id;

    private Integer cid;

    private String name;

    private Long startTime;

    private Long endTime;

    private Integer testDuration;

    private Integer score;

    private Integer creator;

    private Long createTime;

    private Byte published;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getTestDuration() {
        return testDuration;
    }

    public void setTestDuration(Integer testDuration) {
        this.testDuration = testDuration;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Byte getPublished() {
        return published;
    }

    public void setPublished(Byte published) {
        this.published = published;
    }
}