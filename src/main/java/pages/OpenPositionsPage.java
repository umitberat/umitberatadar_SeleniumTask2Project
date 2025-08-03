package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class OpenPositionsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    private final By locationSelect   = By.cssSelector("select[id*='location']");
    private final By departmentSelect = By.cssSelector("select[id*='department']");
    private final By jobCards         = By.cssSelector("div.position-list-item");

    // locator
    private final By viewRoleInsideCard =
            By.xpath(".//a[normalize-space()='View Role' and contains(@class,'btn')]");

    public OpenPositionsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(25));
        this.js     = (JavascriptExecutor) driver;
        closeCookieIfPresent();
    }


    public void setLocation()   { waitOptionsAndSelect(locationSelect, "Istanbul, Turkiye"); }
    public void setDepartment() { waitOptionsAndSelect(departmentSelect, "Quality Assurance"); }

    public void scrollToFirstJobCard() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(jobCards, 0));
        centerOn(driver.findElements(jobCards).get(0));
    }

    // İlk QA kartının View Role’üne tıklar
    public void clickViewRoleOfFirstCardAndSwitch() {

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(jobCards, 0));
        WebElement firstCard = driver.findElements(jobCards).get(0);
        centerOn(firstCard);

        WebElement viewBtn = firstCard.findElement(viewRoleInsideCard);

        String thisWin = driver.getWindowHandle();
        try {
            wait.until(ExpectedConditions.elementToBeClickable(viewBtn)).click();
        } catch (Exception e) {                  // JS fallback
            js.executeScript("arguments[0].click();", viewBtn);
        }

        //sekme mi? Pencere mi?
        wait.until(d -> d.getWindowHandles().size() > 1
                || !d.getCurrentUrl().contains("/careers/"));

        if (driver.getWindowHandles().size() > 1) {
            for (String w : driver.getWindowHandles())
                if (!w.equals(thisWin)) { driver.switchTo().window(w); break; }
        }

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> js.executeScript("return document.readyState").equals("complete"));
    }


    private void waitOptionsAndSelect(By select, String wanted) {
        wait.until(d -> {
            WebElement sel = d.findElement(select);
            return new Select(sel).getOptions().stream()
                    .anyMatch(o -> o.getText().trim().equalsIgnoreCase(wanted));
        });
        Select sel = new Select(driver.findElement(select));
        sel.selectByVisibleText(wanted);
        wait.until(d -> sel.getFirstSelectedOption().getText().trim()
                .equalsIgnoreCase(wanted));
    }

    private void centerOn(WebElement el) {
        js.executeScript(
                "const r=arguments[0].getBoundingClientRect();" +
                        "window.scrollBy(0, r.top - (window.innerHeight/2));", el);
    }

    private void closeCookieIfPresent() {
        try {
            WebElement btn = driver.findElement(By.cssSelector("#wt-cli-accept-all-btn"));
            if (btn.isDisplayed()) btn.click();
        } catch (NoSuchElementException ignored) {}
    }

    public boolean isAnyJobPresent() { return !driver.findElements(jobCards).isEmpty(); }

    public List<JobInfo> getAllJobInfos() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(jobCards, 0));
        return driver.findElements(jobCards).stream().map(c -> new JobInfo(
                c.findElement(By.cssSelector(".position-title")).getText(),
                c.findElement(By.cssSelector(".position-department")).getText(),
                c.findElement(By.cssSelector(".position-location")).getText()
        )).toList();
    }

    public record JobInfo(String position, String department, String location) {}
}
