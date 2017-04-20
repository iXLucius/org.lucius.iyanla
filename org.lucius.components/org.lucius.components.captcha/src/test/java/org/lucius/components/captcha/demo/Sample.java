package org.lucius.components.captcha.demo;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import org.lucius.components.captcha.color.SingleColorFactory;
import org.lucius.components.captcha.filter.predefined.CurvesRippleFilterFactory;
import org.lucius.components.captcha.service.ConfigurableCaptchaService;
import org.lucius.components.captcha.utils.encoder.EncoderHelper;

/**
 * sample code
 * Created by shijinkui on 15/3/15.
 */
public class Sample {
    public static void main(String[] args) throws IOException {

        ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
        cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
        cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));

        FileOutputStream fos = new FileOutputStream("patcha_demo.png");
        EncoderHelper.getChallangeAndWriteImage(cs, "png", fos);
        fos.close();
    }
}
