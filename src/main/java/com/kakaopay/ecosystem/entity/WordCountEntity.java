package com.kakaopay.ecosystem.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordCountEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5459483243562675451L;
	private String keyword;
	private int count;

	public WordCountEntity(String keyword) {
		this.keyword = keyword;
	}

	public void plus() {
		count++;
	}

}
