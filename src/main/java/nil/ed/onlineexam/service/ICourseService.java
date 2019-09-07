package nil.ed.onlineexam.service;

import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.Course;
import nil.ed.onlineexam.vo.BaseTestPaperVO;
import nil.ed.onlineexam.vo.CourseVO;
import nil.ed.onlineexam.vo.TestPaperWithQuestionsVO;

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
    Response<Void> joinCourse(Integer joiner, Integer cid);

    /**
     * 查看课程
     * @param cid 课程id
     * @return 课程信息
     */
    Response getCourse(Integer cid);

    /**
     * 获得参加的课程的已发布的考试信息
     * @param cid 课程id
     * @param uid 用户id
     * @return 考试信息
     */
    Response<PageResult<BaseTestPaperVO>> listPublishedTestPapersOf(Integer cid, Integer uid);

    /**
     * 获得已参加的考试
     * @param uid 用户id
     * @return 课程信息
     */
    Response<PageResult<CourseVO>> listJoinedCourses(Integer uid);

}
