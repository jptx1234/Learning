package com.github.jptx1234.productManSysConVer.view;

import java.io.IOException;

import com.github.jptx1234.productManSysConVer.utils.JLineUtil;

import jline.console.ConsoleReader;
import jline.console.UserInterruptException;

public class ViewFrame {
	final String VERSIONINFO="商品管理系统";
	final String NORMALTITLE="消息";
	final String ADDTITLE="添加";
	final String DELETETITLE="删除";
	final String UPDATETITLE="修改";
	final String FINDTITLE="查找";
	final String ERRTITLE="错误";
	final String LISTTITLE="列表";
	JLineUtil jLineUtil = null;
	int screenWidth,screenHeight;
	ConsoleReader consoleReader = null;
	public volatile ViewType nowView = null;
	
	public ViewFrame(){
		jLineUtil = new JLineUtil();
		screenWidth = jLineUtil.getWidth()-1;
		screenHeight = jLineUtil.getHeight()-1;
		consoleReader=jLineUtil.getConsoleReader();
	}
	
	public void show(ViewType type){
		switch (type) {
		case NORMAL:
			nowView=ViewType.NORMAL;
			showTitle("\u001b[30;47m", VERSIONINFO+" - "+NORMALTITLE);
			showBottom("");
			moveCursorToContent();
			break;
		case ADD:
			nowView=ViewType.ADD;
			showTitle("\u001b[42m\u001b[37;1m", VERSIONINFO+" - "+ADDTITLE);
			showBottom("Enter 提交更改，Ctrl+S 保存当前商品，Ctrl+U 上一个属性，Ctrl+J 下一个属性，Ctrl+C 回到主界面");
			moveCursorToContent();
			break;
		case DELETE:
			nowView=ViewType.DELETE;
			showTitle("\u001b[45;1m\u001b[37;1m", VERSIONINFO+" - "+DELETETITLE);
			showBottom("Ctrl+C 回到主界面");
			moveCursorToContent();
			break;
		case FIND:
			nowView=ViewType.FIND;
			showTitle("\u001b[46;1m\u001b[37;1m", VERSIONINFO+" - "+FINDTITLE);
			showBottom("Ctrl+C 回到主界面");
			moveCursorToContent();
			break;
		case UPDATE:
			nowView=ViewType.UPDATE;
			showTitle("\u001b[43;1m\u001b[37;1m", VERSIONINFO+" - "+UPDATETITLE);
			showBottom("Ctrl+C 回到主界面");
			moveCursorToContent();
			break;
		case ERROR:
			nowView=ViewType.ERROR;
			showTitle("\u001b[41;1m\u001b[37;1m", VERSIONINFO+" - "+ERRTITLE);
			showBottom("Enter 确定，Ctrl+C 回到主界面");
			moveCursorToContent();
			break;
		case LIST:
			nowView=ViewType.LIST;
			showTitle("\u001b[44;1m\u001b[37;1m", VERSIONINFO+" - "+LISTTITLE);
			showBottom("w 上一项，s 下一项，a 上一页，d 下一页，e 编辑商品，Ctrl+D 删除商品，Ctrl+C 主界面");
			moveCursorToContent();
			break;

		default:
			break;
		}
	}
	
	private void showTitle(String pre,String title){
		int titleLength = title.length();
		StringBuilder sb = new StringBuilder();
		sb.append(pre);
		int space = (screenWidth - titleLength )/2-4;
		for (int i = 0; i < space; i++) {
			sb.append(" ");
		}
		sb.append(title);
		for (int i = 0; i < space; i++) {
			sb.append(" ");
		}
		sb.append("\u001b[0m");
		jLineUtil.clearScreen();
		jLineUtil.outPutRAWmsg(sb.toString());
		jLineUtil.newLine();
	}
	private void showBottom(String keyTip){
		showInfoInLine(keyTip, 3);
		showWhiteLine(2);
	}
	
	public void showCenter(String msg){
		String[] strings = msg.split("\r\n");
		for (String string : strings) {
			StringBuilder sb = new StringBuilder();
			int space = (screenWidth - string.length() )/2-4;
			for (int i = 0; i < space; i++) {
				sb.append(" ");
			}
			sb.append(string);
			jLineUtil.outPutRAWmsg(sb.toString());
			jLineUtil.newLine();
		}
	}
	public void showNormal(String msg){
		jLineUtil.outPutRAWmsg(msg);
	}
	private void moveCursorToContent(){
		jLineUtil.outPutRAWmsg("\u001b[0m\u001b[2;1H");
	}
	
	public void showInfoInLine(String msg,int lastLine){
		jLineUtil.outPutRAWmsg("\u001b[0m\u001b["+(screenHeight-(lastLine+1))+";1H\u001b[30;47m"+msg+"\u001b[0m");
		jLineUtil.newLine();
	}
	
	public void showWhiteLine(int lastLine){
		StringBuilder sb= new StringBuilder();
		sb.append("\u001b["+(screenHeight-(lastLine+1))+";1H");
		for (int i = 0; i < screenWidth-2; i++) {
			sb.append("-");
		}
		jLineUtil.outPutRAWmsg(sb.toString());
		jLineUtil.newLine();
	}
	public void showWhiteLine(){
		StringBuilder sb= new StringBuilder();
		sb.append("\u001b[1G\u001b[37;1m");
		for (int i = 0; i < screenWidth-2; i++) {
			sb.append("-");
		}
		sb.append("\u001b[0m");
		jLineUtil.outPutRAWmsg(sb.toString());
		jLineUtil.newLine();
		
	}
	public void showProductInfo(String name,String price,String num,String description){
		showNormal("\r\n\u001b[32;1m商品名称  \u001b[37;1m"+name+"\r\n");
		showWhiteLine();
		showNormal("\u001b[32;1m商品价格  \u001b[37;1m"+price+"\r\n");
		showWhiteLine();
		showNormal("\u001b[32;1m商品数量  \u001b[37;1m"+num+"\r\n");
		showWhiteLine();
		showNormal("\u001b[32;1m商品描述  \u001b[37;1m\r\n");
		showNormal("\t"+description+"\r\n");
	}
	
	public char readChar(String prompt,char[] allowed){
		jLineUtil.outPutRAWmsg("\u001b["+(screenHeight-1)+";1H");
		jLineUtil.clearLine();
		char input = 255;
		try {
			jLineUtil.outPutRAWmsg(prompt);
			input =(char) consoleReader.readCharacter(allowed);
		} catch (IOException e) {
		}
		
		return input;
	}
	
	public String readLine(String prompt,String buffer) throws UserInterruptException{
		jLineUtil.outPutRAWmsg("\u001b["+(screenHeight-1)+";1H");
		jLineUtil.clearLine();
		resetPromptLine(prompt, buffer);
		return jLineUtil.readLine();
	}
//	public String readLine() throws UserInterruptException{
//		jLineUtil.outPutRAWmsg("\u001b[0m\u001b["+(screenHeight-(lastLine+1))+";1H\u001b[30;47m"+msg+"\u001b[0m");
//		return jLineUtil.readLine();
//		return readSingleLine();
//	}
//	public void setPrompt(String prompt){
//		jLineUtil.setPrompt(prompt);
//	}
	public void resetPromptLine(String prompt,String buffer){
		try {
			consoleReader.resetPromptLine(prompt, buffer, -1);
		} catch (IOException e) {
		}
	}
	
	public ConsoleReader getConsole(){
		return consoleReader;
	}
	public void clearScreen(){
		jLineUtil.clearScreen();
	}
	
}
