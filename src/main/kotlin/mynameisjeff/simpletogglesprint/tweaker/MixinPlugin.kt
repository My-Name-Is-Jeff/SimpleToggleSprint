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

package mynameisjeff.simpletogglesprint.tweaker

import com.llamalad7.mixinextras.MixinExtrasBootstrap
import net.minecraftforge.fml.relauncher.CoreModManager
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class MixinPlugin : IMixinConfigPlugin {
    private var hasPlayerAPI = false
    override fun onLoad(mixinPackage: String) {
        MixinExtrasBootstrap.init()

        for ((key, value) in CoreModManager.getTransformers()) {
            if (key.startsWith("PlayerAPIPlugin") && value.contains("api.player.forge.PlayerAPITransformer")) {
                println("PlayerAPI detected.")
                hasPlayerAPI = true
                break
            }
        }
    }

    override fun getRefMapperConfig(): String? = null

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        return if (mixinClassName.startsWith("mynameisjeff.simpletogglesprint.mixins.playerapi.")) {
            hasPlayerAPI
        } else true
    }

    override fun acceptTargets(myTargets: Set<String>, otherTargets: Set<String>) {}
    override fun getMixins(): List<String>? = null

    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }
}