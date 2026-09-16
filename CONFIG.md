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
| `usage-reporting.enabled` | Boolean | `true` | Whether the plugin reports usage events (see below). Set to `false` to turn it off. |
| `usage-reporting.endpoint` | String | `https://trace.danielstephenson.dev` | The trace server events are sent to. |
| `usage-reporting.key` | String | the plugin's key | Identifies this plugin to the trace server so reports are attributed to it. Not a secret: it ships in the default config and can only report as MiniFactions. Empty means reporting is off regardless of `enabled`. |

## Usage reporting

When the plugin is enabled, and each time one of its commands is used, a small event is sent to the
author's [trace](https://github.com/Stephenson-Software/trace-client-java) server so it is known which
plugins are actually in use. An event carries the plugin's name, the event name (`startup` or
`command`), and either the plugin version or the command name — nothing about players, the world, or
the server. Sending happens off the main thread, never delays a tick, and is dropped silently if the
server cannot be reached. Set `usage-reporting.enabled` to `false` to turn it off, either in the file
or with `/mf config set usage-reporting.enabled false`.

It can also be turned off for every plugin on the server that reports this way, with `enabled: false`
in `plugins/trace/config.yml` (created the first time such a plugin enables; plugins never turn it
back on), or for the whole server process with the environment variable `TRACE_USAGE_REPORTING=off`
or `DO_NOT_TRACK=1`. Each startup logs whether reporting is on or, if it is off, why. The
`usage-reporting` block is written into `config.yml` on the first enable that finds it missing, so
the switch is visible on servers upgraded from before it existed. Details:
<https://github.com/Stephenson-Software/trace#usage-reporting>.
