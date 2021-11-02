package com.lz.adminweb.utils.Validate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 图像验证码干扰类型
 * @Auther:luzhichao
 * @Date:2021/5/25 09:53
 * @Description:
 */
@AllArgsConstructor
@Getter
public enum CaptchaShapeEnum {

	LINE(1, "线干扰"),
	CIRCLE(2, "圆圈干扰"),
	SHEAR(3, "扭曲干扰");

	private int code;

	private String value;

	public static CaptchaShapeEnum formValue(int code) {
		for (CaptchaShapeEnum value : CaptchaShapeEnum.values()) {
			if (code == value.getCode()) {
				return value;
			}
		}
		return null;
	}
}
