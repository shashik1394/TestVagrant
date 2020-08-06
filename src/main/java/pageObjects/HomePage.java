package pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {

	public WebDriver driver;

	By subMenu = By.id("h_sub_menu");
	By weather = By.xpath("//a[text()='WEATHER']");
	By popup = By.xpath("//a[@class='notnow']");

	public HomePage(WebDriver driver) {
		this.driver = driver;

	}

	public WebElement SubMenu() {
		return driver.findElement(subMenu);
	}
	
	public WebElement Weather() {
		return driver.findElement(weather);
	}
	
	public WebElement PopUp() {
		return driver.findElement(popup);
	}
	
	public List<WebElement> PopUps() {
		return driver.findElements(popup);
	}

}
