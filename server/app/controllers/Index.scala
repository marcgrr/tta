package controllers

import play.api.mvc.Action
import play.api.mvc.Controller

object Index extends Controller {

  def index = Action {
    Ok("This is the root of the TTA server.")
  }

}