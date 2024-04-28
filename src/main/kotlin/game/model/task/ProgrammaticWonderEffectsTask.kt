package game.model.task

import Logging
import game.common.BuildingType
import game.common.modifiers.ActiveBuildingMod
import game.common.modifiers.BuildingMod
import game.common.modifiers.BuildingModTarget
import game.common.modifiers.BuildingModType
import game.model.Model
import game.model.game.Building
import logger
import kotlin.math.abs

class ProgrammaticWonderEffectsTask(
    private val model: Model,
    private val waterNextToWheat: Double,
    private val ironNextToForge: Double,
) : Logging {
    private val log = logger()

    fun process() {
        model.buildings.values.forEach { b ->
            applyHatshepsutTemple(b)
            applyPyramidOfGiza(b)
            applyTempleOfHeaven(b)
            applyTerracottaArmy(b)
            applyRijksmuseum(b)
            applySummerPalace(b)
            applyGoldenGateBridge(b)
            applyUnitedNations(b)
            applyMountTai(b)
            applyCNTower(b)
        }
    }

    private fun applyHatshepsutTemple(b: Building) {
        if (b.name == "WheatFarm") {
            val mod = BuildingMod(
                from = "HatshepsutTemple",
                bldName = b.name,
                type = BuildingModType.WONDER,
                target = BuildingModTarget.OUTPUT,
                value = waterNextToWheat
            )

            applyModToBuilding(b, mod)
        }
    }

    private fun applyPyramidOfGiza(b: Building) {
        if (b.output.keys.any { it.isWorker }) {
            val mod = BuildingMod(
                from = "PyramidOfGiza",
                bldName = b.name,
                type = BuildingModType.WONDER,
                target = BuildingModTarget.OUTPUT,
                value = 1.0
            )

            applyModToBuilding(b, mod)
        }
    }

    private fun applyTempleOfHeaven(b: Building) {
        if (b.type == BuildingType.NORMAL) {
            applyModToBuilding(
                b, BuildingMod(
                    from = "TempleOfHeaven",
                    bldName = b.name,
                    type = BuildingModType.WONDER,
                    target = BuildingModTarget.WORKER,
                    value = 1.0
                )
            )
        }
    }

    private fun applyTerracottaArmy(b: Building) {
        if (b.name == "IronForge") {
            val mod = BuildingMod(
                from = "TerracottaArmy",
                bldName = b.name,
                type = BuildingModType.WONDER,
                target = BuildingModTarget.OUTPUT,
                value = ironNextToForge
            )

            applyModToBuilding(b, mod)
        }
    }

    private fun applyRijksmuseum(b: Building) {
        if (b.output.keys.any { it.name == "Culture" } || b.input.keys.any { it.name == "Culture" }) {
            listOf(BuildingModTarget.OUTPUT, BuildingModTarget.WORKER, BuildingModTarget.STORAGE).map {
                applyModToBuilding(
                    b, BuildingMod(
                        from = "Rijksmuseum",
                        bldName = b.name,
                        type = BuildingModType.WONDER,
                        target = it,
                        value = 1.0
                    )
                )
            }
        }
    }

    private fun applySummerPalace(b: Building) {
        if (b.output.keys.any { it.name == "Gunpowder" } || b.input.keys.any { it.name == "Gunpowder" }) {
            listOf(BuildingModTarget.OUTPUT, BuildingModTarget.WORKER, BuildingModTarget.STORAGE).map {
                applyModToBuilding(
                    b, BuildingMod(
                        from = "SummerPalace",
                        bldName = b.name,
                        type = BuildingModType.WONDER,
                        target = it,
                        value = 1.0
                    )
                )
            }
        }
    }

    private fun applyGoldenGateBridge(b: Building) {
        if (b.output.keys.any { it.name == "Power" }) {
            applyModToBuilding(
                b,
                BuildingMod(
                    from = "GoldenGateBridge",
                    bldName = b.name,
                    type = BuildingModType.WONDER,
                    target = BuildingModTarget.OUTPUT,
                    value = 1.0
                )
            )
        }
    }

    private fun applyUnitedNations(b: Building) {
        if (b.tier in 4..6) {
            listOf(BuildingModTarget.OUTPUT, BuildingModTarget.WORKER, BuildingModTarget.STORAGE).forEach {
                applyModToBuilding(
                    b, BuildingMod(
                        from = "UnitedNations",
                        bldName = b.name,
                        type = BuildingModType.WONDER,
                        target = it,
                        value = 1.0
                    )
                )
            }
        }
    }

    private fun applyMountTai(b: Building) {
        if (b.type == BuildingType.NORMAL && b.output.keys.any { it.isScience }) {
            applyModToBuilding(
                b, BuildingMod(
                    from = "MountTai",
                    bldName = b.name,
                    type = BuildingModType.WONDER,
                    target = BuildingModTarget.OUTPUT,
                    value = 1.0
                )
            )
        }
    }

    private fun applyCNTower(b: Building) {
        val age = b.unlockedBy?.age ?: return

        if (age.name in listOf("WorldWarAge", "ColdWarAge")) {
            val m = abs(age.id + 1 - b.tier).toDouble()

            listOf(BuildingModTarget.OUTPUT, BuildingModTarget.WORKER, BuildingModTarget.STORAGE).forEach {
                applyModToBuilding(
                    b, BuildingMod(
                        from = "CNTower",
                        bldName = b.name,
                        type = BuildingModType.WONDER,
                        target = it,
                        value = m
                    )
                )
            }
        }
    }

    private fun applyModToBuilding(b: Building, mod: BuildingMod) {
        val multi = getModsValue(mod)

        if (multi <= 0.0) {
            return
        }

        b.specialMods.add(
            ActiveBuildingMod(
                mod = mod,
                note = "${mod.type} ${mod.from}: ${mod.value} ${mod.target} for ${mod.bldName} @ $multi",
                effect = multi
            ).also {
                log.atTrace()
                    .setMessage("Applied programmatic wonder mod to building.")
                    .addKeyValue("building") { b.name }
                    .addKeyValue("source") { mod.from }
                    .addKeyValue("target") { mod.target }
                    .addKeyValue("multi") { multi }
                    .log()
            }
        )
    }

    private fun getModsValue(mod: BuildingMod): Double {
        return if (model.tiles.values.any { it.building?.bld?.name == mod.from && it.explored }) {
            mod.value
        } else {
            0.0
        }
    }
}