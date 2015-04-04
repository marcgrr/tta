import com.google.inject.Guice
import play.api.GlobalSettings

object Global extends GlobalSettings {

  val injector = Guice.createInjector()

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    injector.getInstance(controllerClass)
  }

}