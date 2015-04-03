package controllers

import play.api.mvc.Action
import play.api.mvc.Controller

object GameState extends Controller {

  var number = 0

  def get = Action {
    Ok(number.toString)
  }

  def action = Action {
    number += 1
    Ok(number.toString)
  }

}