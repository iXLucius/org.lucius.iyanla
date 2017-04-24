package org.lucius.iyanla.portal.auth;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lucius.components.captcha.color.SingleColorFactory;
import org.lucius.components.captcha.filter.predefined.CurvesRippleFilterFactory;
import org.lucius.components.captcha.filter.predefined.DiffuseRippleFilterFactory;
import org.lucius.components.captcha.filter.predefined.DoubleRippleFilterFactory;
import org.lucius.components.captcha.filter.predefined.MarbleRippleFilterFactory;
import org.lucius.components.captcha.filter.predefined.WobbleRippleFilterFactory;
import org.lucius.components.captcha.service.ConfigurableCaptchaService;
import org.lucius.components.captcha.utils.encoder.EncoderHelper;
import org.lucius.components.data.converter.json.JsonUtils;
import org.lucius.components.redis.IRedisService;
import org.lucius.iyanla.model.auth.User;
import org.lucius.iyanla.service.auth.IUserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;

@Controller
@RequestMapping("/user")
public class UserController {

    public static final String DEFAULT_CAPTCHA_PARAM = "jcaptcha";

    @Resource
    private IUserService userService;

    @Resource
    private IRedisService redisService;

    @RequestMapping(value = "/{userId}")
    public String getUserById(@PathVariable Long userId, Model model) {
        User user = userService.findUserById(userId);
        model.addAttribute("user", user);
        return "login";
    }

    @RequestMapping(value = "/index")
    public String index(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "index";
    }

    @RequestMapping(value = "/info/{userId}")
    @ResponseBody
    public User info(@PathVariable Long userId, Model model) {
        User user = userService.findUserById(userId);
        return user;
    }

    @RequestMapping(value = "/captcha.jpg",
                    method = RequestMethod.GET)
    public void getCaptcha(HttpSession session, HttpServletResponse response)
            throws IOException {

        HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.IMAGE_GIF);
        headers.setContentType(MediaType.IMAGE_PNG);
        ConfigurableCaptchaService cs = getRandomCaptchaService();

        OutputStream os = response.getOutputStream();
        String code = EncoderHelper.getChallangeAndWriteImage(cs, "png", os);
        session.removeAttribute(DEFAULT_CAPTCHA_PARAM);
        session.setAttribute(DEFAULT_CAPTCHA_PARAM, code);
        os.close();

    }

    @RequestMapping(value = "/pushToRedis",
                    method = RequestMethod.GET)
    @ResponseBody
    public String pushToRedis(HttpSession session, HttpServletResponse response)
            throws IOException {
        User user = userService.findUserById(1L);
        redisService.set("user-key-" + user.getId(), user);
        return "success";
    }

    @RequestMapping(value = "/pollFromRedis",
                    method = RequestMethod.GET)
    @ResponseBody
    public User pollFromRedis()
            throws IOException {
        User user = redisService.get("user-key-1",User.class);
        return user;
    }

    @RequestMapping(value = "/toJson",
            method = RequestMethod.GET)
    @ResponseBody
    public Map<String, List<User>> toJson(){
        Map<String, List<User>> x = new HashMap<String, List<User>>();
        for (int i = 0 ; i < 5; i ++){
            
            List<User> users = new ArrayList<User>();
            
            for (int j = 0 ; j < 5; j ++){
                
                User user = new User();
                user.setId((long) j);
                user.setUsername("lucius-"+j);
                user.setPassword("lucius-" + j);
                user.setGender(j%2==0?User.Gender.FEMALE:User.Gender.MALE);
                users.add(user);
            }
            x.put("index-"+i, users);
        }
        return JsonUtils.fromJson(JsonUtils.toJson(x), new TypeReference<Map<String, List<User>>>() { });
    }
    
    public ConfigurableCaptchaService getRandomCaptchaService()
            throws IOException {
        int counter = new Random().nextInt(5);
        ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
        cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
        switch (counter % 5) {
            case 0:
                cs.setFilterFactory(
                        new CurvesRippleFilterFactory(cs.getColorFactory()));
                break;
            case 1:
                cs.setFilterFactory(new MarbleRippleFilterFactory());
                break;
            case 2:
                cs.setFilterFactory(new DoubleRippleFilterFactory());
                break;
            case 3:
                cs.setFilterFactory(new WobbleRippleFilterFactory());
                break;
            case 4:
                cs.setFilterFactory(new DiffuseRippleFilterFactory());
                break;
        }
        return cs;
    }

}
