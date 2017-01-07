package com.github.jptx1234.productManSysConVer.utils;

import java.io.IOException;
import java.io.Writer;

import jline.Terminal;
import jline.console.ConsoleReader;
import jline.console.UserInterruptException;

public class JLineUtil {
	private ConsoleReader consoleReader = null;
	private  Writer writer = null;
	private int screenWidth = 0,screenHeight = 0;
	private String prompt="";
	
	public JLineUtil(){
		try {
			consoleReader = new ConsoleReader(System.in, System.out);
			consoleReader.setHandleUserInterrupt(true);
			writer = consoleReader.getOutput();
			Terminal terminal = consoleReader.getTerminal();
			screenWidth = terminal.getWidth();
			screenHeight = terminal.getHeight();
		} catch (IOException e) {
			System.out.println("JLine初始化失败！10秒后自动退出。"+e.getLocalizedMessage());
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
			}
			System.exit(1);
		}
	}
	
	public void outPutRAWmsg(String msg){
		try {
			writer.write(msg);
			writer.flush();
		} catch (IOException e) {
			System.out.println("JLine 输出信息失败："+e.getLocalizedMessage());
		}
	}
	
	public void setPrompt(String prompt){
		this.prompt = prompt;
	}
	
	public String readLine() throws UserInterruptException{
		String input = "";
		try {
			input=consoleReader.readLine(prompt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}
	
	public void clearScreen(){
		outPutRAWmsg("\u001b[0m\u001b[2J\u001b[1;1H");
//		try {
//			consoleReader.clearScreen();
//		} catch (IOException e) {
//		}
	}
	public void clearLine(){
		outPutRAWmsg("\u001b[2K");
	}
	public int getWidth(){
		return screenWidth;
	}
	public int getHeight(){
		return screenHeight;
	}
	public ConsoleReader getConsoleReader(){
		return consoleReader;
	}
	public void newLine(){
		try {
			consoleReader.println();
		} catch (IOException e) {
		}
	}
}
