package nil.ed.onlineexam.service.impl;

import nil.ed.onlineexam.common.NormalResponseBuilder;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.ResponseCodeEnum;
import nil.ed.onlineexam.common.enumm.CourseStatusEnum;
import nil.ed.onlineexam.entity.Course;
import nil.ed.onlineexam.entity.JoinedCourse;
import nil.ed.onlineexam.mapper.CourseMapper;
import nil.ed.onlineexam.service.ICourseService;
import nil.ed.onlineexam.service.support.impl.SimpleInsertHelper;
import nil.ed.onlineexam.service.support.impl.SimpleSelectOneHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Objects;

@Service("courseService")
public class CourseServiceImpl implements ICourseService {
    private static final long INITIAL_JOIN_TEST_TIME = 1L;
    @Resource
    private CourseMapper courseMapper;

    @Override
    public Response addCourse(Course course, Integer creator) {
        setCreateFields(course, creator);
        return new SimpleInsertHelper()
                .operate(() -> {
                    courseMapper.insert(course);
                    return null;
                });
    }

    @Override
    public Response joinCourse(Integer joiner, Integer cid) {
        if (!isValid(cid)){
            return new NormalResponseBuilder<Void>()
                    .setCode(ResponseCodeEnum.FAILED.getCode())
                    .setMessage("课程不存在或者当前不允许加入")
                    .build();
        }

        JoinedCourse joinedCourse = new JoinedCourse();

        joinedCourse.setUid(joiner);
        joinedCourse.setCid(cid);
        joinedCourse.setScore((short)-1);
        joinedCourse.setJoinTestTime(INITIAL_JOIN_TEST_TIME);
        joinedCourse.setCreateTime(Instant.now().toEpochMilli());
        joinedCourse.setUpdateTime(Instant.now().toEpochMilli());

        return new SimpleInsertHelper()
                .operate(()->{
                    courseMapper.joinCourse(joinedCourse);
                    return null;
                });
    }

    @Override
    public Response getCourse(Integer cid) {
        return new SimpleSelectOneHelper<Course>()
                .operate(() -> courseMapper.getCourseById(cid));
    }

    /**
     * 检查课程是否存在且状态是否合法
     * @param cid 课程id
     * @return 是否能加入改课程
     */
    public boolean isValid(Integer cid){
        Course course = courseMapper.getCourseById(cid);

        /*
        判断课程是否存在
         */
        if (Objects.isNull(course)){
            return false;
        }

        /*
        判断课程状态
         */
        if (course.getStatus() > CourseStatusEnum.ON.getCode()){
            return false;
        }

        return true;
    }

    private void setCreateFields(Course course, Integer creator){
        course.setCreateTime(Instant.now().toEpochMilli());
        course.setTeacher(creator);
        course.setUpdateTime(course.getCreateTime());
    }

    private void setUpdateFields(Course course, Integer updater){
        course.setUpdateTime(Instant.now().toEpochMilli());
    }
}
