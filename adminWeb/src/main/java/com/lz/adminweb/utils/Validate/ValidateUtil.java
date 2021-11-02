package com.lz.adminweb.utils.Validate;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;

import java.awt.image.BufferedImage;

/**
 * @Auther:luzhichao
 * @Date:2021/5/24 17:50
 * @Description: 验证码生成工具
 */
@Log4j2
public class ValidateUtil {

	/** 用于随机选的数字 */
	public static final String BASE_NUMBER = "0123456789";

	/** 用于随机选的字符 */
	public static final String BASE_CHAR_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";

	/** 用于随机选的字符 */
	public static final String BASE_CHAR_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/** 用于随机选的字符和数字 */
	public static final String BASE_CHAR_NUMBER = BASE_NUMBER + BASE_CHAR_LOWER_CASE + BASE_CHAR_UPPER_CASE;

	private static final int SIZE = 6;            //字符数
	private static final int LINES = 50;        //干扰数量
	private static final int THICKNESS = 4;        //干扰宽度
	private static final int WIDTH = 120;        //生成的验证码图片的宽度
	private static final int HEIGHT = 49;        //生成的验证码图片的长度
	private static final CaptchaShapeEnum SHAPE_ENUM = CaptchaShapeEnum.SHEAR;        //干扰类型

	/**
	 * 生成验证码图片
	 * @author: luzhichao
	 * @param
	 * @return: java.util.Map<java.lang.String,java.awt.image.BufferedImage>
	 * @date: 2021/5/24 17:58
	 */
	public static ValidateProperty createImage(){
		return createImage(null, null, null, null, SHAPE_ENUM);
	}

	/**
	 * 生成验证码图片
	 * @author: luzhichao
	 * @param
	 * @return: java.util.Map<java.lang.String,java.awt.image.BufferedImage>
	 * @date: 2021/5/24 17:58
	 */
	public static ValidateProperty createImage(CaptchaShapeEnum shapeEnum){
		return createImage(null, null, null, null, shapeEnum);
	}

	/**
	 * 生成验证码图片
	 * @author: luzhichao
	 * @param codeCount 字符个数
	 * @return: java.util.Map<java.lang.String,java.awt.image.BufferedImage>
	 * @date: 2021/5/24 17:58
	 */
	public static ValidateProperty createImage(Integer codeCount){
		return createImage(codeCount, null, null, null, SHAPE_ENUM);
	}

	/**
	 * 生成验证码图片
	 * @author: luzhichao
	 * @param codeCount 字符个数
	 * @return: java.util.Map<java.lang.String,java.awt.image.BufferedImage>
	 * @date: 2021/5/24 17:58
	 */
	public static ValidateProperty createImage(Integer codeCount, CaptchaShapeEnum shapeEnum){
		return createImage(codeCount, null, null, null, shapeEnum);
	}

	/**
	 * 生成验证码图片
	 * @author: luzhichao
	 * @param codeCount 字符个数
	 * @param shapeValue 干扰个数\宽度
	 * @param width 宽度
	 * @param height 长度
	 * @param shapeEnum 干扰线类型
	 * @return: ValidateProperty
	 * @date: 2021/5/24 17:57
	 */
	public static ValidateProperty createImage(@Nullable Integer codeCount, @Nullable Integer shapeValue,
											   @Nullable Integer width, @Nullable Integer height, CaptchaShapeEnum shapeEnum){
		if (codeCount == null || codeCount == 0) {
			codeCount = SIZE;
		}
		if (width == null || width == 0) {
			width = WIDTH;
		}
		if (height == null || height == 0) {
			height = HEIGHT;
		}
		String code;
		String imageBase64;
		BufferedImage imageData;
		if (shapeEnum == CaptchaShapeEnum.LINE) {
			// 线干扰
			if (shapeValue == null || shapeValue == 0) {
				shapeValue = LINES;
			}
			LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(width, height, codeCount, shapeValue);
			code = lineCaptcha.getCode();
			imageBase64 = lineCaptcha.getImageBase64();
			imageData = lineCaptcha.getImage();
		} else if (shapeEnum == CaptchaShapeEnum.CIRCLE) {
			// 圆圈干扰
			if (shapeValue == null || shapeValue == 0) {
				shapeValue = LINES;
			}
			CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(width, height, codeCount, shapeValue);
			code = circleCaptcha.getCode();
			imageBase64 = circleCaptcha.getImageBase64();
			imageData = circleCaptcha.getImage();
		} else {
			// 扭曲干扰
			if (shapeValue == null || shapeValue == 0) {
				shapeValue = THICKNESS;
			}
			ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(width, height, codeCount, shapeValue);
			code = shearCaptcha.getCode();
			imageBase64 = shearCaptcha.getImageBase64();
			imageData = shearCaptcha.getImage();
		}
		ValidateProperty property = ValidateProperty.builder()
			.code(code)
			.imageData(imageData)
			.imageBase64Data(imageBase64)
			.build();
		return property;
	}

	/**
	 * 获取字符串验证码
	 * @author: luzhichao
	 * @param
	 * @return: java.lang.String
	 * @date: 2021/5/25 10:10
	 */
	public static String getStrCode() {
		return RandomUtil.randomString(SIZE);
	}

	/**
	 * 获取字符串验证码
	 * @author: luzhichao
	 * @param count 数量
	 * @param ignoreCapital 忽略大小写
	 * @return: java.lang.String
	 * @date: 2021/5/25 10:10
	 */
	public static String getStrCode(int count, boolean ignoreCapital) {
		if (count == 0) {
			count = 1;
		}
		if (ignoreCapital) {
			return RandomUtil.randomString(count);
		}
		return RandomUtil.randomString(BASE_CHAR_NUMBER, count);
	}

	/**
	 * 获取数字验证码
	 * @author: luzhichao
	 * @param
	 * @return: java.lang.String
	 * @date: 2021/5/25 10:14
	 */
	public static String getNumberCode() {
		return RandomUtil.randomNumbers(SIZE);
	}

	/**
	 * 获取数字验证码
	 * @author: luzhichao
	 * @param count 数量
	 * @return: java.lang.String
	 * @date: 2021/5/25 10:14
	 */
	public static String getNumberCode(int count) {
		if (count == 0) {
			count = 1;
		}
		return RandomUtil.randomNumbers(count);
	}


}
