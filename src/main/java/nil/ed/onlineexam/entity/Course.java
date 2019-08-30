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
    private int id;

   /*
   课程名称
    */
    private String name;

    /*
    老师id
     */
    private int teacher;

    /*
    课程创建时间
     */
    private long createTime;

    /*
    课程更新时间
     */
    private long updateTime;

    /*
    课程开始时间
     */
    private long startTime;

    /*
    课程结束时间
     */
    private long endTime;

    /*
    课程状态
     */
    private byte status;

}
