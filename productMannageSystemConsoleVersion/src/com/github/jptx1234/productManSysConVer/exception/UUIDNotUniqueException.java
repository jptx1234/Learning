package com.github.jptx1234.productManSysConVer.exception;

public class UUIDNotUniqueException extends Exception{
	public UUIDNotUniqueException(String uuid){
		super("有多个商品的UUID以"+uuid+"开头");
	}
}
