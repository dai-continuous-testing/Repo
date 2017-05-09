package com.experitest.repo;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Repository {
	private String folderName = null;
	public Repository(String folderName){
		this.folderName = folderName;
		if(folderName == null){
			this.folderName = "repo";
		}
	}
	
	public By obj(String key){
		Properties prop = prop(key);
		String expression = prop.getProperty("expression");
		switch(prop.getProperty("by")){
		case "xpath":
			return By.xpath(expression);
		case "id":
			return By.id(expression);
		case "class":
			return By.className(expression);
		case "name":
			return By.name(expression);
		case "link":
			return By.linkText(expression);
		case "partialLink":
			return By.partialLinkText(expression);
		case "tag":
			return By.tagName(expression);
		case "css":
			return By.cssSelector(expression);
		default:
			throw new RuntimeException("Cannot recongnise By method: " + prop.getProperty("by") +", supported methods are xpath, id, class, name, link, partialLink, tag and css");
		}
	}
	public String zone(String key){
		return new Props(prop(key)).context;
	}
	public String element(String key){
		return new Props(prop(key)).element;
	}

	public Properties prop(String key) {
		File repoDir = new File(folderName);
		if(!repoDir.exists()){
			repoDir = new File(System.getProperty("user.dir"), folderName);
			if(!repoDir.exists()){
				throw new RuntimeException("Cannot allocate repository directory: " + folderName + ", open a folder name " + folderName + " in your java project root");			
			}
		}
		File propFile = new File(repoDir, key + ".obj");
		Properties prop = new Properties();
		try {
			FileReader fr = new FileReader(propFile);
			prop.load(fr);
			fr.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if(!prop.containsKey("by") || !prop.containsKey("expression")){
			throw new RuntimeException("by or expression properties were not set");
		}
		return prop;
	}

	public void verifyPage(WebDriver driver, String pageName) {
		File repoDir = new File(folderName);
		if(!repoDir.exists()){
			repoDir = new File(System.getProperty("user.dir"), folderName);
			if(!repoDir.exists()){
				throw new RuntimeException("Cannot allocate repository directory: " + folderName + ", open a folder name " + folderName + " in your java project root");			
			}
		}
		File[] files = repoDir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().startsWith(pageName + ".") && pathname.getName().endsWith(".obj");
			}
		});
		if(files.length == 0){
			throw new RuntimeException("No objects were found for page " + pageName);
		}
		for(File f: files){
			driver.findElement(obj(f.getName().substring(0, f.getName().length() - 4)));
		}
	}
}
class Props{
	public final String context;
	public final String element;
	
	public Props(Properties prop){
		String _context = "NATIVE";
		if(prop.containsKey("context") && prop.getProperty("context").toLowerCase().contains("web")){
			_context = "WEB";
		}
		context = _context;
		String by = prop.getProperty("by");
		String expression = prop.getProperty("expression");
		if("class".equals(by)){
			element = "xpath=" + "//*[@class='" + expression + "']";
		} else if("name".equals(by)){
				element = "xpath=" + "//*[@name='" + expression + "']";
		} else if("link".equals(by)){
			element = "text=" + expression;
		} else if("partialLink".equals(by)){
			element = "partial_text=" + expression;
		} else {
			element = by + "=" + expression;
		}
	}
}

