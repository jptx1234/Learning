package com.github.jptx1234.productManSysConVer.exception;

public class UUIDNotUniqueException extends Exception{
	public UUIDNotUniqueException(String uuid){
		super("�ж����Ʒ��UUID��"+uuid+"��ͷ");
	}
}
