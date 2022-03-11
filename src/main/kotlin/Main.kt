import io.github.cdimascio.dotenv.dotenv
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import java.lang.Thread.sleep
import kotlin.random.Random

fun main() {

    //cargamos variables de entorno
    val dotenv = dotenv {}

    //cargamos el driver que usaremos, hay que generalizarlos para distintos OS y browsers
    System.setProperty("webdriver.gecko.driver", "drivers/win64/geckodriver.exe")
    val driver: WebDriver = FirefoxDriver()

    //vamos a ucursos
    driver.get("https://www.u-cursos.cl")

    //completamos el login
    driver.findElement(By.name("username")).sendKeys(dotenv["U-USERNAME"])
    driver.findElement(By.name("password")).sendKeys(dotenv["U-PASSWORD"])
    driver.findElement(By.cssSelector("[value='Ingresar']")).click()

    //buscamos todos los 'a' y luego el que tiene los cursos
    driver.findElements(By.cssSelector("a"))
        .first {
            it.getAttribute("href").contains("todos_cursos")
        }
        .click()

    //obtenemos los links de todos los cursos
    val todosLosCursos =
        driver.findElements(By.cssSelector("#todos_cursos > tbody > tr > td > h1"))
            .filter { !it.findElement(By.cssSelector("a")).getAttribute("href").contains("/uchile/") }
            .map { it.findElement(By.cssSelector("a")).getAttribute("href") }

    todosLosCursos.forEach { curso ->

        println(curso)
        driver.get(curso)

        //buscamos todos los 'a' y luego el que tiene los integrantes
        val integrantes =
            driver.findElements(By.cssSelector("a"))
                .first { it.getAttribute("href").contains("/integrantes/") }

        println(integrantes.getAttribute("href"))
        integrantes.click()

        //iteramos sobre todos los integrantes
        driver.findElements(By.cssSelector("#integrantes > tbody > tr > td > h1")).forEach {
            println(it.text)
        }

        sleep(Random.nextInt(1, 5).toLong() * 1000)

    }

    sleep(30000)

    driver.quit()
}