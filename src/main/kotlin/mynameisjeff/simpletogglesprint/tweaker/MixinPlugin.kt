package mynameisjeff.simpletogglesprint.tweaker

import net.minecraftforge.fml.relauncher.CoreModManager
import org.spongepowered.asm.lib.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class MixinPlugin : IMixinConfigPlugin {
    private var hasPlayerAPI = false
    override fun onLoad(mixinPackage: String) {
        for ((key, value) in CoreModManager.getTransformers()) {
            if (key.startsWith("PlayerAPIPlugin") && value.contains("api.player.forge.PlayerAPITransformer")) {
                println("PlayerAPI detected.")
                hasPlayerAPI = true
                break
            }
        }
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        return if (mixinClassName.startsWith("mynameisjeff.simpletogglesprint.mixins.playerapi.")) {
            hasPlayerAPI
        } else true
    }

    override fun acceptTargets(myTargets: Set<String>, otherTargets: Set<String>) {}
    override fun getMixins(): List<String>? {
        return null
    }

    override fun preApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
    }

    override fun postApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
    }
}