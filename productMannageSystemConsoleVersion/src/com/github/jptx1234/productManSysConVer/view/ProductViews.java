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
				errorView("�ڲ�����\r\n"+e.getLocalizedMessage());
			}
		});
		mainView();
	}
	
	public void mainView(){
		boolean ok = false;
		while (!ok) {
			viewFrame.show(ViewType.NORMAL);
			viewFrame.showCenter("\u001b[37;1m\r\n\r\n\r\n�����������ţ�\r\n1.�����Ʒ\r\n2.������Ʒ\r\n3.��Ʒ�б�\r\n0.�˳�ϵͳ");
//			viewFrame.showNormal("\u001b[0m");
//			viewFrame.showInfoInLine(msg, 3);
//			viewFrame.showWhiteLine(2);
			char op = 255;
//			viewFrame.setPrompt("\u001b[37;1m������������ >\u001b[36;1m");
			try {
				op=viewFrame.readChar("\u001b[37;1m������������ >\u001b[36;1m", new char[]{'1','2','3','4','0'});
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
//			viewFrame.showInfoInLine("Enter �ύ���ģ�Ctrl+S ���浱ǰ��Ʒ��Ctrl+U ��һ�����ԣ�Ctrl+J ��һ�����ԣ�Ctrl+C �ص�������", 3);
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
							infoView("��ӳɹ�����ƷIDΪ\r\n"+uuid+"\r\n���س�������");
							mainView();
						} catch (ProductControlException e1) {
							errorView("�����Ʒʧ��\r\n"+e1.getLocalizedMessage());
							addView(name, price, num, description, oldIndex);
						}
					}
				}
			});
			String input = "";
			try {
				switch (index) {
				case 0:
//					viewFrame.resetPromptLine("\u001b[36;1m��Ʒ���ƣ�\u001b[37;1m", name);
//					viewFrame.setPrompt("");
					input=viewFrame.readLine("\u001b[36;1m��Ʒ���ƣ�\u001b[37;1m",name);
					addView(input, price, num, description, index+1);
					break;
				case 1:
//					viewFrame.resetPromptLine("\u001b[36;1m��Ʒ�۸�\u001b[37;1m", price);
					input=viewFrame.readLine("\u001b[36;1m��Ʒ�۸�\u001b[37;1m", price);
					addView(name, input, num, description, index+1);
					break;
				case 2:
//					viewFrame.resetPromptLine("\u001b[36;1m��Ʒ������\u001b[37;1m", num);
					input=viewFrame.readLine("\u001b[36;1m��Ʒ������\u001b[37;1m", num);
					addView(name, price, input, description, index+1);
					break;
				case 3:
//					viewFrame.resetPromptLine("\u001b[36;1m��Ʒ������\u001b[37;1m", description);
					input=viewFrame.readLine("\u001b[36;1m��Ʒ������\u001b[37;1m", description);
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
//		viewFrame.showCenter("ɾ����Ʒ����");
//		viewFrame.readLine();
		
	}
	
	public void findView(){
		viewFrame.show(ViewType.FIND);
		viewFrame.showCenter("������Ʒ����");
		viewFrame.readLine(">","");
		
	}
	
	public void updateView(){
		viewFrame.show(ViewType.UPDATE);
		viewFrame.showCenter("�༭��Ʒ����");
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
