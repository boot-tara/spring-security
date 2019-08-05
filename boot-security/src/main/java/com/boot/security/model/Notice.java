package com.boot.security.model;

import lombok.Data;
@Data
public class Notice  {
	private Integer id;
	private String title;
	private String content;
	private Integer status;
	private String createTime;
	private String updateTime;
}
