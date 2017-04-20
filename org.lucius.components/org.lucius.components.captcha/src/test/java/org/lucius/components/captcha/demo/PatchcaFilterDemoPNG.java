package org.lucius.components.captcha.demo;

import java.awt.Color;
import java.io.FileOutputStream;

import org.lucius.components.captcha.color.SingleColorFactory;
import org.lucius.components.captcha.filter.predefined.CurvesRippleFilterFactory;
import org.lucius.components.captcha.filter.predefined.DiffuseRippleFilterFactory;
import org.lucius.components.captcha.filter.predefined.DoubleRippleFilterFactory;
import org.lucius.components.captcha.filter.predefined.MarbleRippleFilterFactory;
import org.lucius.components.captcha.filter.predefined.WobbleRippleFilterFactory;
import org.lucius.components.captcha.service.ConfigurableCaptchaService;
import org.lucius.components.captcha.utils.encoder.EncoderHelper;

public class PatchcaFilterDemoPNG {

	public static void main(String[] args) throws Exception {
		for (int counter = 0; counter < 5; counter++) {
			ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
			cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
			switch (counter % 5) {
			case 0:
				cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
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
			FileOutputStream fos = new FileOutputStream("patcha_demo" + counter + ".png");
			String x = EncoderHelper.getChallangeAndWriteImage(cs, "png", fos);
			fos.close();
		}
	}
}
