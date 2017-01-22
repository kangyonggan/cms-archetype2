#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.web.controller.dashboard;

import ${package}.${parentArtifactId}.biz.service.DictionaryService;
import ${package}.${parentArtifactId}.biz.service.RedisService;
import ${package}.${parentArtifactId}.model.constants.DictionaryType;
import ${package}.${parentArtifactId}.model.vo.Dictionary;
import ${package}.${parentArtifactId}.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author kangyonggan
 * @since 2017/1/9
 */
@Controller
@RequestMapping("dashboard/content/cache")
public class DashboardContentCacheController extends BaseController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 缓存管理
     *
     * @param system
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions("CONTENT_CACHE")
    public String list(@RequestParam(value = "system", required = false, defaultValue = "") String system,
                       Model model) {
        Set<String> keys = new HashSet();
        if (StringUtils.isNotEmpty(system)) {
            keys = redisService.getKeys(system + "*");
        }
        List<Dictionary> systems = dictionaryService.findDictionariesByType(DictionaryType.SYSTEM.getType());

        model.addAttribute("keys", keys);
        model.addAttribute("systems", systems);
        return getPathList();
    }

    /**
     * 缓存详情
     *
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = "{key:[${symbol_escape}${symbol_escape}w:]+}", method = RequestMethod.GET)
    @RequiresPermissions("CONTENT_CACHE")
    public String detail(@PathVariable("key") String key, Model model) {
        Object cache = redisService.get(key);

        model.addAttribute("key", key);
        model.addAttribute("cache", cache);
        model.addAttribute("isList", cache instanceof List);
        return getPathDetail();
    }

    /**
     * 清空缓存
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "{key:[${symbol_escape}${symbol_escape}w:]+}/clear", method = RequestMethod.GET)
    @RequiresPermissions("CONTENT_CACHE")
    @ResponseBody
    public Map<String, Object> clear(@PathVariable("key") String key) {
        redisService.delete(key);
        return getResultMap();
    }

    /**
     * 清空列表缓存
     *
     * @param system
     * @return
     */
    @RequestMapping(value = "clearall", method = RequestMethod.GET)
    @RequiresPermissions("CONTENT_CACHE")
    @ResponseBody
    public Map<String, Object> clearList(@RequestParam("system") String system) {
        redisService.deleteAll(system + "*");
        return getResultMap();
    }

}
