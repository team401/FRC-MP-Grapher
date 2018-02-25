package org.team401.mpgrapher

/*
 * mpgrapher - Created on 2/25/18
 * Author: Cameron Earle
 * 
 * This code is licensed under the GNU GPL v3
 * You can find more info in the LICENSE file at project root
 */

/**
 * @author Cameron Earle
 * @version 2/25/18
 */
data class PublishData(val left: Side,
                       val right: Side,
                       val head: Double,
                       val desHead: Double,
                       val time: Long) {
    data class Side(val pos: Int,
                    val vel: Int,
                    val desPos: Double,
                    val desVel: Double)
}