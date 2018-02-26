package org.team401.mpgrapher

import com.google.gson.Gson
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.control.TextField
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

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

class MpGrapherController {
    @FXML lateinit var leftP: TextField
    @FXML lateinit var leftD: TextField
    @FXML lateinit var leftV: TextField
    @FXML lateinit var leftA: TextField
    @FXML lateinit var rightP: TextField
    @FXML lateinit var rightD: TextField
    @FXML lateinit var rightV: TextField
    @FXML lateinit var rightA: TextField
    @FXML lateinit var H: TextField
    @FXML lateinit var runnerName: TextField
    @FXML lateinit var leftPointFile: TextField
    @FXML lateinit var rightPointFile: TextField
    @FXML lateinit var leftPosChart: LineChart<Double, Double>
    @FXML lateinit var leftVelChart: LineChart<Double, Double>
    @FXML lateinit var leftHdgChart: LineChart<Double, Double>
    @FXML lateinit var rightPosChart: LineChart<Double, Double>
    @FXML lateinit var rightVelChart: LineChart<Double, Double>
    @FXML lateinit var rightHdgChart: LineChart<Double, Double>

    private fun key(key: String) = "tuningRunner-${runnerName.text}-$key"

    private val executor = Executors.newSingleThreadScheduledExecutor {
        val t = Executors.defaultThreadFactory().newThread(it)
        t.isDaemon = true
        t //return t
    }
    private var future: ScheduledFuture<*>? = null
    private val gson = Gson()

    fun initialize() {
        future = executor.scheduleAtFixedRate(::process, 0L, 5L, TimeUnit.MILLISECONDS)

        leftP.textProperty().addListener {
            _, _, newValue ->
            rightP.text = newValue
        }
        leftD.textProperty().addListener {
            _, _, newValue ->
            rightD.text = newValue
        }
        leftV.textProperty().addListener {
            _, _, newValue ->
            rightV.text = newValue
        }
        leftA.textProperty().addListener {
            _, _, newValue ->
            rightA.text = newValue
        }
    }

    private var lastState = "not connected"
    private var currentState = "not connected"
    private var currentData = ""
    private var data = arrayListOf<String>()

    private fun process() {
        currentState = SmartDashboard.getString(key("state"), "not connected")
        currentData = SmartDashboard.getString(key("current"), "{}")
        if (currentState == "ready" && lastState != "ready") { //If transitioning to ready
            renderNewData() //Render our new data
        }
        if (currentState == "running") { //If we are running
            if (lastState != "running") { //If we transitioned to running
                data.clear() //Clear old data
            }
            if (currentData != "{}" && currentData != data.lastOrNull()) { //If this is new data
                data.add(currentData) //Add it
            }
        }
        lastState = currentState
    }

    private fun renderNewData() {
        val processedData = arrayListOf<PublishData>()
        data.forEach {
            try {
                processedData.add(gson.fromJson(it, PublishData::class.java))
            } catch (e: Exception) {}
        }

        val leftPosDesiredSeries = XYChart.Series<Double, Double>()
        val leftPosActualSeries = XYChart.Series<Double, Double>()
        val leftVelDesiredSeries = XYChart.Series<Double, Double>()
        val leftVelActualSeries = XYChart.Series<Double, Double>()
        val leftHdgDesiredSeries = XYChart.Series<Double, Double>()
        val leftHdgActualSeries = XYChart.Series<Double, Double>()
        val rightPosDesiredSeries = XYChart.Series<Double, Double>()
        val rightPosActualSeries = XYChart.Series<Double, Double>()
        val rightVelDesiredSeries = XYChart.Series<Double, Double>()
        val rightVelActualSeries = XYChart.Series<Double, Double>()
        val rightHdgDesiredSeries = XYChart.Series<Double, Double>()
        val rightHdgActualSeries = XYChart.Series<Double, Double>()
        leftPosDesiredSeries.name = "Desired"
        leftPosActualSeries.name = "Actual"
        leftVelDesiredSeries.name = "Desired"
        leftVelActualSeries.name = "Actual"
        leftHdgDesiredSeries.name = "Desired"
        leftHdgActualSeries.name = "Actual"
        rightPosDesiredSeries.name = "Desired"
        rightPosActualSeries.name = "Actual"
        rightVelDesiredSeries.name = "Desired"
        rightVelActualSeries.name = "Actual"
        rightHdgDesiredSeries.name = "Desired"
        rightHdgActualSeries.name = "Actual"

        processedData.forEach {
            val time = it.time.toDouble()

            leftHdgDesiredSeries.putData(time, it.desHead)
            leftHdgActualSeries.putData(time, it.head)
            rightHdgDesiredSeries.putData(time, it.desHead)
            rightHdgActualSeries.putData(time, it.head)

            leftPosDesiredSeries.putData(time, it.left.desPos)
            leftPosActualSeries.putData(time, it.left.pos.toDouble())
            rightPosDesiredSeries.putData(time, it.right.desPos)
            rightPosActualSeries.putData(time, it.right.pos.toDouble())

            leftVelDesiredSeries.putData(time, it.left.desVel)
            leftVelActualSeries.putData(time, it.left.vel.toDouble())
            rightVelDesiredSeries.putData(time, it.right.desVel)
            rightVelActualSeries.putData(time, it.right.vel.toDouble())
        }

        Platform.runLater {
            leftPosChart.data.clear()
            leftVelChart.data.clear()
            leftHdgChart.data.clear()
            rightPosChart.data.clear()
            rightVelChart.data.clear()
            rightHdgChart.data.clear()
            leftPosChart.data.add(leftPosDesiredSeries)
            leftPosChart.data.add(leftPosActualSeries)
            leftVelChart.data.add(leftVelDesiredSeries)
            leftVelChart.data.add(leftVelActualSeries)
            leftHdgChart.data.add(leftHdgDesiredSeries)
            leftHdgChart.data.add(leftHdgActualSeries)
            rightPosChart.data.add(rightPosDesiredSeries)
            rightPosChart.data.add(rightPosActualSeries)
            rightVelChart.data.add(rightVelDesiredSeries)
            rightVelChart.data.add(rightVelActualSeries)
            rightHdgChart.data.add(rightHdgDesiredSeries)
            rightHdgChart.data.add(rightHdgActualSeries)
        }
    }

    @FXML private fun onSubmitGains(e: ActionEvent) {
        SmartDashboard.setDouble(key("leftP"), leftP.text.toDoubleOrNull() ?: 0.0)
        SmartDashboard.setDouble(key("leftD"), leftD.text.toDoubleOrNull() ?: 0.0)
        SmartDashboard.setDouble(key("leftV"), leftV.text.toDoubleOrNull() ?: 0.0)
        SmartDashboard.setDouble(key("leftA"), leftA.text.toDoubleOrNull() ?: 0.0)
        SmartDashboard.setDouble(key("rightP"), rightP.text.toDoubleOrNull() ?: 0.0)
        SmartDashboard.setDouble(key("rightD"), rightD.text.toDoubleOrNull() ?: 0.0)
        SmartDashboard.setDouble(key("rightV"), rightV.text.toDoubleOrNull() ?: 0.0)
        SmartDashboard.setDouble(key("rightA"), rightA.text.toDoubleOrNull() ?: 0.0)
        SmartDashboard.setDouble(key("H"), H.text.toDoubleOrNull() ?: 0.0)
    }

    @FXML private fun onSubmitSettings(e: ActionEvent) {
        SmartDashboard.setString(key("lPoints"), leftPointFile.text)
        SmartDashboard.setString(key("rPoints"), rightPointFile.text)
    }
}