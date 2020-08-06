package pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import resources.Base;

public class WeatherPage extends Base{
	
	public WebDriver driver;

	By cities = By.xpath("//div[@class='message']");
	By citiesCheckBoxes = By.xpath("//input[@type='checkbox']");
	By tempInCelOnMap = By.xpath("//span[@class='tempRedText']");
	By tempInFahOnMap = By.xpath("//span[@class='tempWhiteText']");
	By Bengaluru = By.xpath("//div[@title='Bengaluru']");
	By tempInfo = By.xpath("//div[@class='leaflet-popup-content-wrapper']");
	By humidity = By.xpath("//div[@class='leaflet-popup-content-wrapper']/div/div/span[3]");
	By tempInCelsius = By.xpath("//div[@class='leaflet-popup-content-wrapper']/div/div/span[4]");
	
	public WeatherPage(WebDriver driver) {
		this.driver = driver;

	}
	
	public List<WebElement> Cities() {
		return driver.findElements(cities);

	}
	
	public List<WebElement> CitiesCheckBoxes() {
		return driver.findElements(citiesCheckBoxes);

	}
	
	public List<WebElement> CitiesOnMap() {
		return driver.findElements(By.xpath("//div[@class='cityText']"));
	}
	
	public List<WebElement> TempInCelOnMap() {
		return driver.findElements(tempInCelOnMap);

	}
	
	public List<WebElement> TempInFahOnMap() {
		return driver.findElements(tempInFahOnMap);

	}
	
	public WebElement Bengaluru() {
		return driver.findElement(Bengaluru);
	}
	
	public WebElement TapCityOnMap(String city) {
		return driver.findElement(By.xpath("//div[@title='"+city+"']"));
	}
	
	public WebElement TempInfo() {
		return driver.findElement(tempInfo);
	}
	
	public WebElement Humidity() {
		return driver.findElement(humidity);
	}
	
	public WebElement TempInCelsius() {
		return driver.findElement(tempInCelsius);
	}
}
