package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;

import java.time.Duration;
import java.util.List;

public class InsiderTest {

    private WebDriver driver;
    private HomePage home;
    private CareersPage careers;
    private QualityAssurancePage qa;
    private OpenPositionsPage jobs;

    private static final int DEMO_WAIT = 2;   // saniye

    @BeforeClass
    public void setup() {
        driver   = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://useinsider.com/");

        home    = new HomePage(driver);
        careers = new CareersPage(driver);
        qa      = new QualityAssurancePage(driver);
        jobs    = new OpenPositionsPage(driver);
    }

    @Test
    public void fullCareerFlowDemo() {

        home.goToCareersPage();               pause();

        //Bloklar
        Assert.assertTrue(careers.areAllBlocksVisible(),
                "Locations / Teams / Life blokları görünmüyor!!!!");
        pause();

        //Life at Insider
        careers.slowScrollDownToLifeAtInsider();   pause();

        //See all Teams
        careers.slowScrollUpToSeeAllTeamsAndClick();   pause();
        careers.slowScrollDownToQualityAssuranceAndClick();   pause();

        // See all QA jobs
        qa.clickSeeAllQaJobs();
        pause();

        //Filter by Location & Department
        jobs.setLocation();
        pause();
        jobs.setDepartment();
        pause();

        // Liste ve içerik kontrolü
        jobs.scrollToFirstJobCard();                 pause();
        Assert.assertTrue(jobs.isAnyJobPresent(), "hiç kart görünmedi!!!");

        for (OpenPositionsPage.JobInfo job : jobs.getAllJobInfos()) {
            Assert.assertTrue(job.position().contains("Quality Assurance"),
                    "Pozisyon QA içermiyor: " + job.position());
            Assert.assertEquals(job.department().trim(), "Quality Assurance",
                    "Departman yanlış: " + job.department());
            Assert.assertEquals(job.location().trim(), "Istanbul, Turkiye",
                    "Lokasyon yanlış: " + job.location());
        }
        pause();

        // View Role
        jobs.clickViewRoleOfFirstCardAndSwitch();  pause();
        Assert.assertTrue(driver.getCurrentUrl().contains("lever.co"),
                "Lever formuna yönlenmedi!");
    }

    @AfterClass
    public void tearDown() { if (driver != null) driver.quit(); }

    private void pause() {
        try { Thread.sleep(DEMO_WAIT * 1000L); } catch (InterruptedException ignored) {}
    }
}
