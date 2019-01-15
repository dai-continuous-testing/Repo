package in;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.experitest.repo.Repository;

public class Repo {
	private static Repository repo = new Repository(null);
	
	
	public static By obj(String key){
		return repo.obj(key);
	}
	public static void verifyPage(WebDriver driver, String pageName){
		repo.verifyPage(driver, pageName);
	}
	
	public static String zone(String key){
		return repo.zone(key);
	}
	public static String element(String key){
		return repo.element(key);
	}
	public static Properties properties(String key){
		return repo.prop(key);
	}
}
