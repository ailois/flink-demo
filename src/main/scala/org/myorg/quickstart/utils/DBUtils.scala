package org.myorg.quickstart.utils

import scala.util.Random

object DBUtils {

  def getCollention()={
    new Random().nextInt(10) + ""

  }

  def returnCollection(connent:String) ={

  }

}
