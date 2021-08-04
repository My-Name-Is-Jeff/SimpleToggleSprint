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

package mynameisjeff.simpletogglesprint.core;

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

            val currentVersion = DefaultArtifactVersion(currentTag.substringBefore("-"))
            val latestVersion = DefaultArtifactVersion(latestTag.substringAfter("v").substringBefore("-"))

            var updateUrl: String? = null
            if (latestTag.contains("pre") || (currentTag.contains("pre") && currentVersion >= latestVersion)) {
                var currentPre = 0.0
                var latestPre = 0.0
                if (currentTag.contains("pre")) {
                    currentPre = currentTag.substringAfter("pre").toDouble()
                }
                if (latestTag.contains("pre")) {
                    latestPre = latestTag.substringAfter("pre").toDouble()
                }
                if ((latestPre > currentPre) || (latestPre == 0.0 && currentVersion.compareTo(latestVersion) == 0)) {
                    updateUrl = latestRelease.optJSONArray("assets")[0].asJsonObject["browser_download_url"].asString
                }
            } else if (currentVersion < latestVersion) {
                updateUrl = latestRelease.optJSONArray("assets")[0].asJsonObject["browser_download_url"].asString
            }
            if (updateUrl != null) {
                EssentialAPI.getNotifications().push("Mod Update", "SimpleToggleSprint $latestTag is available!\nClick to open!", 5f) {
                    UDesktop.browse(URI.create("https://github.com/My-Name-Is-Jeff/SimpleToggleSprint/releases/latest"))
                }
            }
        }
    }
}