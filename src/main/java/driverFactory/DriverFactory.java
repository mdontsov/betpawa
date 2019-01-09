package driverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class DriverFactory {

    private static WebDriver driver;

    public static WebDriver driver() {
        return driver;
    }

    private final static String DRIVER = "webdriver.chrome.driver";

    protected DriverFactory() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            System.setProperty(DRIVER, "src/main/resources/chromedriverWin.exe");
        } else if (osName.contains("nix") || osName.contains("nux")) {
            System.setProperty(DRIVER, "src/main/resources/chromedriverLinux");
        } else if (osName.contains("mac")) {
            System.setProperty(DRIVER, "src/main/resources/chromedriverMac");
        } else {
            throw new IllegalStateException("Unsupported OS");
        }

        driver = new ChromeDriver(chromeOptions());
        driver.manage().window().maximize();
    }

    private ChromeOptions chromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--ignore-certificate-errors");
        return chromeOptions;
    }
}
