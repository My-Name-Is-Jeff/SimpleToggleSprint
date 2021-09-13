/*
 *   SimpleToggleSprint
 *   Copyright (C) 2021  My-Name-Is-Jeff
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mynameisjeff.simpletogglesprint.core

import gg.essential.api.EssentialAPI
import gg.essential.api.utils.WebUtil
import gg.essential.universal.UDesktop
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mynameisjeff.simpletogglesprint.SimpleToggleSprint
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion
import java.net.URI

object UpdateChecker {

    fun checkUpdate() {
        CoroutineScope(Dispatchers.IO + CoroutineName("SimpleToggleSprint-UpdateChecker")).launch {
            val latestRelease = WebUtil.fetchJSON("https://api.github.com/repos/My-Name-Is-Jeff/SimpleToggleSprint/releases/latest")
            val latestTag = latestRelease.optString("tag_name")
            val currentTag = SimpleToggleSprint.VERSION

            val currentVersion = UpdateVersion(currentTag)
            val latestVersion = UpdateVersion(latestTag.substringAfter("v"))
            if (currentVersion < latestVersion) {
                EssentialAPI.getNotifications().push("Mod Update", "SimpleToggleSprint $latestTag is available!\nClick to open!", 5f) {
                    UDesktop.browse(URI.create("https://github.com/My-Name-Is-Jeff/SimpleToggleSprint/releases/latest"))
                }
            }
        }
    }
}

class UpdateVersion(val versionString: String) : Comparable<UpdateVersion> {

    companion object {
        val regex = Regex("^(?<version>[\\d.]+)-?(?<type>\\D+)?(?<typever>\\d+\\.?\\d*)?\$")
    }

    private val matched by lazy {
        regex.find(versionString)
    }
    val isSafe = matched != null

    val version = matched!!.groups["version"]!!.value
    val versionArtifact = DefaultArtifactVersion(matched!!.groups["version"]!!.value)
    val specialVersionType by lazy {
        val typeString = matched!!.groups["type"]?.value ?: return@lazy UpdateType.RELEASE

        return@lazy UpdateType.values().find { typeString == it.prefix } ?: UpdateType.UNKNOWN
    }
    val specialVersion by lazy {
        if (specialVersionType == UpdateType.RELEASE) return@lazy null
        return@lazy matched!!.groups["typever"]?.value?.toDoubleOrNull()
    }

    override fun compareTo(other: UpdateVersion): Int {
        if (!isSafe || !other.isSafe) return -1
        return if (versionArtifact.compareTo(other.versionArtifact) == 0) {
            if (specialVersionType.ordinal == other.specialVersionType.ordinal) {
                (specialVersion ?: 0.0).compareTo(other.specialVersion ?: 0.0)
            } else other.specialVersionType.ordinal - specialVersionType.ordinal
        } else versionArtifact.compareTo(other.versionArtifact)
    }
}

enum class UpdateType(val prefix: String) {
    UNKNOWN("unknown"),
    RELEASE(""),
    RELEASECANDIDATE("RC"),
    PRERELEASE("pre"),
}