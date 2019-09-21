package nil.ed.onlineexam.entity;

import lombok.Data;

/**
 * 课程
 */
@Data
public class Course {
    /*
    课程id
     */
    private Integer id;

   /*
   课程名称
    */
    private String name;

    /*
    老师id
     */
    private Integer teacher;

    /*
    课程创建时间
     */
    private Long createTime;

    /*
    课程更新时间
     */
    private Long updateTime;

    /*
    课程开始时间
     */
    private Long startTime;

    /*
    课程结束时间
     */
    private Long endTime;

    /*
    课程状态
     */
    private byte status;

}
