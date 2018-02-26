package org.team401.mpgrapher

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/*
 * mpgrapher - Created on 2/24/18
 * Author: Cameron Earle
 * 
 * This code is licensed under the GNU GPL v3
 * You can find more info in the LICENSE file at project root
 */

/**
 * @author Cameron Earle
 * @version 2/24/18
 */

fun main(args: Array<String>) {
    Application.launch(MpGrapher::class.java, *args)
}

class MpGrapher: Application() {
    override fun start(stage: Stage) {
        SmartDashboard.init("10.4.1.2")

        val root: Parent = FXMLLoader.load(javaClass.classLoader.getResource("main_view.fxml"))
        val scene = Scene(root)
        stage.title = "401 RIORunner Tuning Utility"
        stage.scene = scene
        stage.show()
    }
}