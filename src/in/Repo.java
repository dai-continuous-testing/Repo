package in;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.experitest.repo.Repository;

public class Repo {
	private static Repository repo = new Repository("repo");
	
	
	public static By obj(String key){
		return repo.obj(key);
	}
	public static void verifyPage(WebDriver driver, String pageName){
		repo.verifyPage(driver, pageName);
	}
}
