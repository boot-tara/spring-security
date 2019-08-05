package com.boot.security.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Dict extends BaseEntity<Long> {

	private static final long serialVersionUID = -2431140186410912787L;
	private String type;
	private String k;
	private String val;
}
