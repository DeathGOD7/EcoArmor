package com.willfp.ecoarmor.sets

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.common.collect.ImmutableList
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.ecoarmor.EcoArmorPlugin
import com.willfp.libreforge.chains.EffectChains

object ArmorSets {
    /**
     * Registered armor sets.
     */
    private val BY_ID: BiMap<String, ArmorSet> = HashBiMap.create()

    /**
     * Get all registered [ArmorSet]s.
     *
     * @return A list of all [ArmorSet]s.
     */
    @JvmStatic
    fun values(): List<ArmorSet> {
        return ImmutableList.copyOf(BY_ID.values)
    }

    /**
     * Get [ArmorSet] matching ID.
     *
     * @param name The name to search for.
     * @return The matching [ArmorSet], or null if not found.
     */
    @JvmStatic
    fun getByID(name: String): ArmorSet? {
        return BY_ID[name]
    }

    /**
     * Update all [ArmorSet]s.
     *
     * @param plugin Instance of EcoArmor.
     */
    @ConfigUpdater
    @JvmStatic
    fun update(plugin: EcoArmorPlugin) {
        plugin.ecoArmorYml.getSubsections("chains").mapNotNull {
            EffectChains.compile(it, "Effect Chains")
        }
        for (set in values()) {
            removeSet(set)
        }
        for (setConfig in plugin.ecoArmorYml.getSubsections("sets")) {
            ArmorSet(setConfig!!, plugin)
        }
    }

    /**
     * Add new [ArmorSet] to EcoArmor.
     *
     * @param set The [ArmorSet] to add.
     */
    @JvmStatic
    fun addNewSet(set: ArmorSet) {
        BY_ID.remove(set.id)
        BY_ID[set.id] = set
    }

    /**
     * Remove [ArmorSet] from EcoArmor.
     *
     * @param set The [ArmorSet] to remove.
     */
    @JvmStatic
    fun removeSet(set: ArmorSet) {
        BY_ID.remove(set.id)
    }
}