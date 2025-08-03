package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class CareersPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;
    private final JavascriptExecutor js;

    private final By locationsSec = By.xpath(
            "//h3[normalize-space(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'))" +
                    "  ='our locations']/ancestor::*[self::section or self::div][1]");

    private final By teamsSec = By.xpath(
            "//h3[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'find your calling')]" +
                    "/ancestor::*[self::section or self::div][1]");

    private final By lifeSec = By.xpath(
            "//h2[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'life at insider')]" +
                    "/ancestor::*[self::section or self::div][1]");

    //locator'ler
    private final By lifeHeading        = By.xpath("//h2[contains(.,'Life at Insider')]");
    private final By firstLifePhoto     = By.xpath("(//h2[contains(.,'Life at Insider')]/following::img)[1]");
    private final By seeAllTeamsButton  = By.xpath("//a[normalize-space()='See all teams']");
    private final By qaCard             = By.xpath("//h3[contains(.,'Quality Assurance')]/ancestor::a");

    public CareersPage(WebDriver driver) {
        this.driver  = driver;
        this.wait    = new WebDriverWait(driver, Duration.ofSeconds(12));
        this.actions = new Actions(driver);
        this.js      = (JavascriptExecutor) driver;
    }

    public boolean areAllBlocksVisible() {
        wait.until(ExpectedConditions.presenceOfElementLocated(locationsSec));
        wait.until(ExpectedConditions.presenceOfElementLocated(teamsSec));
        wait.until(ExpectedConditions.presenceOfElementLocated(lifeSec));
        return true;
    }


    //CareerPage asagi inme
    public void slowScrollDownToLifeAtInsider() {
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(lifeHeading));
        smoothScrollTo(header);

        WebElement photo  = wait.until(ExpectedConditions.visibilityOfElementLocated(firstLifePhoto));
        centerOn(photo);
        pause(1500);
    }

    // Sayfayı yukarı al, See all teams gorunce tıkla
    public void slowScrollUpToSeeAllTeamsAndClick() {
        WebElement seeAll = wait.until(ExpectedConditions.visibilityOfElementLocated(seeAllTeamsButton));
        smoothScrollTo(seeAll, Direction.UP);
        centerOn(seeAll);
        pause(600);
        js.executeScript("arguments[0].click();", seeAll);
    }

    //Quality Assurance tiklama
    public void slowScrollDownToQualityAssuranceAndClick() {
        WebElement qa = wait.until(ExpectedConditions.visibilityOfElementLocated(qaCard));
        smoothScrollTo(qa);
        centerOn(qa);
        pause(700);
        js.executeScript("arguments[0].click();", qa);
    }


    private enum Direction { DOWN, UP }

    private void smoothScrollTo(WebElement el, Direction dir) {
        int step = dir == Direction.UP ? -300 : 300;
        int tries = 0;
        while (!isInViewport(el) && tries++ < 40) {
            js.executeScript("window.scrollBy(0, arguments[0]);", step);
            pause(200);
        }
    }
    private void smoothScrollTo(WebElement el) { smoothScrollTo(el, Direction.DOWN); }

    // İlgili elementi ekranın ortasına getirme
    private void centerOn(WebElement el) {
        js.executeScript(
                "const r = arguments[0].getBoundingClientRect();" +
                        "window.scrollBy(0, r.top - (window.innerHeight/2));", el);
    }

    private boolean isInViewport(WebElement el) {
        return (Boolean) js.executeScript(
                "const r = arguments[0].getBoundingClientRect();" +
                        "return (r.top < window.innerHeight && r.bottom > 0);", el);
    }

    private void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
