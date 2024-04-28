package game.model

import DEFAULT_OUTPUT_PATH

class ModelFactoryConfiguration(
    val output: String = "$DEFAULT_OUTPUT_PATH/model.json",
    val producers: Map<String, String> = mapOf(
        "Worker" to "Condo",
        "Power" to "HydroDam",
        "Science" to "School",
        "Water" to "HydroDam",
        "Tool" to "IronForge",
        "Faith" to "StPetersBasilica",
    )
)