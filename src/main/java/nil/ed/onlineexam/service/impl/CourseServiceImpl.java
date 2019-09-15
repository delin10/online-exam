package nil.ed.onlineexam.service.impl;

import nil.ed.onlineexam.common.NormalResponseBuilder;
import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.ResponseCodeEnum;
import nil.ed.onlineexam.common.enumm.CourseStatusEnum;
import nil.ed.onlineexam.entity.Course;
import nil.ed.onlineexam.entity.JoinedCourse;
import nil.ed.onlineexam.mapper.CourseMapper;
import nil.ed.onlineexam.mapper.TestPaperMapper;
import nil.ed.onlineexam.service.ICourseService;
import nil.ed.onlineexam.service.support.impl.SimpleInsertHelper;
import nil.ed.onlineexam.service.support.impl.SimpleSelectOneHelper;
import nil.ed.onlineexam.service.support.impl.SimpleSelectPageHelper;
import nil.ed.onlineexam.util.PageUtils;
import nil.ed.onlineexam.vo.BaseTestPaperVO;
import nil.ed.onlineexam.vo.CourseVO;
import nil.ed.onlineexam.vo.CourseWithStudentsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service("courseService")
public class CourseServiceImpl implements ICourseService {
    private static final long INITIAL_JOIN_TEST_TIME = 1L;
    @Resource
    private CourseMapper courseMapper;

    @Resource
    private TestPaperMapper testPaperMapper;

    private Executor executor;

    @Autowired
    @Qualifier("commonExecutor")
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public Response<Void> addCourse(Course course, Integer creator) {
        checkCourseRequired(course);
        setCreateFields(course, creator);
        return new SimpleInsertHelper()
                .operate(() -> {
                    courseMapper.insert(course);
                    return null;
                });
    }

    @Override
    public Response<Void> joinCourse(Integer joiner, Integer cid) {
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
        try {
            return new SimpleInsertHelper()
                    .operate(() -> {
                        courseMapper.joinCourse(joinedCourse);
                        return null;
                    });
        }catch (DuplicateKeyException e){
            return new NormalResponseBuilder<Void>()
                    .setCodeEnum(ResponseCodeEnum.DUPLICATE_OPERATION)
                    .build();
        }
    }

    @Override
    public Response getCourse(Integer cid) {
        return new SimpleSelectOneHelper<Course>()
                .operate(() -> courseMapper.getCourseById(cid));
    }

    @Override
    public Response<PageResult<BaseTestPaperVO>> listPublishedTestPapersOf(Integer cid, Integer uid) {
        int pageNo = 0;
        int pageSize = 20;
        return new SimpleSelectPageHelper<BaseTestPaperVO>(executor)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setCounter(() -> testPaperMapper.countPublishedTestPapersOf(uid, cid))
                .operate(() -> testPaperMapper.listPublishedTestPapersOf(uid, cid, PageUtils.calPageStart(pageNo, pageSize),  pageSize));
    }

    @Override
    public Response<PageResult<CourseVO>> listJoinedCourses(Integer uid) {
        int pageNo = 0;
        int pageSize = 20;
        return new SimpleSelectPageHelper<CourseVO>(executor)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setCounter(() -> courseMapper.countJoinedCourses(uid))
                .operate(() -> courseMapper.listJoinedCourses(uid, PageUtils.calPageStart(pageNo, pageSize), pageSize));
    }

    @Override
    public Response<PageResult<CourseVO>> listCourses() {
        int pageNo = 0;
        int pageSize = Integer.MAX_VALUE;
        return new SimpleSelectPageHelper<CourseVO>(executor)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setCounter(() -> courseMapper.countCourses())
                .operate(()  -> courseMapper.listCourses(null, PageUtils.calPageStart(pageNo, pageSize), pageSize));
    }

    @Override
    public Response<PageResult<CourseVO>> listCoursesOfTeacher(Integer teacher) {
        int pageNo = 0;
        int pageSize = Integer.MAX_VALUE;
        return new SimpleSelectPageHelper<CourseVO>(executor)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setCounter(() -> courseMapper.countCoursesIfUser(teacher))
                .operate(()  -> courseMapper.listCourses(teacher, PageUtils.calPageStart(pageNo, pageSize), pageSize));
    }

    @Override
    public Response<PageResult<CourseWithStudentsVO>> listCourseWithStudentsVOs(Integer cid, Integer currentUser) {
        Integer pageNo = 0, pageSize = Integer.MAX_VALUE;
        return new SimpleSelectPageHelper<CourseWithStudentsVO>(executor)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setCounter(() -> courseMapper.countCourses())
                .operate(() -> courseMapper.listCourseWithStudents(cid, currentUser, PageUtils.calPageStart(pageNo, pageSize), pageSize));
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

    private void checkCourseRequired(Course course){
        if (course.getStartTime() == null){
            throw new IllegalArgumentException("无开始时间");
        }

        if (course.getEndTime() == null){
            throw new IllegalArgumentException("无截止时间");
        }

        if (course.getName() == null){
            throw new IllegalArgumentException("课程名称不能为空");
        }
    }
}
