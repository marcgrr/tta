package controllers

import javax.inject.Inject
import javax.inject.Singleton

import play.api.mvc.Action
import play.api.mvc.Controller

@Singleton
class Index @Inject() () extends Controller {

  def index = Action {
    Ok("This is the root of the TTA server.")
  }

}