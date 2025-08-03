package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    private By companyMenu   = By.xpath("//a[@id='navbarDropdownMenuLink' and contains(text(),'Company')]");
    private By careersOption = By.xpath("//a[contains(@class,'dropdown-sub') and contains(text(),'Careers')]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
    }

    public void goToCareersPage() {

        WebElement company = wait.until(ExpectedConditions.visibilityOfElementLocated(companyMenu));

        //0.8 s bekleme kullanıcı gözüksünmesi icin
        pause(800);

        actions.moveToElement(company).perform();

        //0.7 s bekleme dropdown animasyonu için
        pause(700);

        WebElement careers = wait.until(ExpectedConditions.elementToBeClickable(careersOption));
        careers.click();
    }

    private void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
