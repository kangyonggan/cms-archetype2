#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.biz.service.impl;

import com.github.pagehelper.PageHelper;
import ${package}.${parentArtifactId}.biz.service.UserService;
import ${package}.${parentArtifactId}.biz.util.Digests;
import ${package}.${parentArtifactId}.biz.util.Encodes;
import ${package}.${parentArtifactId}.biz.util.StringUtil;
import ${package}.${parentArtifactId}.mapper.RoleMapper;
import ${package}.${parentArtifactId}.mapper.UserMapper;
import ${package}.${parentArtifactId}.mapper.UserProfileMapper;
import ${package}.${parentArtifactId}.model.annotation.CacheDelete;
import ${package}.${parentArtifactId}.model.annotation.CacheDeleteAll;
import ${package}.${parentArtifactId}.model.annotation.CacheGetOrSave;
import ${package}.${parentArtifactId}.model.annotation.LogTime;
import ${package}.${parentArtifactId}.model.constants.AppConstants;
import ${package}.${parentArtifactId}.model.vo.ShiroUser;
import ${package}.${parentArtifactId}.model.vo.User;
import ${package}.${parentArtifactId}.model.vo.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/1/19
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    @LogTime
    public User findUser4Login(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }

        // 用户名
        if (username.matches("^[a-z]${symbol_escape}${symbol_escape}w+")) {
            return findUserByUsername(username);
        }

        // 邮箱
        if (username.contains("@")) {
            return findUserByEmail(username);
        }

        // 手机号
        if (username.matches("^1${symbol_escape}${symbol_escape}d{10}${symbol_dollar}")) {
            return findUserByMobile(username);
        }

        return null;
    }

    @Override
    @LogTime
    public User findUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);

        return super.selectOne(user);
    }

    @Override
    @LogTime
    public User findUserByMobile(String mobile) {
        User user = new User();
        user.setMobile(mobile);

        return super.selectOne(user);
    }

    @Override
    @LogTime
    public User findUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);

        return super.selectOne(user);
    }

    @Override
    @LogTime
    public ShiroUser getShiroUser() {
        return (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    }

    @Override
    @LogTime
    @CacheGetOrSave("user:id:{0}")
    public User findUserById(Long id) {
        User user = super.selectByPrimaryKey(id);

        if (user != null) {
            user.setPassword(null);
            user.setSalt(null);
        }
        return user;
    }

    @Override
    @LogTime
    public boolean existsUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return userMapper.selectCount(user) == 1;
    }

    @Override
    @LogTime
    public void saveUserWithDefaultRole(User user) {
        entryptPassword(user);
        super.insertSelective(user);

        saveUserProfile(user);

        saveUserRoles(user.getUsername(), AppConstants.DEFAULT_ROLE_CODE);
    }

    @Override
    @LogTime
    public boolean existsEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return userMapper.selectCount(user) == 1;
    }

    @Override
    @LogTime
    @CacheDelete("user:id:{0:id}")
    public void updateUserPassword(User user) {
        entryptPassword(user);
        super.updateByPrimaryKeySelective(user);
    }

    @Override
    @LogTime
    public List<User> searchUsers(int pageNum, String fullname, String mobile, String email) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotEmpty(fullname)) {
            criteria.andLike("fullname", StringUtil.toLikeString(fullname));
        }

        if (StringUtils.isNotEmpty(mobile)) {
            criteria.andLike("mobile", StringUtil.toLikeString(mobile));
        }

        if (StringUtils.isNotEmpty(email)) {
            criteria.andLike("email", StringUtil.toLikeString(email));
        }

        example.setOrderByClause("id desc");

        PageHelper.startPage(pageNum, AppConstants.PAGE_SIZE);
        return super.selectByExample(example);
    }

    @Override
    @LogTime
    @CacheDelete("user:id:{0:id}")
    public void updateUser(User user) {
        super.updateByPrimaryKeySelective(user);
    }

    @Override
    @LogTime
    @CacheDelete("menu:username:{0}||role:username:{0}")
    public void updateUserRoles(String username, String roleCodes) {
        roleMapper.deleteAllRolesByUsername(username);

        if (StringUtils.isNotEmpty(roleCodes)) {
            saveUserRoles(username, roleCodes);
        }
    }

    @Override
    @LogTime
    public boolean existsMobile(String mobile) {
        User user = new User();
        user.setMobile(mobile);
        return userMapper.selectCount(user) == 1;
    }

    @Override
    @LogTime
    public UserProfile findUserProfileByUsername(String username) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(username);

        return userProfileMapper.selectOne(userProfile);
    }

    @Override
    @LogTime
    @CacheDelete("user:id:{0:id}")
    public void updateUserAndProfile(User user, UserProfile userProfile) {
        if (StringUtils.isNotEmpty(user.getPassword())) {
            entryptPassword(user);
        } else {
            user.setPassword(null);
        }

        super.updateByPrimaryKeySelective(user);

        userProfileMapper.updateByPrimaryKeySelective(userProfile);
    }

    /**
     * 保存用户基础信息
     *
     * @param user
     */
    private void saveUserProfile(User user) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(user.getUsername());

        userProfileMapper.insertSelective(userProfile);
    }

    /**
     * 批量保存用户角色
     *
     * @param username
     * @param roleCodes
     */
    private void saveUserRoles(String username, String roleCodes) {
        userMapper.insertUserRoles(username, Arrays.asList(roleCodes.split(",")));
    }

    /**
     * 设定安全的密码，生成随机的salt并经过N次 sha-1 hash
     *
     * @param user
     */
    private void entryptPassword(User user) {
        byte[] salt = Digests.generateSalt(AppConstants.SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, AppConstants.HASH_INTERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }
}
