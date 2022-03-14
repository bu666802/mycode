package com.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CheckUtil;
import com.SystemConfig;

public class WebSession {
	
	static File sessionFile =null;
	static{
		
		String sessionSavePath = SystemConfig.getInstance().getProperty("uploadSavePath")+"session";
		sessionFile = new File(sessionSavePath);
		if(!sessionFile.exists())sessionFile.mkdirs();
		
	}
	
	
	public static void  setAttribute(HttpServletRequest req,HttpServletResponse response   ,String  name  ,Object data ) throws Exception {
		
		
		
		String coolkieId = findCookieName(req,"manager-cookie-id");
		if(CheckUtil.isEmpty(coolkieId))  coolkieId =  UUID.randomUUID().toString();
		Cookie  cookie = new Cookie("manager-cookie-id",coolkieId);
//		cookie.setMaxAge(Integer.MAX_VALUE); // 设置生命周期为MAX_VALUE
		response.addCookie(cookie);
		File saveFile = new File(sessionFile,name+"/"+coolkieId);
		if(!saveFile.getParentFile().exists())saveFile.getParentFile().mkdirs();
		if(data==null) 
			saveFile.delete();
		else
			save(saveFile,data);
		
		
	}
	public static Object getAttribute(HttpServletRequest req , String  name  ) throws Exception {
		
		String coolkieId = findCookieName(req,"manager-cookie-id");
		//System.out.println("coolkieId: "+coolkieId);
		
		if(CheckUtil.isEmpty(coolkieId))  return null;
		File saveFile = new File(sessionFile,name+"/"+coolkieId);
		//System.out.println("coolkieId: exists:"+saveFile.exists());
		if(!saveFile.exists())  return null;
		return read(saveFile);
		
		
	}
	
	public static LinkedList<Object> getAllAttribute(String  name  ) throws Exception{
		File[] saveFile = new File(sessionFile,name).listFiles();
		//System.out.println("coolkieId: exists:"+saveFile.exists());
		LinkedList<Object> oList= new LinkedList<Object>();
		for (File file : saveFile) {
			Object o = read(file);
			oList.add(o);
		}
		
		return oList;
	}
	
	private static String findCookieName(HttpServletRequest req , String  name) {
		Cookie[] cks = req.getCookies();
		if(cks==null)return null;
		for (int i = 0; i < cks.length; i++) {
			String ckName = cks[i].getName();
			if(name.equals(ckName)) return cks[i].getValue();
			
		}
		return null;
		
	}
	
	
	private static  void save( File file ,Object data  ) throws Exception{
		
		FileOutputStream   fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(data);
		oos.flush();
		oos.close();
		fos.close();
		 
		
	}

	public static Object  read (File file) throws Exception{
	
	 
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object deTest = ois.readObject();
		ois.close();
		fis.close();
		return  deTest;
	
	}
	
	
	
	

}
