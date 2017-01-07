package com.github.jptx1234.productManSysConVer.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.dom4j.DocumentException;

import com.github.jptx1234.productManSysConVer.domain.Product;
import com.github.jptx1234.productManSysConVer.exception.NoSuchProductException;
import com.github.jptx1234.productManSysConVer.exception.ProductControlException;
import com.github.jptx1234.productManSysConVer.exception.UUIDNotUniqueException;
import com.github.jptx1234.productManSysConVer.exception.XMLFileException;
import com.github.jptx1234.productManSysConVer.model.ProductXMLOPs;
import com.github.jptx1234.productManSysConVer.utils.Utils;

public class ProductsCtrl {
	ProductXMLOPs op = new ProductXMLOPs();
	Product lastDelProduct = null;
	
	public ProductsCtrl() {
		
	}
	
	public void setXMLFile(String fileName) throws XMLFileException{
		op.setXMLFile(fileName);
	}
	public ProductControlException documentExceptionTrans(DocumentException e){
		return new ProductControlException("XML�ļ���ȡʱ����"+e.getLocalizedMessage());
	}
	public ProductControlException ioExceptionTrans(IOException e){
		return new ProductControlException("XML�ļ�����ʱ����"+e.getLocalizedMessage());
	}
	private String encodeBase64(String string) throws ProductControlException{
		try {
			return Base64.getEncoder().encodeToString(string.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new ProductControlException("�ڲ��������");
		}
	}
	private String decodeBase64(String base64) throws ProductControlException{
		try {
			return new String(Base64.getDecoder().decode(base64),"utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ProductControlException("�ڲ��������");
		}
	}
	
	public String addProduct(String name,String price,String num,String description) throws ProductControlException{
		try {
			Double.parseDouble(price);
		} catch (Exception e) {
			throw new ProductControlException("�趨�ļ۸�"+price+"����һ����ȷ������");
		}
		try {
			Integer.parseInt(num);
		} catch (Exception e) {
			throw new ProductControlException("�趨������"+num+"����һ����ȷ������");
		}
		description = encodeBase64(description);
		name = encodeBase64(name);
		String uuid=Utils.getRandomUUID();
		Product product = new Product(uuid,name,price,num,description);
		try {
			op.addProduct(product);
		} catch (DocumentException e) {
			throw documentExceptionTrans(e);
		} catch (IOException e) {
			throw ioExceptionTrans(e);
		}
		
		return uuid;
	}
	
	public void delProduct(String uuid) throws ProductControlException{
		try {
			lastDelProduct = op.deleteProduct(uuid);
		} catch (DocumentException e) {
			throw documentExceptionTrans(e);
		} catch (IOException e) {
			throw ioExceptionTrans(e);
		} catch (NoSuchProductException e) {
			throw new ProductControlException("��Ʒ"+uuid+"������");
		} catch (UUIDNotUniqueException e) {
			throw new ProductControlException(e.getLocalizedMessage());
		}
	}
	
	public Product findProduct(String uuid) throws ProductControlException{
		try {
			Product product= op.findProduct(uuid);
			product.setDescription(decodeBase64(product.getDescription()));
			product.setName(decodeBase64(product.getName()));
			return product;
		} catch (DocumentException e) {
			throw documentExceptionTrans(e);
		} catch (UUIDNotUniqueException e) {
			throw new ProductControlException(e.getLocalizedMessage());
		} 
	}
	
	public void updateProduct(Product product) throws ProductControlException{
		try {
			product.setDescription(encodeBase64(product.getDescription()));
			product.setName(encodeBase64(product.getName()));
			op.updateProduct(product);
		} catch (DocumentException e) {
			throw documentExceptionTrans(e);
		} catch (IOException e) {
			throw ioExceptionTrans(e);
		}
	}
	
	
	

}
