import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver

fun main(args: Array<String>) {
    println("Hello World!")

    System.setProperty("webdriver.gecko.driver","drivers/win64/geckodriver.exe");
    val driver: WebDriver = FirefoxDriver()

    driver.get("https://www.u-cursos.cl")

    driver.findElement(By.name("username")).sendKeys()

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    driver.quit()
}