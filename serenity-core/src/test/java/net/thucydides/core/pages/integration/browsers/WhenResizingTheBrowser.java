package net.thucydides.core.pages.integration.browsers;


import net.thucydides.core.pages.integration.StaticSitePage;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.MockEnvironmentVariables;
import net.thucydides.core.webdriver.SupportedWebDriver;
import net.thucydides.core.webdriver.WebDriverFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Parameterized.class)
@Ignore("Resizing doesn't work well in Geckodriver")
public class WhenResizingTheBrowser {

    private final SupportedWebDriver driverType;

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                {SupportedWebDriver.FIREFOX },      // TODO: Resizing doesn't work in Geckodriver yet
                {SupportedWebDriver.CHROME },
                {SupportedWebDriver.PHANTOMJS }
        });
    }

    private StaticSitePage page;
    private EnvironmentVariables environmentVariables;
    private WebDriver driver;
    WebDriverFactory factory;

    public WhenResizingTheBrowser(SupportedWebDriver driverType) {
        this.driverType = driverType;
    }

    @Before
    public void setupFactory() {
        environmentVariables = new MockEnvironmentVariables();
        factory = new WebDriverFactory(environmentVariables);
    }

    @After
    public void shutdownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void should_resize_browser_automatically() {
        environmentVariables.setProperty("thucydides.browser.height", "300");
        environmentVariables.setProperty("thucydides.browser.width", "400");

        driver = factory.newInstanceOf(driverType);
        page = new StaticSitePage(driver, 1000);
        page.open();

        Dimension screenSize = driver.manage().window().getSize();
        assertThat(screenSize.width, is(400));
        assertThat(screenSize.height, is(300));
    }
}
