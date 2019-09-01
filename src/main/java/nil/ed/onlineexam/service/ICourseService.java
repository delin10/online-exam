package nil.ed.onlineexam.service;

import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.Course;

/**
 * 课程管理
 */
public interface ICourseService {
    /**
     * 创建课程
     * @param course 课程
     * @return 响应对象
     */
    Response addCourse(Course course, Integer creator);

    /**
     * 参加课程
     * @param joiner 参加者
     * @param cid 课程id
     * @return 响应对象
     */
    Response joinCourse(Integer joiner, Integer cid);

    /**
     * 查看课程
     * @param cid 课程id
     * @return 课程信息
     */
    Response getCourse(Integer cid);
}
