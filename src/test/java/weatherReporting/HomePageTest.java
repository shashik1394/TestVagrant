package weatherReporting;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;


import pageObjects.HomePage;
import pageObjects.WeatherPage;
import resources.Base;

public class HomePageTest extends Base {

	public WebDriver driver;
	HomePage homepage;
	private String HumidityValue;
	private String TempInCelsiusValue;
	double TempInCelsiusValueBySelenium;
	double HumidityValueBySelenium;
	double TempInKelvinValue;
	double TempInCelsiusByAPI;
	double HumidityValueByAPI;
	
	public static Logger log = LogManager.getLogger(HomePageTest.class.getName());

	@BeforeTest
	public void initialize() throws IOException {

		driver = initializeDriver();

	}

	@Test(priority=1)
	public void GetWeatherDataBySelenium() throws IOException, InterruptedException {
		
		driver.get(prop.getProperty("url"));
		driver.manage().window().maximize();
		homepage = new HomePage(driver);
		if(homepage.PopUps().size()>0) {
			homepage.PopUp().click();
			System.out.println("Click on No Thanks if pop up appears");
			log.info("Click on No Thanks if pop up appears");
		}
		homepage.SubMenu().click();
		log.info("Clicked on SubMenu");
		System.out.println("Clicked on SubMenu");
		if(homepage.Weather().isDisplayed())
			homepage.Weather().click();
		log.info("Clicked on Weather");
		System.out.println("Clicked on Weather");
		
		
		WeatherPage weatherpage = new WeatherPage(driver);
		int countOfCities = weatherpage.Cities().size();
		String searchedCity = prop.getProperty("SearchedCity");
		System.out.println("The city to be searched: " + searchedCity);
		log.info("The city to be searched: " + searchedCity);
		String[] array = {"Bengaluru","Bhopal","Chennai","Hyderabad","Kolkata","Lucknow","Mumbai","New Delhi","Patna","Srinagar","Visakhapatnam"};
		List<String> defaultCity = Arrays.asList(array);
		for(int i = 0; i<countOfCities; i++) {
			if(weatherpage.Cities().get(i).getText().trim().equalsIgnoreCase(searchedCity) && !defaultCity.contains(searchedCity)) {
				weatherpage.CitiesCheckBoxes().get(i).click();
				break;
			}
		}
		Thread.sleep(8000);
		ArrayList<String> citiesVisibleOnMap = new ArrayList<String>();
		int countOfCitiesOnMap = weatherpage.CitiesOnMap().size();
		for(int i = 0; i<countOfCitiesOnMap;i++) {
			citiesVisibleOnMap.add(weatherpage.CitiesOnMap().get(i).getText());
		}
		if(citiesVisibleOnMap.contains(searchedCity)) {
			System.out.println("City is available on Map");
			log.info("City is available on Map");
		}
		
		int countOfTempInCelOnMap = weatherpage.TempInCelOnMap().size();
		int countOfTempInFahOnMap = weatherpage.TempInFahOnMap().size();
		if(countOfCitiesOnMap == (countOfTempInCelOnMap + countOfTempInFahOnMap)/2) {
			System.out.println("Cities Temperature Info are visible on the Map");
			log.info("Cities Temperature Info are visible on the Map");
		}
		
		weatherpage.TapCityOnMap(prop.getProperty("SearchedCity")).click();
		Thread.sleep(2000);
		System.out.println("Select Any city on Map");
		log.info("Select Any city on Map");
		if(weatherpage.TempInfo().isDisplayed()) {
			System.out.println("Weather info Displayed on Map itself");
			log.info("Weather info Displayed on Map itself");
		}
		
		HumidityValue = weatherpage.Humidity().getText().split("Humidity: ")[1].split("%")[0];
		TempInCelsiusValue = weatherpage.TempInCelsius().getText().split("Temp in Degrees: ")[1];
		TempInCelsiusValueBySelenium = Double.parseDouble(TempInCelsiusValue);
		HumidityValueBySelenium = Double.parseDouble(HumidityValue);
		System.out.println("Humidity Value By Frontend: " + HumidityValueBySelenium);
		System.out.println("Temp Value in Celsius By Frontend: " + TempInCelsiusValueBySelenium);
		log.info("Humidity Value By Frontend: " + HumidityValueBySelenium);
		log.info("Temp Value in Celsius By Frontend: " + TempInCelsiusValueBySelenium);
	}
	
	@Test(priority=2)
	public void GetWeatherDataByAPI() {
		RestAssured.baseURI = "https://api.openweathermap.org";
		String cityName = prop.getProperty("SearchedCity");
		String response = given().log().all().queryParam("appid", "7fe67bf08c80ded756e598d6f8fedaea")
		.queryParam("q", cityName).header("Content-Type","application/json")
		.when().get("/data/2.5/weather")
		.then().assertThat().statusCode(200).extract().response().asString();
		JsonPath js = new JsonPath(response);
		TempInKelvinValue = js.getDouble("main.temp");
		TempInCelsiusByAPI = (TempInKelvinValue - 273.15);
		HumidityValueByAPI = js.getDouble("main.humidity");
		System.out.println("Humidity Value By API: " + HumidityValueByAPI);
		System.out.println("Temp Value in Celsius By API: " + TempInCelsiusByAPI);
		log.info("Humidity Value By API: " + HumidityValueByAPI);
		log.info("Temp Value in Celsius By API: " + TempInCelsiusByAPI);
	}
	
	@Test(priority=3)
	public void Comparison() {
		double TempDiff = Math.abs((TempInCelsiusValueBySelenium - TempInCelsiusByAPI));
		double HumidityDiff = Math.abs((HumidityValueBySelenium - HumidityValueByAPI));
		System.out.println("Difference in Temp Value: " + TempDiff);
		System.out.println("Difference in Humidity Value: " + HumidityDiff);
		log.info("Difference in Temp Value: " + TempDiff);
		log.info("Difference in Humidity Value: " + HumidityDiff);
		if(TempDiff <= 2 && HumidityDiff <= 10) {
			System.out.println("TestCase Passed as Difference lies within 2 degree temp and humidity difference less than 10");
			log.info("TestCase Passed as Difference lies within 2 degree temp and humidity difference less than 10");
		}
		else {
			System.out.println("TestCase Failed as Difference lies beyond 2 degree temp or humidity difference more than 10");
			log.info("TestCase Failed as Difference lies beyond 2 degree temp or humidity difference more than 10");
		}
		
		AssertJUnit.assertTrue(TempDiff <= 2.0 && HumidityDiff <= 10.0);	
	}

	@AfterTest
	public void teardown() {

		driver.close();

	}
}
