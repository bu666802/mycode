package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;


public class SystemProperties {
	
	private static SystemProperties instance ;

	//private static final Logger logger = LoggerFactory.getLogger(Thread.class); 
	private Logger logger = Logger.getLogger(this.getClass());
	
	private  Properties properties  = null; 
	private  String  propertiesName = "config-system.perperties";
	

	public SystemProperties(String propertiesName ){
		this.propertiesName = propertiesName;
	}
	
	
	public static SystemProperties getInstance(){
		if(instance==null) instance = new SystemProperties("config-system.properties");
		return instance;
	}
	
	
	public   void load() {
		
		
		File file = new File(propertiesName);
		
		try {
			
			if(file.exists()){
				
				InputStream inputStream =new FileInputStream(propertiesName);
			   	InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			   	Properties languagesProperties = new Properties();
				languagesProperties.load(inputStreamReader); 
			   	inputStream.close();
			   	inputStreamReader.close();
			   	properties = languagesProperties;
				
				
			}else{
				
				
				
				InputStream inputStream = SystemProperties.class.getClassLoader().getResourceAsStream(propertiesName);
			   	InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			   	Properties languagesProperties = new Properties();
				languagesProperties.load(inputStreamReader); 
			   	inputStream.close();
			   	inputStreamReader.close();
			   	properties = languagesProperties;
				
			}
		
			
			
		} catch (Exception e) {
			logger.error(e.toString(),e);
		}
		
	   	
		
	}

	

	public String getProperty(String string) {
		
		if(properties==null) load();
		
		return properties.getProperty(string);
		
	}
	public Integer getInteger(String string) {
		
		if(properties==null) load();
		
		return Integer.valueOf(properties.getProperty(string));
		
	}
	
	
	
	public static void main(String[] args) {
		
		
		
		
		//用法一
		//String version = SystemProperties.getInstance().getProperty("version");
		
		//用法二
		//System.out.println(version);
		SystemProperties systemProperties= new SystemProperties("wk_categeroy.properties");
		systemProperties.getProperty("version");
		
		
	}



}
