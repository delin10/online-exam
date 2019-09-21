package nil.ed.onlineexam.entity;

import lombok.Data;

@Data
public class JoinedCourse {
    /*
    id
     */
    private Integer id;

    /*
    参加用户id
     */
    private Integer uid;

    /*
    参加课程id
     */
    private Integer cid;

    /*
    获得的分数
     */
    private Short score;

    /*
    参加考试的时间
     */
    private Long joinTestTime;

    /*
    该记录的创建时间
     */
    private Long createTime;

    /*
    该记录的更新时间
     */
    private Long updateTime;
}