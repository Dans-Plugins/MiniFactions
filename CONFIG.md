# MiniFactions Configuration

The configuration file is located at `plugins/MiniFactions/config.yml`.

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `version` | String | *(plugin version)* | Plugin version. Do not edit manually. |
| `debugMode` | Boolean | `false` | Enables verbose debug logging to the console. |
| `initialPower` | Double | `50.0` | Starting power level for new players. |
| `territoryCostsPower` | Boolean | `true` | Whether claiming territory costs power. |
| `minimumPowerCost` | Double | `1.0` | Minimum power cost to claim a chunk. |
| `losePowerOnDeath` | Boolean | `true` | Whether players lose power when they die. |
| `percentagePowerLostOnDeath` | Double | `0.10` | Fraction of power lost on death (e.g. `0.10` = 10%). |
| `chunkRequirementFactor` | Double | `0.10` | Factor used to calculate how much power is required per claimed chunk. |
