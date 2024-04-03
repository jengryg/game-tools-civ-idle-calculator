package game.loader.player

import Logging
import game.loader.game.GameData
import game.loader.player.converter.ObtainedGreatPersonConverter
import game.loader.player.converter.TileConverter
import game.loader.player.converter.TransportationConverter
import game.loader.player.data.TileData
import game.loader.player.data.TransportationData
import logger

class PlayerConverter(
    configuration: PlayerConverterConfiguration? = null,
    private val gd: GameData
) : Logging {
    private val log = logger()

    val cfg = configuration ?: PlayerConverterConfiguration().also {
        log.atInfo()
            .setMessage("Initialized ${this::class.simpleName} with default configuration.")
            .log()
    }

    fun convert(ps: PlayerJson): PlayerData {
        val city = gd.cities[ps.saveGame.current.city]
            ?: throw IllegalArgumentException("Player is currently on unknown city ${ps.saveGame.current.city}!")
        // just simply select the city of the current run from GameData

        val unlockedTechnology = ps.saveGame.current.unlockedTech.mapValues { gd.technologies[it.key]!! }
        // just simply map name to TechnologyData instance of the unlocked technology

        val obtainedGreatPeople = processGreatPersons(ps)

        val tiles = processTiles(ps)

        val transportation = processTransportation(ps, tiles)

        return PlayerData(
            city = city,
            unlockedTechnology = unlockedTechnology,
            obtainedGreatPeople = obtainedGreatPeople,
            tiles = tiles,
            transportation = transportation
        ).also {
            // TODO apply tasks
        }
    }

    private fun processGreatPersons(ps: PlayerJson) =
        ObtainedGreatPersonConverter(gd).process(
            ps.saveGame.current.greatPeople,
            ps.saveGame.options.greatPeople
        ).also {
            log.atInfo()
                .setMessage("Processed obtained great persons.")
                .addKeyValue("size") { it.size }
                .log()
        }

    private fun processTiles(ps: PlayerJson) =
        TileConverter(gd).process(ps.saveGame.current.tiles.value).also {
            log.atInfo()
                .setMessage("Processed tiles.")
                .addKeyValue("size") { it.size }
                .log()
        }

    private fun processTransportation(ps: PlayerJson, tiles: Map<Int, TileData>): Map<Int, TransportationData> =
        TransportationConverter(gd, tiles).process(ps.saveGame.current.transportation.value).also {
            log.atInfo()
                .setMessage("Processed transportations.")
                .addKeyValue("size") { it.size }
                .log()
        }
}