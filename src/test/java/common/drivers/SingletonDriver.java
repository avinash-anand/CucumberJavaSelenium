package common.drivers;

import com.google.gson.JsonObject;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Objects;

public class SingletonDriver {

    private SingletonDriver() {
    }

    private static WebDriver driver;

    private static WebDriver getWebDriver(BrowserType type, boolean proxyEnabled) {
        if (driver == null) {
            Proxy proxy = null;
            if (proxyEnabled) {
                System.out.println("PROXY ENABLED!!");
                proxy = getProxySettings();
            }
            switch (type) {
                case FIREFOX: {
                    System.setProperty("webdriver.gecko.driver", "gecko/geckodriver_x64.exe");
                    FirefoxOptions options = new FirefoxOptions();
                    if (Objects.nonNull(proxy)) {
                        //Below line is for setting proxy
                        options.setProxy(proxy);
                    }
                    options.setAcceptInsecureCerts(true);
//                    FirefoxProfile profile = new FirefoxProfile();
//                    profile.setAcceptUntrustedCertificates(true);
//                    profile.setPreference("network.proxy.type", 1);//Manual
//                    profile.setPreference("network.proxy.http", "localhost");
//                    profile.setPreference("network.proxy.http_port", 11111);
//                    options.setProfile(profile);
                    driver = new FirefoxDriver(options);
                    break;
                }
                case CHROME: {
                    System.setProperty("webdriver.chrome.driver", "chrome/chromedriver.exe");
                    ChromeOptions co = new ChromeOptions();
                    //Proxy doesn't work for chrome, as it reads from IE settings
                    driver = new ChromeDriver(co);
                    break;
                }
            }
        }
        return driver;
    }

    public static WebDriver getDriver() {
        String browserName = System.getProperty("browser", "firefox");
        //assuming name is coming as correct
        BrowserType type = BrowserType.fromName(browserName);
        boolean proxyEnabled = Boolean.valueOf(System.getProperty("proxy.enabled", "FALSE"));
        return getWebDriver(type, proxyEnabled);
    }

    private static Proxy getProxySettings() {
        String proxyPath = System.getProperty("proxy.path", "localhost:11111");
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(proxyPath);
        proxy.setFtpProxy(proxyPath);
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        proxy.setSslProxy(proxyPath);
        System.out.println("proxy - json = " + proxy.toJson());
        return proxy;
    }

    //you can do it through json as well
    private static JsonObject getJsonObjectForProxy() {
        JsonObject object = new JsonObject();
        object.addProperty("proxyType", "manual");
        object.addProperty("httpProxy", "localhost:11111");
        object.addProperty("ftpProxy", "localhost:11111");
        object.addProperty("sslProxy", "localhost:11111");
        return object;
    }

}
