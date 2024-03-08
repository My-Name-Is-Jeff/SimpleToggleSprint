package mynameisjeff.simpletogglesprint.tweaker

import net.minecraftforge.common.ForgeVersion
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
import kotlin.reflect.full.staticProperties

class FMLLoadingPlugin : IFMLLoadingPlugin {
    companion object {
        val mcVersion by lazy {
            (ForgeVersion::class.staticProperties.find {
                it.name == "mcVersion"
            }!!.get() as String)
        }
    }

    override fun getASMTransformerClass(): Array<String>? = if (mcVersion.startsWith("1.8")) null else arrayOf(PlatformCreator::class.qualifiedName!!)

    override fun getModContainerClass(): String? = null

    override fun getSetupClass(): String? = null

    override fun injectData(map: MutableMap<String, Any>) {
    }

    override fun getAccessTransformerClass(): String? = null
}