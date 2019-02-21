package com.ais.sys.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.ais.sys.models.Picture;

@Component
public class ImageUtils {
	
	String host = PropertiesUtils.getValue(ConstantUtil.PropertiesKeys.IMAGE_HOST);
	String rootPath = PropertiesUtils.getValue(ConstantUtil.PropertiesKeys.IMAGE_ROOT_PATH);
	
	public Picture resizeImage(String imagePath, String name, String sub) throws IOException {
		String fmt = getImageFormatByPath(imagePath);
		if(fmt == null) return null;
		Picture pic = new Picture();
		pic.setPicName(name);
		pic.setPicSubPath(sub);
		pic.setHost(PropertiesUtils.getValue(ConstantUtil.PropertiesKeys.IMAGE_HOST));
		String fileName = UUID.randomUUID().toString();
		String[] fileNames = new String[3];
		fileNames[0] = fileName + ConstantUtil.ImageSubfix.MIN_SUBFIX;
		fileNames[1] = fileName + ConstantUtil.ImageSubfix.MEDIUM_SUBFIX;
		fileNames[2] = fileName + ConstantUtil.ImageSubfix.MAX_SUBFIX;
		
		File file = new File(imagePath);
		InputStream is = null;
		BufferedImage prevImage = null;
		try {
			is = new FileInputStream(file);
			prevImage = ImageIO.read(is);
		} catch (IOException e) {
			return null;
		} finally {
			if (is != null)
				try { is.close(); } catch (IOException e) {}
		}
		double width = prevImage.getWidth();
		int size = 3;
		if(width <= ConstantUtil.ImageSize.MIN_SIZE) size = 1;
		else if(width <= ConstantUtil.ImageSize.MEDIUM_SIZE) size = 2;
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < size; i++) {
			boolean flag = writeImage(file, rootPath, sub, fileNames[i], i, fmt);
			if(flag) {
				sb.append(",").append(fileNames[i] + "." + fmt);
			}
			
		}
		if(sb.length() > 0) sb.deleteCharAt(0);
		pic.setPicSubMinPath(sb.toString());
		return pic;
	}
	
	private boolean writeImage(File file, String root, String sub, String item, int index, String format) throws IOException {
		String outPath = root + "/" + (sub==null? "":sub) + "/" + item + "." + format;
		File outFile = new File(outPath);
		if(!outFile.getParentFile().exists()) {
			outFile.getParentFile().mkdirs();
		}
		FileOutputStream os = new FileOutputStream(outPath);
		boolean shouldCopy = false;
		if(index == 0) {
			shouldCopy = resizeImage(new FileInputStream(file), os, ConstantUtil.ImageSize.MIN_SIZE, format);
		} else if (index == 1) {
			shouldCopy = resizeImage(new FileInputStream(file), os, ConstantUtil.ImageSize.MEDIUM_SIZE, format);
		} else {
			shouldCopy = resizeImage(new FileInputStream(file), os, ConstantUtil.ImageSize.MAX_SIZE, format);
		}
		if(shouldCopy) FileUtils.copyFile(file, outFile);
		return true;
	}

	public Boolean resizeImage(InputStream is, OutputStream os, int size, String format) throws IOException {
		try {
	        BufferedImage prevImage = ImageIO.read(is);
	        double width = prevImage.getWidth();
	        double height = prevImage.getHeight();
	        double percent = size/width;
	        /*if(percent >= 1) {
	        	return true;
	        } else {
		        int newWidth = (int)(width * percent);
		        int newHeight = (int)(height * percent);
		        BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);
		        Graphics graphics = image.createGraphics();
		        graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);
		        ImageIO.write(image, format, os);
	        }*/
	        int newWidth = (int)(width * percent);
	        int newHeight = (int)(height * percent);
	        BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);
	        Graphics graphics = image.createGraphics();
	        graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);
	        ImageIO.write(image, format, os);
		} finally {
			os.flush();
	        is.close();
	        os.close();
		}
//      ByteArrayOutputStream b = (ByteArrayOutputStream) os;
        return false;
    }
	
	public static String getImageFormatByPath(String imagePath) {
		if(imagePath != null) {
			String fmt = imagePath.substring(imagePath.lastIndexOf(".") + 1);
			if(fmt != null) return fmt;
		}
		return null;
	}
}