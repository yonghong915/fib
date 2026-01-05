package com.fib.uqcp.ctrler.sys;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class SysUserLoginCtrler {
	@Autowired
	private Producer producer;

	@GetMapping("captcha.jpg")
	public void captcha(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-store,no-cache");
		response.setContentType("image/ipeg");
		// 生成文字验证码
		String text = producer.createText();
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
		// 保存到shiro session
		// tants.KAPTCHTON KEY.textShiroUtils.setsessionattrrt
		ServletOutputStream out;
		try {
			out = response.getOutputStream();
			ImageIO.write(image, "jpg", out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
