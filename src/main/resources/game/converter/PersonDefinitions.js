const GreatPersonType = {
    Normal: "Normal",
    Wildcard: "Wildcard",
    Promotion: "Promotion"
}

const GreatPersons = {
    // Bronze /////////////////////////////////////////////////////////////////////////////////////////////////

    Hammurabi: {
        name: () => t(L.Hammurabi),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["House", "Hut"],
        },
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "BronzeAge",
        time: "c. 1800s BC",
    },

    RamessesII: {
        name: () => t(L.RamessesII),
        desc: (self, level) => t(L.RamessesIIDesc, { value: self.value(level) }),
        time: "c. 1300s BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "BronzeAge",
        type: GreatPersonType.Normal,
    },

    TangOfShang: {
        name: () => t(L.TangOfShang),
        desc: (self, level) => t(L.TangOfShangDesc, { value: self.value(level) }),
        time: "c. 1600s BC",
        value: 1 * 0.5,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "BronzeAge",
        type: GreatPersonType.Normal,
    },

    Hatshepsut: {
        name: () => t(L.Hatshepsut),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["Aqueduct", "Brickworks"],
        },
        time: "c. 1507 ~ 1458 BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "BronzeAge",
    },

    SargonOfAkkad: {
        name: () => t(L.SargonOfAkkad),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["LoggingCamp", "LumberMill"],
        },
        time: "c. 2334 ~2279 BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "BronzeAge",
    },

    // Iron ///////////////////////////////////////////////////////////////////////////////////////////////////

    Agamemnon: {
        name: () => t(L.Agamemnon),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["StoneQuarry", "Marbleworks"],
        },
        time: "c. 1200s BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IronAge",
    },

    DukeOfZhou: {
        name: () => t(L.DukeOfZhou),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["CopperMiningCamp", "Blacksmith"],
        },
        time: "c. 1000s BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IronAge",
    },

    Dido: {
        name: () => t(L.Dido),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["DairyFarm", "PoultryFarm", "LivestockFarm"],
        },
        time: "c. 800s BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IronAge",
    },

    Zoroaster: {
        name: () => t(L.Zoroaster),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["CottonPlantation", "CottonMill"],
        },
        time: "c. 1500s BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IronAge",
    },

    // Classical //////////////////////////////////////////////////////////////////////////////////////////////

    Aeschylus: {
        name: () => t(L.Aeschylus),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["FlourMill", "Bakery"],
        },
        time: "c. 500s",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
    },

    Ashurbanipal: {
        name: () => t(L.Ashurbanipal),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["Sandpit", "Glassworks"],
        },
        time: "c. 685 ~ 631 BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
    },

    NebuchadnezzarII: {
        name: () => t(L.NebuchadnezzarII),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["WheatFarm", "Brewery"],
        },
        time: "c. 600s BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
    },

    Herodotus: {
        name: () => t(L.Herodotus),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["PaperMaker", "MusiciansGuild"],
        },
        time: "c. 600s BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
    },

    CyrusII: {
        name: () => t(L.CyrusII),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["IronMiningCamp", "IronForge"],
        },
        time: "c. 600s BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
    },

    Socrates: {
        name: () => t(L.Socrates),
        desc: (self, level) => t(L.SocratesDesc, { value: self.value(level) }),
        time: "c. 600s BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
        type: GreatPersonType.Normal,
    },

    Aristophanes: {
        name: () => t(L.Aristophanes),
        desc: (self, level) => t(L.AristophanesDesc, { value: self.value(level) }),
        time: "446 ~ 386 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
        type: GreatPersonType.Normal,
    },

    Confucius: {
        name: () => t(L.Confucius),
        desc: (self, level) => t(L.ConfuciusDescV2, { value: self.value(level) }),
        time: "c. 600s BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
        type: GreatPersonType.Normal,
    },

    Homer: {
        name: () => t(L.Homer),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["Stable", "PoetrySchool"],
        },
        time: "c. 800s BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
    },

    Archimedes: {
        name: () => t(L.Archimedes),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["Caravansary", "Warehouse"],
        },
        time: "c. 200 BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
    },

    EmperorWuOfHan: {
        name: () => t(L.EmperorWuOfHan),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["Caravansary"],
        },
        time: "156 ~ 87 BC",
        value: 1 * 2,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
        city: "Beijing",
    },

    QinShiHuang: {
        name: () => t(L.QinShiHuang),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["ChariotWorkshop", "Armory"],
        },
        time: "c. 200s BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
    },

    Zenobia: {
        name: () => t(L.Zenobia),
        desc: (self, level) => t(L.ZenobiaDesc, { value: self.value(level) }),
        time: "240 ~ 274 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Normal,
    },

    Plato: {
        name: () => t(L.Plato),
        desc: (self, level) => t(L.WildCardGreatPersonDescV2),
        time: "427 ~ 348 BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Wildcard,
    },

    AshokaTheGreat: {
        name: () => t(L.AshokaTheGreat),
        desc: (self, level) => t(L.PromotionGreatPersonDescV2),
        time: "304 ~ 232 BC",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ClassicalAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Promotion,
    },

    // Middle Age /////////////////////////////////////////////////////////////////////////////////////////////

    Justinian: {
        name: () => t(L.Justinian),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["FurnitureWorkshop", "GarmentWorkshop"],
        },
        time: "482 ~ 565 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "MiddleAge",
    },

    IsidoreOfMiletus: {
        name: () => t(L.IsidoreOfMiletus),
        desc: (self, level) => t(L.IsidoreOfMiletusDesc, { value: self.value(level) }),
        time: "c. 500 AD",
        value: 1 * 2,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "MiddleAge",
        type: GreatPersonType.Normal,
    },

    Charlemagne: {
        name: () => t(L.Charlemagne),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["SwordForge", "KnightCamp"],
        },
        time: "747 ~ 814 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "MiddleAge",
    },

    HarunAlRashid: {
        name: () => t(L.HarunAlRashid),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["CheeseMaker", "Mosque"],
        },
        time: "763 ~ 809 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "MiddleAge",
    },

    WuZetian: {
        name: () => t(L.WuZetian),
        desc: (self, level) => t(L.WuZetianDesc, { value: self.value(level) }),
        time: "624 ~ 705 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "MiddleAge",
        type: GreatPersonType.Normal,
    },

    Xuanzang: {
        name: () => t(L.Xuanzang),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["Library", "Pagoda"],
        },
        time: "602 ~ 664 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "MiddleAge",
    },

    Rurik: {
        name: () => t(L.Rurik),
        desc: (self, level) => t(L.RurikDesc, { value: self.value(level) }),
        time: "624 ~ 705 AD",
        value: 1 * 2,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "MiddleAge",
        type: GreatPersonType.Normal,
    },

    Fibonacci: {
        name: () => t(L.Fibonacci),
        desc: (self, level) => t(L.FibonacciDescV2, { idle: self.value(level) / 2, busy: self.value(level) }),
        time: "c. 1170 ~ 1250 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "MiddleAge",
        type: GreatPersonType.Normal,
    },

    Saladin: {
        name: () => t(L.Saladin),
        desc: (self, level) => t(L.WildCardGreatPersonDescV2),
        time: "1137 ~ 1193 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "MiddleAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Wildcard,
    },

    MarcoPolo: {
        name: () => t(L.MarcoPolo),
        desc: (self, level) => t(L.PromotionGreatPersonDescV2),
        time: "1254 ~ 1324 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "MiddleAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Promotion,
    },

    // Renaissance ////////////////////////////////////////////////////////////////////////////////////////////

    LeonardoDaVinci: {
        name: () => t(L.LeonardoDaVinci),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["PaintersGuild", "University"],
        },
        time: "1452 ~ 1519 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "RenaissanceAge",
    },

    JohannesKepler: {
        name: () => t(L.JohannesKepler),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["Shrine", "LensWorkshop"],
        },
        time: "1571 ~ 1630 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "RenaissanceAge",
    },

    GalileoGalilei: {
        name: () => t(L.GalileoGalilei),
        desc: (self, level) => t(L.GalileoGalileiDesc, { value: self.value(level) }),
        time: "1564 ~ 1642 AD",
        value: 1 * 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "RenaissanceAge",
        type: GreatPersonType.Normal,
    },

    MartinLuther: {
        name: () => t(L.MartinLuther),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["Church", "School"],
        },
        time: "1483 ~ 1546 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "RenaissanceAge",
    },

    WilliamShakespeare: {
        name: () => t(L.WilliamShakespeare),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["WritersGuild", "ActorsGuild"],
        },
        time: "1564 ~ 1616 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "RenaissanceAge",
    },

    ReneDescartes: {
        name: () => t(L.ReneDescartes),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["School"],
        },
        time: "1596 ~ 1650 AD",
        value: 2,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "RenaissanceAge",
    },

    ZhengHe: {
        name: () => t(L.ZhengHe),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["CaravelBuilder", "GalleonBuilder"],
        },
        time: "1371 ~ 1435 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "RenaissanceAge",
    },

    CosimoDeMedici: {
        name: () => t(L.CosimoDeMedici),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["GoldMiningCamp", "CoinMint"],
        },
        time: "1389 ~ 1464 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "RenaissanceAge",
    },

    IsaacNewton: {
        name: () => t(L.IsaacNewton),
        desc: (self, level) => t(L.IsaacNewtonDescV2, { value: self.value(level) }),
        time: "1642 ~ 1727 AD",
        value: 1 * 2,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "RenaissanceAge",
        tick: (self, level, source) => {
            addScienceBasedOnBusyWorkers(self.value(level), source);
        },
        type: GreatPersonType.Normal,
    },

    GeorgiusAgricola: {
        name: () => t(L.GeorgiusAgricola),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["SiegeWorkshop", "CoalMine"],
        },
        time: "1494 ~ 1555 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "RenaissanceAge",
    },

    Michelangelo: {
        name: () => t(L.Michelangelo),
        desc: (self, level) => t(L.WildCardGreatPersonDescV2),
        time: "1475 ~ 1564 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "RenaissanceAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Wildcard,
    },

    JohannesGutenberg: {
        name: () => t(L.JohannesGutenberg),
        desc: (self, level) => t(L.PromotionGreatPersonDescV2),
        time: "1393 ~ 1406 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "RenaissanceAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Promotion,
    },

    // Industrial /////////////////////////////////////////////////////////////////////////////////////////////

    JamesWatt: {
        name: () => t(L.JamesWatt),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["Steamworks", "DynamiteWorkshop"],
        },
        time: "1736 ~ 1819 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
    },

    IsambardKingdomBrunel: {
        name: () => t(L.IsambardKingdomBrunel),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["FrigateBuilder", "ConcretePlant"],
        },
        time: "1806 ~ 1859 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
    },

    LouisSullivan: {
        name: () => t(L.LouisSullivan),
        desc: (self, level) => t(L.LouisSullivanDesc, { value: self.value(level) }),
        time: "1856 ~ 1924 AD",
        value: 1 * 3,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
        type: GreatPersonType.Normal,
    },

    KarlMarx: {
        name: () => t(L.KarlMarx),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["Courthouse", "Parliament"],
        },
        time: "1818 ~ 1883 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
    },

    AdaLovelace: {
        name: () => t(L.AdaLovelace),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["PrintingHouse", "Museum"],
        },
        time: "1815 ~ 1852 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
    },

    NapoleonBonaparte: {
        name: () => t(L.NapoleonBonaparte),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["CannonWorkshop", "GunpowderMill"],
        },
        time: "1769 ~ 1821 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
    },

    CharlesDarwin: {
        name: () => t(L.CharlesDarwin),
        desc: (self, level) => t(L.CharlesDarwinDesc, { value: self.value(level) }),
        time: "1809 ~ 1882 AD",
        value: 1 * 2,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
        type: GreatPersonType.Normal,
    },

    CarlFriedrichGauss: {
        name: () => t(L.CarlFriedrichGauss),
        desc: (self, level) =>
            t(L.CarlFriedrichGaussDesc, { idle: 0.5 * self.value(level), busy: 1.5 * self.value(level) }),
        time: "c. 1777 ~ 1855 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
        type: GreatPersonType.Normal,
    },

    FlorenceNightingale: {
        name: () => t(L.FlorenceNightingale),
        desc: (self, level) => t(L.FlorenceNightingaleDesc, { value: self.value(level) }),
        time: "1820 ~ 1910 AD",
        value: 1 * 3,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
        type: GreatPersonType.Normal,
    },

    JPMorgan: {
        name: () => t(L.JPMorgan),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["BondMarket", "RifleFactory"],
        },
        time: "1837 ~ 1913 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
    },

    AndrewCarnegie: {
        name: () => t(L.AndrewCarnegie),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["Bank", "SteelMill"],
        },
        time: "1837 ~ 1913 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
    },

    ThomasEdison: {
        name: () => t(L.ThomasEdison),
        desc: (self, level) => t(L.WildCardGreatPersonDescV2),
        time: "1847 ~ 1931 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Wildcard,
    },

    AdamSmith: {
        name: () => t(L.AdamSmith),
        desc: (self, level) => t(L.PromotionGreatPersonDescV2),
        time: "1723 ~ 1790 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "IndustrialAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Promotion,
    },

    // World Wars /////////////////////////////////////////////////////////////////////////////////////////////

    JohnDRockefeller: {
        name: () => t(L.JohnDRockefeller),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["OilWell", "StockExchange"],
        },
        time: "1839 ~ 1937 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
    },

    RudolfDiesel: {
        name: () => t(L.RudolfDiesel),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["OilRefinery", "LocomotiveFactory"],
        },
        time: "1858 ~ 1913 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
    },

    PierreDeCoubertin: {
        name: () => t(L.PierreDeCoubertin),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["PublishingHouse", "Stadium"],
        },
        time: "1863 ~ 1937 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
    },

    CharlesParsons: {
        name: () => t(L.CharlesParsons),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["CoalPowerPlant", "IroncladBuilder"],
        },
        time: "1854 ~ 1931 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
    },

    RichardJordanGatling: {
        name: () => t(L.RichardJordanGatling),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["GatlingGunFactory", "TankFactory"],
        },
        time: "1818 ~ 1903 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
    },

    JosephPulitzer: {
        name: () => t(L.JosephPulitzer),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["MagazinePublisher", "Pizzeria"],
        },
        time: "1847 ~ 1911 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
    },

    CharlesMartinHall: {
        name: () => t(L.CharlesMartinHall),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["NaturalGasWell", "AluminumSmelter"],
        },
        time: "1863 ~ 1914 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
    },

    MarieCurie: {
        name: () => t(L.MarieCurie),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["CableFactory", "UraniumMine"],
        },
        time: "1867 ~ 1934 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
    },

    AlbertEinstein: {
        name: () => t(L.AlbertEinstein),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["ResearchLab"],
        },
        time: "1879 ~ 1955 AD",
        value: 2,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
    },

    AlanTuring: {
        name: () => t(L.AlanTuring),
        desc: (self, level) => t(L.AlanTuringDesc, { value: self.value(level) }),
        time: "1912 ~ 1954 AD",
        value: 1 * 2,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
        type: GreatPersonType.Normal,
    },

    NielsBohr: {
        name: () => t(L.NielsBohr),
        desc: (self, level) => t(L.NielsBohrDescV2, { value: self.value(level) }),
        time: "1885 ~ 1962 AD",
        value: 1 * 3,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
        tick: (self, level, source) => {
            addScienceBasedOnBusyWorkers(self.value(level), source);
        },
        type: GreatPersonType.Normal,
    },

    AmeliaEarhart: {
        name: () => t(L.AmeliaEarhart),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["BiplaneFactory", "GasPowerPlant"],
        },
        time: "1897 ~ 1937 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
    },

    HenryFord: {
        name: () => t(L.HenryFord),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["CarFactory", "PlasticsFactory"],
        },
        time: "1863 ~ 1947 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
    },

    MahatmaGandhi: {
        name: () => t(L.MahatmaGandhi),
        desc: (self, level) => t(L.WildCardGreatPersonDescV2),
        time: "1869 ~ 1948 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Wildcard,
    },

    PabloPicasso: {
        name: () => t(L.PabloPicasso),
        desc: (self, level) => t(L.PromotionGreatPersonDescV2),
        time: "1881 ~ 1973 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Promotion,
    },

    JohnVonNeumann: {
        name: () => t(L.JohnVonNeumann),
        desc: (self, level) => t(L.JohnVonNeumannDesc, { value: self.value(level) }),
        time: "1903 ~ 1957 AD",
        value: 1 * 3,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
        type: GreatPersonType.Normal,
    },

    CharlieChaplin: {
        name: () => t(L.CharlieChaplin),
        desc: (self, level) => t(L.CharlieChaplinDesc, { value: self.value(level) }),
        time: "1889 ~ 1977 AD",
        value: 1 * 4,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
        type: GreatPersonType.Normal,
    },

    FrankLloydWright: {
        name: () => t(L.FrankLloydWright),
        desc: (self, level) => t(L.FrankLloydWrightDesc, { value: self.value(level) }),
        time: "1867 ~ 1959 AD",
        value: 1 * 4,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "WorldWarAge",
        type: GreatPersonType.Normal,
    },

    // Cold Wars /////////////////////////////////////////////////////////////////////////////////////////////

    JRobertOppenheimer: {
        name: () => t(L.JRobertOppenheimer),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["UraniumEnrichmentPlant", "AtomicFacility"],
        },
        time: "1904 ~ 1967 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
    },

    WaltDisney: {
        name: () => t(L.WaltDisney),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["MovieStudio", "RadioStation"],
        },
        time: "1901 ~ 1966 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
    },

    WernherVonBraun: {
        name: () => t(L.WernherVonBraun),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["RocketFactory", "ArtilleryFactory"],
        },
        time: "1912 ~ 1977 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
    },

    ChesterWNimitz: {
        name: () => t(L.ChesterWNimitz),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["BattleshipBuilder", "HydroDam"],
        },
        time: "1885 ~ 1966 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
    },

    FrankWhittle: {
        name: () => t(L.FrankWhittle),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["AirplaneFactory", "FighterJetPlant"],
        },
        time: "1907 ~ 1996 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
    },

    EnricoFermi: {
        name: () => t(L.EnricoFermi),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["NuclearPowerPlant", "SubmarineYard"],
        },
        time: "1901 ~ 1954 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
    },

    PhiloFarnsworth: {
        name: () => t(L.PhiloFarnsworth),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["TVStation", "NuclearMissileSilo"],
        },
        time: "1906 ~ 1971 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
    },

    SergeiKorolev: {
        name: () => t(L.SergeiKorolev),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["SatelliteFactory", "SpacecraftFactory"],
        },
        time: "1907 ~ 1966 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
    },

    GeorgeCMarshall: {
        name: () => t(L.GeorgeCMarshall),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["Embassy", "ForexMarket"],
        },
        time: "1880 ~ 1959 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
    },

    RobertNoyce: {
        name: () => t(L.RobertNoyce),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["SemiconductorFab", "SiliconSmelter"],
        },
        time: "1927 ~ 1990 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
    },

    JamesWatson: {
        name: () => t(L.JamesWatson),
        desc: (self, level) => t(L.JamesWatsonDesc, { value: self.value(level) }),
        time: "1928 ~ ",
        value: 1 * 4,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
        type: GreatPersonType.Normal,
    },

    RichardFeynman: {
        name: () => t(L.RichardFeynman),
        desc: (self, level) => t(L.RichardFeynmanDesc, { value: self.value(level) }),
        time: "1918 ~ 1988 AD",
        value: 1 * 4,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
        tick: (self, level, source) => {
            addScienceBasedOnBusyWorkers(self.value(level), source);
        },
        type: GreatPersonType.Normal,
    },

    LinusPauling: {
        name: () => t(L.LinusPauling),
        desc: (self, level) => t(L.LinusPaulingDesc, { value: self.value(level) }),
        time: "1901 ~ 1994 AD",
        value: 1 * 3,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
        type: GreatPersonType.Normal,
    },

    IMPei: {
        name: () => t(L.IMPei),
        desc: (self, level) => t(L.IMPeiDesc, { value: self.value(level) }),
        time: "1917 ~ 2019 AD",
        value: 1 * 5,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
        type: GreatPersonType.Normal,
    },

    BobHope: {
        name: () => t(L.BobHope),
        desc: (self, level) => t(L.BobHopeDesc, { value: self.value(level) }),
        time: "1903 ~ 2003 AD",
        value: 1 * 5,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
        type: GreatPersonType.Normal,
    },

    ElvisPresley: {
        name: () => t(L.ElvisPresley),
        desc: (self, level) => t(L.WildCardGreatPersonDescV2),
        time: "1935 ~ 1977 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Wildcard,
    },

    YuriGagarin: {
        name: () => t(L.YuriGagarin),
        desc: (self, level) => t(L.PromotionGreatPersonDescV2),
        time: "1934 ~ 1968 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "ColdWarAge",
        tick: (self, level, source) => {},
        type: GreatPersonType.Promotion,
    },

    // Information ////////////////////////////////////////////////////////////////////////////////////////////

    TimBernersLee: {
        name: () => t(L.TimBernersLee),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["InternetServiceProvider", "OpticalFiberPlant"],
        },
        time: "1955 ~ ",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "InformationAge",
    },

    GordonMoore: {
        name: () => t(L.GordonMoore),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["ComputerFactory", "SupercomputerLab"],
        },
        time: "1929 ~ 2023 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "InformationAge",
    },

    DennisRitchie: {
        name: () => t(L.DennisRitchie),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["SoftwareCompany", "MaglevFactory"],
        },
        time: "1941 ~ 2011 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "InformationAge",
    },

    HymanGRickover: {
        name: () => t(L.HymanGRickover),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["AircraftCarrierYard", "NuclearSubmarineYard"],
        },
        time: "1900 ~ 1986 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "InformationAge",
    },

    HarryMarkowitz: {
        name: () => t(L.HarryMarkowitz),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["MutualFund", "HedgeFund"],
        },
        time: "1927 ~ 2023 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "InformationAge",
    },

    JohnCarmack: {
        name: () => t(L.JohnCarmack),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["ComputerLab", "CivOasis"],
        },
        time: "1970 ~ ",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "InformationAge",
    },

    SatoshiNakamoto: {
        name: () => t(L.SatoshiNakamoto),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["CivTok", "BitcoinMiner"],
        },
        time: "??? ~ ???",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "InformationAge",
    },

    CarlSagan: {
        name: () => t(L.CarlSagan),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["SpaceCenter", "Peacekeeper"],
        },
        time: "1934 ~ 1996 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "InformationAge",
    },

    JohnMcCarthy: {
        name: () => t(L.JohnMcCarthy),
        boost: {
            multipliers: ["output", "storage"],
            buildings: ["CivGPT", "RobocarFactory"],
        },
        time: "1927 ~ 2011 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "InformationAge",
    },

    StephenHawking: {
        name: () => t(L.StephenHawking),
        desc: (self, level) => t(L.WildCardGreatPersonDescV2),
        time: "1942 ~ 2018 AD",
        value: 1,
        maxLevel: Number.POSITIVE_INFINITY,
        age: "InformationAge",
    },
};