package org.team401.mpgrapher

import javafx.scene.chart.XYChart

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