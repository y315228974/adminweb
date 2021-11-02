package com.lz.adminweb.utils.Validate;

import lombok.Builder;
import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * @Auther:luzhichao
 * @Date:2021/5/2509:03
 * @Description: 验证码参数
 */
@Data
@Builder
public class ValidateProperty {

	/** 验证码 */
	private String code;

	/** 图片数据*/
	private BufferedImage imageData;

	/** 图片数据 base64 */
	private String imageBase64Data;
}
