package controllers

import javax.inject.Inject
import javax.inject.Singleton

import play.api.mvc.Action
import play.api.mvc.Controller

@Singleton
class GameState @Inject() () extends Controller {

  var number = 0

  def get = Action {
    Ok(number.toString)
  }

  def action = Action {
    number += 1
    Ok(number.toString)
  }

}