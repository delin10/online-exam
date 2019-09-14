package nil.ed.onlineexam.service;

import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.User;
import nil.ed.onlineexam.vo.UserVO;

/**
 * 用户管理
 */
public interface IUserService {
    /**
     * 获得用户信息
     *
     * @param uid 用户id
     * @return 用户信息
     */
    Response<UserVO> getUserInfo(Integer uid);

    /**
     * 获取用户信息，包含敏感信息
     * @param uid
     * @return
     */
    Response<User> getUser(Integer uid);

    /**
     * 改变用户的角色，只有管理员具有这个资格
     *
     * @param uid 被修改角色的用户id
     * @param roleId 角色id
     * @param opUid 操作者的id
     * @return 修改后的角色信息
     */
    Response<Void> changeRoleOfUser(Integer uid, Integer roleId, Integer opUid);

    /**
     * 忘记密码，通过信息校验初始化密码
     *
     * @param verifyInfo 校验信息
     * @return 初始化密码
     */
    Response<Void> forgetPassword(User verifyInfo);

    /**
     * 注册用户
     * @param user 用户信息
     * @return 注册后的信息
     */
    Response<UserVO> register(User user);

    /**
     * 显示所有用户
     * @return 分页信息
     */
    Response<PageResult<UserVO>> listUsers();
}
