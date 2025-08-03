package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class QualityAssurancePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    private static final int AFTER_SCROLL_PAUSE = 600;  // ms

    private final By seeAllQaJobsBtn = By.xpath(
            "//a[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
                    "'see all qa jobs')]");

    /* Açılan sayfada mutlaka bulunan göstergeler */
    private final By filterLocation = By.cssSelector("select[id*='location']");
    private final By firstJobCard   = By.cssSelector("div.position-list-item");

    public QualityAssurancePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js     = (JavascriptExecutor) driver;
    }

    public void clickSeeAllQaJobs() {

        WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(seeAllQaJobsBtn));
        js.executeScript("arguments[0].scrollIntoView({behavior:'smooth',block:'center'});", btn);
        pause(AFTER_SCROLL_PAUSE);

        String thisWin = driver.getWindowHandle();
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (ElementClickInterceptedException | TimeoutException e) {
            js.executeScript("arguments[0].click();", btn);
        }

        //Yeni sekme açıldıysa geç, yoksa aynı sayfada filtrenin gelmesini bekleme
        wait.until(d -> d.getWindowHandles().size() > 1 ||
                !d.findElements(filterLocation).isEmpty() ||
                !d.findElements(firstJobCard).isEmpty());

        if (driver.getWindowHandles().size() > 1) {
            for (String w : driver.getWindowHandles())
                if (!w.equals(thisWin)) { driver.switchTo().window(w); break; }
        }

        //Sayfa tamamen yüklenmesi
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> ((JavascriptExecutor) d)
                        .executeScript("return document.readyState").equals("complete"));

        pause(AFTER_SCROLL_PAUSE);
    }

    private void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
