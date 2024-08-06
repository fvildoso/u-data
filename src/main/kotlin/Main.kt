import io.github.cdimascio.dotenv.dotenv
import io.github.evanrupert.excelkt.workbook
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import java.lang.Thread.sleep
import kotlin.random.Random

data class Member(
    val course: String,
    val name: String,
)


fun main() {

    //cargamos variables de entorno
    val dotenv = dotenv {}

    //cargamos el driver que usaremos, hay que generalizarlos para distintos OS y browsers
    val firefoxPath = dotenv["U-FIREFOXPATH"]
    System.setProperty("webdriver.firefox.bin", firefoxPath)
    System.setProperty("webdriver.gecko.driver", dotenv["U-DRIVER"])
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
        driver.findElements(By.cssSelector("#todos_cursos > tbody > tr > td > div > h1"))
            .filter { !it.findElement(By.cssSelector("a")).getAttribute("href").contains("/uchile/") }
            .map { it.findElement(By.cssSelector("a")).getAttribute("href") }
            .filter { !it.contains("COM") }

    val mutableList = mutableListOf<Member>()

    todosLosCursos.forEachIndexed { index, curso ->

        println("$index=$curso")
        driver.get(curso)

        //buscamos todos los 'a' y luego el que tiene los integrantes
        val integrantes =
            driver.findElements(By.cssSelector("a"))
                .first { it.getAttribute("href").contains("/integrantes/") }

        println(integrantes.getAttribute("href"))
        integrantes.click()

        //iteramos sobre todos los integrantes
        driver.findElements(By.cssSelector("#integrantes > tbody > tr > td > div > h1")).forEach {
            val member = Member(curso, it.text)
            mutableList.add(member)
            println(it.text)
        }

        sleep(Random.nextInt(1, 5).toLong() * 1000)

    }

    workbook {
        sheet("members") {
            for (customer in mutableList)
                row {
                    cell(customer.course)
                    cell(customer.name)
                }
        }

    }.write("members.xlsx")

    sleep(30000)

    driver.quit()
}