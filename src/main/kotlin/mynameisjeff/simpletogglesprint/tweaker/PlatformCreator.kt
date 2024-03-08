package mynameisjeff.simpletogglesprint.tweaker

import net.minecraft.launchwrapper.IClassTransformer
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.MethodInsnNode

class PlatformCreator : IClassTransformer {
    private val srgReplacements: Map<String, String> = mapOf(
        "func_70115_ae" to "func_184218_aH"
    )

    override fun transform(name: String?, transformedName: String?, basicClass: ByteArray?): ByteArray? {
        if (name == null || transformedName == null || basicClass == null) return basicClass

        if (transformedName == "mynameisjeff.simpletogglesprint.platform.Platform") {
            val classReader = ClassReader(basicClass)
            val classNode = ClassNode()
            classReader.accept(classNode, ClassReader.EXPAND_FRAMES)
            if (classNode.version < Opcodes.V1_8) {
                classNode.version = Opcodes.V1_8
            }

            for (method in classNode.methods) {
                for (insn in method.instructions) {
                    if (insn is MethodInsnNode && insn.owner.startsWith("net/minecraft")) {
                        srgReplacements[insn.name]?.let {
                            insn.name = it
                        }
                    } else if (insn is FieldInsnNode && insn.owner.startsWith("net/minecraft")) {
                        srgReplacements[insn.name]?.let {
                            insn.name = it
                        }
                    }
                }
            }

            val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
            classNode.accept(classWriter)

            return classWriter.toByteArray()
        }

        return basicClass
    }
}