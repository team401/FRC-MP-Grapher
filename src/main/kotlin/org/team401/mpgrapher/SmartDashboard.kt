package org.team401.mpgrapher

import edu.wpi.first.networktables.NetworkTableInstance

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
object SmartDashboard {
    private val nt = NetworkTableInstance.getDefault()
    private val sd = nt.getTable("SmartDashboard")
    private fun entry(name: String) = sd.getEntry(name)

    fun init(ip: String) {
        nt.startClient(ip)
    }

    fun getString(key: String, default: String = "") = entry(key).getString(default)
    fun setString(key: String, value: String) = entry(key).setString(value)

    fun getDouble(key: String, default: Double = 0.0) = entry(key).getNumber(default).toDouble()
    fun setDouble(key: String, value: Double = 0.0) = entry(key).setDouble(value)
}