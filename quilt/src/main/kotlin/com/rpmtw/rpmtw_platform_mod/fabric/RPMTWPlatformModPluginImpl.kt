package com.rpmtw.rpmtw_platform_mod.fabric

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener

import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.Commands
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import org.quiltmc.loader.api.QuiltLoader
import org.quiltmc.qsl.command.api.client.ClientCommandManager
import org.quiltmc.qsl.command.api.client.ClientCommandRegistrationCallback
import org.quiltmc.qsl.command.api.client.QuiltClientCommandSource
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfigScreen() {
        // no-op
    }

    @JvmStatic
    fun registerClientCommand(
        command: String,
        subCommand: String,
        argumentName: String? = null,
        argumentType: ArgumentType<*>? = null,
        executes: (CommandContext<*>) -> Int
    ) {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback {
                dispatcher: CommandDispatcher<QuiltClientCommandSource>,
                buildContext: CommandBuildContext?,
                environment: Commands.CommandSelection ->
            if (argumentName != null && argumentType != null) {
                dispatcher.register(
                    ClientCommandManager.literal(command).then(
                        ClientCommandManager.literal(subCommand)
                            .then(ClientCommandManager.argument(argumentName, argumentType).executes {
                                return@executes executes(it)
                            })
                    )
                )
            } else {
                dispatcher.register(ClientCommandManager.literal(command).then(ClientCommandManager.literal(subCommand).executes {
                    return@executes executes(it)
                }))
            }
        })
    }

    @JvmStatic
    fun <T> registerReloadEvent(reloadListener: SimplePreparableReloadListener<T>) {
        ResourceManagerHelperImpl.get(PackType.CLIENT_RESOURCES)
            .registerReloadListener(PrepareableReloadListenerWrapper(reloadListener))
    }

    @JvmStatic
    fun getResourcePacksFolder(): File {
        return QuiltLoader.getGameDir().resolve("resourcepacks").toFile()
    }
}

@Suppress("SpellCheckingInspection")
private class PrepareableReloadListenerWrapper<T>(val reloadListener: SimplePreparableReloadListener<T>) :
    IdentifiableResourceReloadListener {
    override fun reload(
        preparationBarrier: PreparableReloadListener.PreparationBarrier,
        resourceManager: ResourceManager,
        profilerFiller: ProfilerFiller,
        profilerFiller2: ProfilerFiller,
        executor: Executor,
        executor2: Executor
    ): CompletableFuture<Void> {
        return reloadListener.reload(
            preparationBarrier,
            resourceManager,
            profilerFiller,
            profilerFiller2,
            executor,
            executor2)
    }

    override fun getQuiltId(): ResourceLocation {
        return ResourceLocation("${RPMTWPlatformMod.MOD_ID}/reload_listener_${reloadListener.hashCode()}")
    }

    override fun getFabricId(): ResourceLocation {
        TODO("Not yet implemented")
    }
}