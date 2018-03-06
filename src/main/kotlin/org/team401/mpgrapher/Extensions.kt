package org.team401.mpgrapher

import javafx.scene.chart.XYChart
import javafx.scene.control.TextField

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

fun <X, Y> XYChart.Series<X, Y>.putData(x: X, y: Y) = data.add(XYChart.Data(x, y))

fun TextField.parseFractional(): Double {
    return if (text.contains('/')) {
        val split = text.split('/')
        val num = split[0].toDoubleOrNull() ?: 0.0
        val denom = split[1].toDoubleOrNull() ?: 1.0
        num / denom
    } else {
        text.toDoubleOrNull() ?: 0.0
    }
}