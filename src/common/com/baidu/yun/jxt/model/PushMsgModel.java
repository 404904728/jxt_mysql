package com.baidu.yun.jxt.model;

import java.util.Map;

public class PushMsgModel {

	// 标题
	private String title;

	// 描述
	private String description;

	/**
	 * Map<String, Object> maps = new HashMap<String, Object>();
	 * maps.put("type", 2);
	 */
	// android自定义参数
	private Map<String, Object> custom_content;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Object> getCustom_content() {
		return custom_content;
	}

	public void setCustom_content(Map<String, Object> custom_content) {
		this.custom_content = custom_content;
	}
}
