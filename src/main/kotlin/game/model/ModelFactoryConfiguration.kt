package game.model

import constants.*

class ModelFactoryConfiguration(
    val output: String = "$DEFAULT_OUTPUT_PATH/model.json",
    val waterNextToWheat: Double = 1.0,
    val ironNextToForge: Double = 1.0,
    val producers: Map<String, String> = mapOf(
        WORKER to APARTMENT,
        POWER to HYDRO_DAM,
        SCIENCE to SCHOOL,
        WATER to HYDRO_DAM,
        TOOL to IRON_FORGE,
        FAITH to ST_PETERS_BASILICA,
    )
)