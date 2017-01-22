#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.biz.service;

import ${package}.${parentArtifactId}.model.vo.ShiroUser;
import ${package}.${parentArtifactId}.model.vo.User;
import ${package}.${parentArtifactId}.model.vo.UserProfile;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/1/19
 */
public interface UserService {

    /**
     * 查找用户，用于登录，username可以是手机号和邮箱
     *
     * @param username
     * @return
     */
    User findUser4Login(String username);

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 根据手机号查找用户
     *
     * @param mobile
     * @return
     */
    User findUserByMobile(String mobile);

    /**
     * 根据邮箱查找用户
     *
     * @param email
     * @return
     */
    User findUserByEmail(String email);

    /**
     * 获取当前登录的用户
     *
     * @return
     */
    ShiroUser getShiroUser();

    /**
     * 根据ID查找用户，不会查出密码
     *
     * @param id
     * @return
     */
    User findUserById(Long id);

    /**
     * 校验用户名是否存在
     *
     * @param username
     * @return
     */
    boolean existsUsername(String username);

    /**
     * 保存用户
     *
     * @param user
     */
    void saveUserWithDefaultRole(User user);

    /**
     * 校验邮件是否存在
     *
     * @param email
     * @return
     */
    boolean existsEmail(String email);

    /**
     * 更新用户的密码
     *
     * @param user
     */
    void updateUserPassword(User user);

    /**
     * 搜索用户
     *
     * @param pageNum
     * @param fullname
     * @param mobile
     * @param email
     * @return
     */
    List<User> searchUsers(int pageNum, String fullname, String mobile, String email);

    /**
     * 更新用户
     *
     * @param user
     */
    void updateUser(User user);

    /**
     * 修改用户角色
     *
     * @param username
     * @param roleCodes
     */
    void updateUserRoles(String username, String roleCodes);

    /**
     * 校验手机号是否存在
     *
     * @param mobile
     * @return
     */
    boolean existsMobile(String mobile);

    /**
     * 查找用户详细信息
     *
     * @param username
     * @return
     */
    UserProfile findUserProfileByUsername(String username);

    /**
     *  更新个人资料
     *
     * @param user
     * @param userProfile
     */
    void updateUserAndProfile(User user, UserProfile userProfile);
}
