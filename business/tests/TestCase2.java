import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TestCase2 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://ec2-54-237-8-98.compute-1.amazonaws.com:8080";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testTes2() throws Exception {
    driver.get(baseUrl + "/business/vaadeLogin.htm");
    driver.findElement(By.id("kasutajaNimi")).clear();
    driver.findElement(By.id("kasutajaNimi")).sendKeys("siimo");
    driver.findElement(By.id("parool")).clear();
    driver.findElement(By.id("parool")).sendKeys("osiim");
    driver.findElement(By.cssSelector("input.loginupp.projektDetailNupp")).click();
    driver.findElement(By.linkText("Töötajad")).click();
    driver.findElement(By.id("kasutajaNimi")).clear();
    driver.findElement(By.id("kasutajaNimi")).sendKeys("testija1");
    driver.findElement(By.cssSelector("input.projektDetailNupp")).click();
    driver.findElement(By.id("kasutajaNimi")).click();
    driver.findElement(By.id("kasutajaNimi")).clear();
    driver.findElement(By.id("kasutajaNimi")).sendKeys("testija2");
    driver.findElement(By.cssSelector("input.projektDetailNupp")).click();
    new Select(driver.findElement(By.id("kvartaliteValikud"))).selectByVisibleText("III - kolmas kvartal");
    driver.findElement(By.xpath("(//input[@value='X'])[3]")).click();
    driver.findElement(By.xpath("(//input[@value='Jah'])[3]")).click();
    driver.findElement(By.xpath("(//input[@value='X'])[3]")).click();
    driver.findElement(By.xpath("(//input[@value='Jah'])[3]")).click();
    driver.findElement(By.linkText("Arhiiv")).click();
    driver.findElement(By.linkText("Pipeline")).click();
    driver.findElement(By.cssSelector("button.projektDetailNupp")).click();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
