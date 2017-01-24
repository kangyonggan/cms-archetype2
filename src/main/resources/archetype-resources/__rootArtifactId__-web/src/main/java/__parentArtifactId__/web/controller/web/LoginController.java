#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.web.controller.web;

import ${package}.${parentArtifactId}.biz.service.MailService;
import ${package}.${parentArtifactId}.biz.service.TokenService;
import ${package}.${parentArtifactId}.biz.service.UserService;
import ${package}.${parentArtifactId}.model.constants.AppConstants;
import ${package}.${parentArtifactId}.model.vo.Token;
import ${package}.${parentArtifactId}.model.vo.User;
import ${package}.${parentArtifactId}.web.controller.BaseController;
import ${package}.${parentArtifactId}.web.util.IPUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 2017/1/8
 */
@Controller
@RequestMapping("/")
@Log4j2
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录界面
     *
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login() {
        return getPathIndex();
    }

    /**
     * 登录
     *
     * @param user
     * @param captcha
     * @param request
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestParam(value = "captcha") String captcha, User user, HttpServletRequest request) {
        Map<String, Object> resultMap = getResultMap();

        HttpSession session = request.getSession();
        String realCaptcha = (String) session.getAttribute(AppConstants.KEY_CAPTCHA);
        log.info("session中的验证码为：{}", realCaptcha);
        log.info("用户上送的验证码为：{}", captcha);

        if (!captcha.equalsIgnoreCase(realCaptcha)) {
            resultMap.put(ERR_CODE, FAILURE);
            resultMap.put(ERR_MSG, "验证码错误或已失效");
            return resultMap;
        }

        // 清除验证码
        session.removeAttribute(AppConstants.KEY_CAPTCHA);

        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        final Subject subject = SecurityUtils.getSubject();

        try {
            session.setMaxInactiveInterval(30 * 24 * 60 * 60);// 30天
            subject.login(token);
        } catch (UnknownAccountException uae) {
            log.warn("未知用户名", uae);
            setResultMapFailure(resultMap, "未知用户名");
            return resultMap;
        } catch (IncorrectCredentialsException ice) {
            log.warn("用户名或密码错误", ice);
            setResultMapFailure(resultMap, "用户名或密码错误");
            return resultMap;
        } catch (LockedAccountException lae) {
            log.warn("账号被锁", lae);
            setResultMapFailure(resultMap, "账号被锁, 请联系管理员");
            return resultMap;
        } catch (DisabledAccountException dae) {
            log.warn("账号已禁用", dae);
            setResultMapFailure(resultMap, "账号已禁用, 请联系管理员");
            return resultMap;
        } catch (Exception e) {
            log.error("未知异常", e);
            setResultMapFailure(resultMap);
            return resultMap;
        }

        String redirectUrl = "/dashboard";

        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        // 获取之前访问的URL
        if (savedRequest != null && savedRequest.getRequestUrl() != null) {
            redirectUrl = savedRequest.getRequestUrl();
        }
        resultMap.put(ERR_MSG, redirectUrl);

        return resultMap;
    }

    /**
     * 注销
     *
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        final Subject subject = SecurityUtils.getSubject();
        log.info("logout {}", subject.getPrincipal());
        subject.logout();
        return "redirect:/";
    }

    /**
     * 找回密码界面
     *
     * @return
     */
    @RequestMapping(value = "reset", method = RequestMethod.GET)
    public String reset() {
        return getPathRoot() + "/reset";
    }

    /**
     * 找回密码
     *
     * @param email
     * @param captcha
     * @param request
     * @return
     */
    @RequestMapping(value = "reset", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> reset(@RequestParam("email") String email, @RequestParam("captcha") String captcha,
                                     HttpServletRequest request) {
        Map<String, Object> resultMap = getResultMap();

        log.info("找回密码的邮箱：{}", email);
        log.info("找回密码的验证码：{}", captcha);

        HttpSession session = request.getSession();
        String realCaptcha = (String) session.getAttribute(AppConstants.KEY_CAPTCHA);
        log.info("session中的验证码：{}", realCaptcha);

        if (!captcha.equalsIgnoreCase(realCaptcha)) {
            resultMap.put(ERR_CODE, FAILURE);
            resultMap.put(ERR_MSG, "验证码错误或已失效");
            return resultMap;
        }

        // 清除验证码
        session.removeAttribute(AppConstants.KEY_CAPTCHA);

        User user = userService.findUserByEmail(email);
        if (user == null) {
            setResultMapFailure(resultMap, "没有此邮箱的注册信息");
            log.info(resultMap.get("errMsg"));
            return resultMap;
        }

        mailService.sendResetMail(user, IPUtil.getServerHost(request));
        resultMap.put("errMsg", "/${symbol_pound}reset-result");

        return resultMap;
    }

    /**
     * 找回密码结果界面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "reset-result", method = RequestMethod.GET)
    public String result(Model model) {
        model.addAttribute("message", "找回密码处理中!!!<br/>请稍后去邮箱查看找回结果...");
        return getPathRoot() + "/reset-result";
    }

    /**
     * 重置密码结果界面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "reset-password-result", method = RequestMethod.GET)
    public String resultPassword(Model model) {
        model.addAttribute("message", "重置密码成功!!!");
        return getPathRoot() + "/reset-result";
    }

    /**
     * 重置密码
     *
     * @param code
     * @param userId
     * @param password
     * @return
     */
    @RequestMapping(value = "reset/password", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> resetPassword(@RequestParam("code") String code, @RequestParam("userId") Long userId,
                                             @RequestParam("password") String password) {
        Map<String, Object> resultMap = getResultMap();

        Token token = tokenService.findTokenByCode(code);
        User user = userService.findUserById(userId);

        if (token == null) {
            setResultMapFailure(resultMap, "链接不合法，请检查在拷贝过程中是否遗漏！");
        } else if (token.getExpireTime().before(new Date())) {
            setResultMapFailure(resultMap, "链接已过期，请重新发起找回密码请求！");
        } else if (token.getIsDeleted() == 1) {
            setResultMapFailure(resultMap, "链接已被使用，不能重复使用！");
        } else {
            if (user == null) {
                setResultMapFailure(resultMap, "用户不存在");
            } else {
                token.setIsDeleted((byte) 1);
                tokenService.updateToken(token);

                user.setPassword(password);
                userService.updateUserPassword(user);
            }
        }

        resultMap.put("errMsg", "/${symbol_pound}reset-password-result");
        return resultMap;
    }

}
