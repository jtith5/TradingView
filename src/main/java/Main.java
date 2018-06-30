import jdk.nashorn.internal.objects.Global;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    static WebDriver driver;
    static GlobalKeyListener globalKeyListener;

    public static void main(String[] args) {
        bringUpBrowser();
        maximizeBrowser();
        goToTradingView();
        openChartInNewTab("TQQQ");
        openChartInNewTab("BOTZ");
        openChartInNewTab("EDC");
        openChartInNewTab("DZK");
        closePopups();
        changeMovingAverage(50);
        createKeyListener();
    }

    private static void createKeyListener() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        // Get the logger for "org.jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        // Change the level for all handlers attached to the default logger.
        Handler[] handlers = Logger.getLogger("").getHandlers();
        for (Handler handler : handlers) {
            handler.setLevel(Level.OFF);
        }
        // Add the appropriate listeners.
        GlobalScreen.addNativeKeyListener(new GlobalKeyListener(driver));
    }

    private static void Wait() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void maximizeBrowser() {
        driver.manage().window().maximize();
    }

    private static void openChartInNewTab(String ticker) {
        searchTicker(ticker);
        clickInteractiveChart();
        toTab(0);
    }

    private static void bringUpBrowser() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Jevit\\IdeaProjects\\TradingView\\src\\main\\resources\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
    }

    private static void goToTradingView() {
        driver.get("http://www.tradingview.com");
    }

    private static void searchTicker(String ticker) {
        WebElement searchBox = driver.findElement(By.name("query"));
        searchBox.sendKeys(ticker);
        WebElement magnifyingGlass = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div[3]/form/label/span"));
        magnifyingGlass.click();
    }

    private static void clickInteractiveChart() {
        try {
            WebElement InteractiveChart = driver.findElement(By.xpath("//a[text()='Interactive Chart']"));
            InteractiveChart.click();
        } catch (StaleElementReferenceException e) {
            WebElement InteractiveChart = driver.findElement(By.xpath("//a[text()='Interactive Chart']"));
            InteractiveChart.click();
        }
    }

    private static void toTab(int tabNumber) {
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabNumber));
    }

    private static void closePopups() {
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        for (int i = tabs.size() - 1; i >= 1; i--) {
            driver.switchTo().window(tabs.get(i));
            Wait();
            try {
                WebElement NoThanks = driver.findElement(By.xpath("//div[text()='No thanks']"));
                NoThanks.click();
            } catch (org.openqa.selenium.NoSuchElementException e) {
                System.out.println("Pop up did not appear on " + driver.getTitle());
            }
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            WebElement Skip = driver.findElement(By.xpath("//div[text()='Skip']"));
            jse.executeScript("arguments[0].click();", Skip);
        }
    }

    private static void changeMovingAverage(Integer movingAverage) {
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        Wait();
        for (int i = tabs.size() - 1; i >= 1; i--) {
            driver.switchTo().window(tabs.get(i));
            WebElement Indicators = driver.findElement(By.xpath("//*[@id=\"header-toolbar-indicators\"]"));
            Indicators.click();

            Wait();
            Wait();

            WebElement BuiltIn = driver.findElement(By.xpath("//div[text()='Built-ins']"));
            BuiltIn.click();

            WebElement MovingAverage = driver.findElement(By.xpath("/html/body/div[9]/div[2]/div[2]/div/div[4]/div/div[61]/div[2]"));
            MovingAverage.click();

            WebElement CloseIndicators = driver.findElement(By.xpath("/html/body/div[9]/div[3]"));
            CloseIndicators.click();

            WebElement showObjectsTree = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div/div[1]/div[1]/div/div/div[5]/div[1]/div"));
            showObjectsTree.click();

            Wait();
            Wait();

            WebElement formatButton = driver.findElement(By.xpath("(//span[contains(@title, 'Format')])[3]"));
            formatButton.click();

            Wait();

            WebElement Input = driver.findElement(By.xpath("/html/body/div[10]/div[4]/div[2]/table/tbody/tr[1]/td[2]/input"));
            Input.sendKeys(Keys.CONTROL + "a");
            Input.sendKeys(Keys.DELETE);
            Input.sendKeys(movingAverage.toString());
            Input.sendKeys(Keys.ENTER);

            try {
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_ESCAPE);
                robot.keyRelease(KeyEvent.VK_ESCAPE);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            Wait();
        }
    }
}
