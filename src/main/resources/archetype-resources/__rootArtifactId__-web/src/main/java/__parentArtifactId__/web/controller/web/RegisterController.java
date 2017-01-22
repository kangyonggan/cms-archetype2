#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.web.controller.web;

import ${package}.${parentArtifactId}.biz.service.UserService;
import ${package}.${parentArtifactId}.model.constants.AppConstants;
import ${package}.${parentArtifactId}.model.vo.User;
import ${package}.${parentArtifactId}.web.controller.BaseController;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 2017/1/19
 */
@Controller
@RequestMapping("/")
@Log4j2
public class RegisterController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 注册界面
     *
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String register() {
        return getPathIndex();
    }

    /**
     * 注册
     *
     * @param user
     * @param result
     * @param captcha
     * @param request
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> register(@ModelAttribute("user") @Valid User user, BindingResult result,
                                        @RequestParam(value = "captcha") String captcha, HttpServletRequest request) {
        Map<String, Object> resultMap = getResultMap();

        HttpSession session = request.getSession();
        String realCaptcha = (String) session.getAttribute(AppConstants.KEY_CAPTCHA);
        log.info("session中的验证码为：{}", realCaptcha);
        log.info("用户上送的验证码为：{}", captcha);

        if (!captcha.equalsIgnoreCase(realCaptcha)) {
            setResultMapFailure(resultMap, "验证码不正确或已失效");
            return resultMap;
        }

        if (!result.hasErrors()) {
            try {
                UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());

                // 保存用户和默认角色
                userService.saveUserWithDefaultRole(user);

                // 登录
                final Subject subject = SecurityUtils.getSubject();
                subject.login(token);

                resultMap.put("errMsg", "/dashboard");
                return resultMap;
            } catch (Exception e) {
                log.error("注册失败", e);
                setResultMapFailure(resultMap, "注册失败，请稍后再试！");
                return resultMap;
            }
        } else {
            setResultMapFailure(resultMap, "注册表单有错误");
            return resultMap;
        }
    }
}
