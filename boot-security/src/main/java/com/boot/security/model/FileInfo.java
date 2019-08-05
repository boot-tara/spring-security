package com.boot.security.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileInfo extends BaseEntity<String> {

	private static final long serialVersionUID = -5761547882766615438L;

	private String contentType;
	private long size;
	private String path;
	private String url;
	private Integer type;
}
