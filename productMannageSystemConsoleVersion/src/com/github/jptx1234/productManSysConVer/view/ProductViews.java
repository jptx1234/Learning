package com.github.jptx1234.productManSysConVer.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import com.github.jptx1234.productManSysConVer.control.ProductsCtrl;
import com.github.jptx1234.productManSysConVer.exception.ProductControlException;
import com.github.jptx1234.productManSysConVer.exception.XMLFileException;

import jline.console.KeyMap;
import jline.console.UserInterruptException;

public class ProductViews {
	ProductsCtrl productsCtrl = new ProductsCtrl();
	ViewFrame viewFrame = new ViewFrame();
	
	public static void main(String[] args) {
		String XMLFileName="products.xml";
		if (args.length != 0) {
			XMLFileName = args[0];
		}
		new ProductViews(XMLFileName);
	}
	
	public ProductViews(String XMLFileName){
		try {
			productsCtrl.setXMLFile(XMLFileName);
		} catch (XMLFileException e) {
			e.printStackTrace();
		}
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler () {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				errorView("内部错误\r\n"+e.getLocalizedMessage());
			}
		});
		mainView();
	}
	
	public void mainView(){
		boolean ok = false;
		while (!ok) {
			viewFrame.show(ViewType.NORMAL);
			viewFrame.showCenter("\u001b[37;1m\r\n\r\n\r\n请输入操作序号：\r\n1.添加商品\r\n2.查找商品\r\n3.商品列表\r\n0.退出系统");
//			viewFrame.showNormal("\u001b[0m");
//			viewFrame.showInfoInLine(msg, 3);
//			viewFrame.showWhiteLine(2);
			char op = 255;
//			viewFrame.setPrompt("\u001b[37;1m请输入操作序号 >\u001b[36;1m");
			try {
				op=viewFrame.readChar("\u001b[37;1m请输入操作序号 >\u001b[36;1m", new char[]{'1','2','3','4','0'});
			} catch (UserInterruptException e) {
			}
			switch (op) {
			case '1':
				ok = true;
				addView("","","","",0);
				break;
			case '2':
				ok = true;
				findView();
				break;
			case '3':
				ok = true;
				listView();
				break;
			case '0':
				viewFrame.clearScreen();
				System.exit(0);
				
			default:
				continue;
			}
			
		}
	}
	
	public void addView(String name,String price,String num,String description,int index){
		index = Math.min(Math.max(0, index), 3);
		final int oldIndex=index;
		boolean ok = false;
		while (!ok) {
			viewFrame.show(ViewType.ADD);
			
			viewFrame.showProductInfo(name, price, num, description);
//			viewFrame.showInfoInLine("Enter 提交更改，Ctrl+S 保存当前商品，Ctrl+U 上一个属性，Ctrl+J 下一个属性，Ctrl+C 回到主界面", 3);
//			viewFrame.showWhiteLine(2);
			KeyMap keyMap = viewFrame.getConsole().getKeys();
			keyMap.bind(""+KeyMap.CTRL_U, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (viewFrame.nowView==ViewType.ADD) {
						addView(name, price, num, description,oldIndex-1);
					}
				}
			});
			keyMap.bind(""+KeyMap.CTRL_J, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (viewFrame.nowView==ViewType.ADD) {
						addView(name, price, num, description,oldIndex+1);
					}
				}
			});
			keyMap.bind(""+KeyMap.CTRL_S, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (viewFrame.nowView==ViewType.ADD) {
						try {
							String uuid=productsCtrl.addProduct(name, price, num, description);
							infoView("添加成功，商品ID为\r\n"+uuid+"\r\n按回车键继续");
							mainView();
						} catch (ProductControlException e1) {
							errorView("添加商品失败\r\n"+e1.getLocalizedMessage());
							addView(name, price, num, description, oldIndex);
						}
					}
				}
			});
			String input = "";
			try {
				switch (index) {
				case 0:
//					viewFrame.resetPromptLine("\u001b[36;1m商品名称：\u001b[37;1m", name);
//					viewFrame.setPrompt("");
					input=viewFrame.readLine("\u001b[36;1m商品名称：\u001b[37;1m",name);
					addView(input, price, num, description, index+1);
					break;
				case 1:
//					viewFrame.resetPromptLine("\u001b[36;1m商品价格：\u001b[37;1m", price);
					input=viewFrame.readLine("\u001b[36;1m商品价格：\u001b[37;1m", price);
					addView(name, input, num, description, index+1);
					break;
				case 2:
//					viewFrame.resetPromptLine("\u001b[36;1m商品数量：\u001b[37;1m", num);
					input=viewFrame.readLine("\u001b[36;1m商品数量：\u001b[37;1m", num);
					addView(name, price, input, description, index+1);
					break;
				case 3:
//					viewFrame.resetPromptLine("\u001b[36;1m商品描述：\u001b[37;1m", description);
					input=viewFrame.readLine("\u001b[36;1m商品描述：\u001b[37;1m", description);
					addView(name, price, num, input, 0);
					break;
					

				default:
					break;
				}
				
			} catch (UserInterruptException  e) {
				mainView();
			}
		}
		
	}
	
	public void listView(){
		viewFrame.show(ViewType.LIST);
		viewFrame.readChar("", new char[]{'w','s','a','d','e'});
		
	}
	
	public void deleteView(){
		viewFrame.show(ViewType.DELETE);
//		viewFrame.showCenter("删除商品测试");
//		viewFrame.readLine();
		
	}
	
	public void findView(){
		viewFrame.show(ViewType.FIND);
		viewFrame.showCenter("查找商品测试");
		viewFrame.readLine(">","");
		
	}
	
	public void updateView(){
		viewFrame.show(ViewType.UPDATE);
		viewFrame.showCenter("编辑商品测试");
		viewFrame.readLine(">","");
		
	}
	
	public void errorView(String msg){
		viewFrame.show(ViewType.ERROR);
		viewFrame.showCenter(msg);
//		viewFrame.setPrompt("");
		viewFrame.readLine("","");
	}
	
	public void infoView(String info){
		viewFrame.show(ViewType.NORMAL);
		viewFrame.showCenter("\r\n\r\n\r\n"+info);
		viewFrame.readLine("", "");
	}
	
}
