
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TestCase1 {
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
  public void test() throws Exception {
    driver.get(baseUrl + "/business/vaadeLogin.htm");
    assertEquals("The page title should equal at the start of the test.", "Bizint login", driver.getTitle());
    driver.findElement(By.id("kasutajaNimi")).clear();
    driver.findElement(By.id("kasutajaNimi")).sendKeys("siimo");
    driver.findElement(By.id("parool")).clear();
    driver.findElement(By.id("parool")).sendKeys("osiim");
    driver.findElement(By.cssSelector("input.loginupp.projektDetailNupp")).click();
    driver.findElement(By.cssSelector("#uusProjekt > #nimi")).clear();
    driver.findElement(By.cssSelector("#uusProjekt > #nimi")).sendKeys("test");
    driver.findElement(By.cssSelector("#uusProjekt > input.projektDetailNupp")).click();
    driver.findElement(By.xpath("//th[contains(text(), 'test')]")).click();
    new Select(driver.findElement(By.id("uueKasutajaList"))).selectByVisibleText("kalle");
    driver.findElement(By.cssSelector("div.leftSideDiv > input.projektDetailNupp")).click();
    new Select(driver.findElement(By.id("uueKasutajaList"))).selectByVisibleText("juhan");
    driver.findElement(By.cssSelector("div.leftSideDiv > input.projektDetailNupp")).click();
    driver.findElement(By.cssSelector("input.tootajaVastutajaCheckbox")).click();
    driver.findElement(By.cssSelector("td.aktiivneVeerg > input[type=\"checkbox\"]")).click();
    driver.findElement(By.xpath("(//input[@type='checkbox'])[4]")).click();
    driver.findElement(By.cssSelector("input.osalusVeerg")).clear();
    driver.findElement(By.cssSelector("input.osalusVeerg")).sendKeys("0.7");
    driver.findElement(By.xpath("(//input[@value='0.0'])[2]")).clear();
    driver.findElement(By.xpath("(//input[@value='0.0'])[2]")).sendKeys("0.3");
    driver.findElement(By.cssSelector("div.buttonAlign > button.projektDetailNupp")).click();
    driver.findElement(By.id("kirjelduseVali")).clear();
    driver.findElement(By.id("kirjelduseVali")).sendKeys("testimiseks");
    driver.findElement(By.cssSelector("input.projektDetailNupp.addbutton")).click();
    driver.findElement(By.name("sonum")).clear();
    driver.findElement(By.name("sonum")).sendKeys("testikommentaar");
    driver.findElement(By.xpath("//input[@value='kommenteeri']")).click();
    driver.findElement(By.id("logiAvamine")).click();
    driver.findElement(By.id("logiAvamine")).click();
    driver.findElement(By.linkText("Tulud ja kulud")).click();
    driver.findElement(By.id("summa")).clear();
    driver.findElement(By.id("summa")).sendKeys("1500.0");
    driver.findElement(By.id("tuluNimi")).clear();
    driver.findElement(By.id("tuluNimi")).sendKeys("monies");
    driver.findElement(By.cssSelector("#uusTulu > input.projektDetailNupp")).click();
    driver.findElement(By.cssSelector("#uusKulu > #summa")).clear();
    driver.findElement(By.cssSelector("#uusKulu > #summa")).sendKeys("500.0");
    driver.findElement(By.id("kuluNimi")).clear();
    driver.findElement(By.id("kuluNimi")).sendKeys("kulu");
    driver.findElement(By.cssSelector("input.kuluLisamiseNupp.projektDetailNupp")).click();
    driver.findElement(By.linkText("Üldine")).click();
    driver.findElement(By.cssSelector("div.closeView")).click();
    driver.findElement(By.xpath("//th[contains(text(), 'test')]")).click();
    driver.findElement(By.cssSelector("button.punaneProjektDetailNupp.kustutaProjektNupp")).click();
    driver.findElement(By.cssSelector("div.muudaKustuta.muudaKustutaTeine > button.punaneProjektDetailNupp")).click();
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
