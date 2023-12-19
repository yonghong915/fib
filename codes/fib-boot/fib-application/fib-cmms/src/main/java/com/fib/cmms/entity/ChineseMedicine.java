package com.fib.cmms.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 中药
 * 
 * @author fangyh
 * @version 1.0
 * @date 2023-11-06 10:42:27
 */

@TableName(value = "biz_chinese_medicine")
public class ChineseMedicine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8908667668086011574L;

	/** 主键 */
	@TableId(value = "id")
	private Long id;

	/** 名称 */
	@TableField(value = "name")
	private String name;

	/** 性味 */
	/** 归经 */
	/** 功效 */
	/** 临床应用 */
	/** 药性 */

	/** 图片 */
	@TableField(value = "img_url")
	private String imgUrl;

	/** 创建时间 */
	@TableField(fill = FieldFill.INSERT)
	private Date createDt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
}