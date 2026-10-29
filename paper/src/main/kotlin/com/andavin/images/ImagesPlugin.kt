package com.andavin.images

import com.andavin.images.util.JULoggingProvider
import org.bukkit.plugin.java.JavaPlugin

class ImagesPlugin : JavaPlugin() {

    override fun onLoad() {
        JULoggingProvider.logger = logger
    }

    override fun onEnable() {
        saveDefaultConfig()
    }

    override fun onDisable() {
    }
}
