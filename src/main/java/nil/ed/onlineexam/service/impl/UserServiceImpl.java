package nil.ed.onlineexam.service.impl;

import nil.ed.onlineexam.aop.annotation.MethodInvokeLog;
import nil.ed.onlineexam.common.NormalResponseBuilder;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.ResponseCodeEnum;
import nil.ed.onlineexam.entity.User;
import nil.ed.onlineexam.mapper.UserMapper;
import nil.ed.onlineexam.security.CustomPasswordEncoder;
import nil.ed.onlineexam.service.IUserService;
import nil.ed.onlineexam.service.exception.VerifyInfoIncorrectException;
import nil.ed.onlineexam.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements IUserService {
    private static String INITIAL_PWD = "123456";

    private static Integer NORMAL_ROLE = 2;

    private static Pattern ONLY_NUMBER_OR_ONLY_LETTER = Pattern.compile("^[0-9]+$|^[a-z]+$|^[A-Z]+$");

    private static Pattern ONLY_NUMBER_OR_LETTER = Pattern.compile("^[0-9a-zA-Z]+$");

    @Resource
    private UserMapper userMapper;

    private CustomPasswordEncoder customPasswordEncoder;

    @Autowired
    public UserServiceImpl(CustomPasswordEncoder customPasswordEncoder) {
        this.customPasswordEncoder = customPasswordEncoder;
    }

    @Override
    @MethodInvokeLog
    public Response<UserVO> getUserInfo(Integer uid) {
        UserVO userVO = userMapper.getUserVO(uid);

        return new NormalResponseBuilder<UserVO>()
                .setData(userVO)
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .build();
    }

    @Override
    @MethodInvokeLog
    public Response<Void> changeRoleOfUser(Integer uid, Integer roleId, Integer opUid) {
        Integer updateResult = userMapper.updateRoleOfUser(uid, roleId, opUid);


        ResponseCodeEnum responseCodeEnum = updateResult > 0 ? ResponseCodeEnum.SUCCESS :
                ResponseCodeEnum.NOT_FOUND;

        return new NormalResponseBuilder<Void>()
                .setCodeEnum(responseCodeEnum)
                .build();
    }

    @Override
    @MethodInvokeLog
    public Response<Void> forgetPassword(User verifyInfo) {
        NormalResponseBuilder<Void> builder = new NormalResponseBuilder<>();

        if (!checkVerifyInfo(verifyInfo)){
            return builder.setCodeEnum(ResponseCodeEnum.FAILED)
                    .build();
        }

        Integer updateResult = userMapper.updatePasswordOfUser(verifyInfo.getId(), customPasswordEncoder.encode(INITIAL_PWD));

        if (updateResult == 0){
            return builder.setCodeEnum(ResponseCodeEnum.FAILED)
                    .build();
        }

        return builder.setCode(ResponseCodeEnum.SUCCESS.getCode())
                .setMessage("密码为："+INITIAL_PWD)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<UserVO> register(User user) {
        /*
        检查必须的参数
         */
        checkRequired(user);

        /**
         * 检查密码的合法性
         */
        checkPassword(user.getPasswordMd5());

        /*
        构造插入对象
         */
        processNewUser(user);

        /*
        插入
         */
        userMapper.insert(user);

        /*
        检查回填的id
         */
        if (Objects.isNull(user.getId())){
            throw new RuntimeException("插入失败");
        }

        /*
        返回用户基本信息
         */
        return getUserInfo(user.getId());
    }

    private boolean checkVerifyInfo(User verifyInfo){
        User user = userMapper.getUserById(verifyInfo.getId());

        if (Objects.isNull(user)){
            return false;
        }

        try {

            /*
            鉴定昵称
             */
            checkEquals(user::getNickName, verifyInfo::getNickName);

            return true;
        }catch (VerifyInfoIncorrectException e){
            return false;
        }
    }

    /**
     * 判断是否相等
     * @param a a
     * @param b b
     * @throws VerifyInfoIncorrectException 不相等抛出异常
     */
    private void checkEquals(Supplier<?> a, Supplier<?> b)throws VerifyInfoIncorrectException {
        if (Objects.nonNull(a.get())){
            if (!a.get().equals(b.get())){
                throw new VerifyInfoIncorrectException();
            }
        }else if (Objects.nonNull(b.get())){
            throw new VerifyInfoIncorrectException();
        }
    }

    /**
     * 检查必要的参数
     * @param user 用户对象
     */
    private void checkRequired(User user){
        if (Objects.isNull(user.getNickName())){
            throw new IllegalArgumentException("昵称不能为空");
        }

    }

    /**
     * 处理新注册的user对象
     * @param user user对象
     */
    private void processNewUser(User user){
        long currentTime = Instant.now().toEpochMilli();
        user.setPasswordMd5(customPasswordEncoder.encode(user.getPasswordMd5()));
        user.setCreateTime(currentTime);
        user.setLastUpdateTime(currentTime);
        user.setRole(NORMAL_ROLE);
        user.setId(null);
    }

    /**
     * 必须为数字和字母的混合
     * @param password 密码
     */
    private static void checkPassword(String password){
        if (Objects.isNull(password)){
            throw new IllegalArgumentException("密码不能为空");
        }

        if (password.length() < 6){
            throw new IllegalArgumentException("密码必须大于6位");
        }

        if (ONLY_NUMBER_OR_ONLY_LETTER.matcher(password).find()){
            throw new IllegalArgumentException("密码必须是字母和数字的组合");
        }

        if (!ONLY_NUMBER_OR_LETTER.matcher(password).matches()){
            throw new IllegalArgumentException("密码中只能包含字母或者数字");
        }
    }
}
