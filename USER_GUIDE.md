# MiniFactions User Guide

## What is MiniFactions?

MiniFactions is a lightweight Spigot plugin that adds a faction system to your Minecraft server. Players can form groups (factions), claim territory, invite allies, and compete for power.

## Installation

1. Download the latest `MiniFactions-<version>.jar` from the [Releases](https://github.com/Dans-Plugins/MiniFactions/releases) page.
2. Place the JAR in your server's `plugins/` folder.
3. Restart the server.
4. The plugin generates `plugins/MiniFactions/config.yml` on first run.

## Getting Started

1. Create a faction: `/mf create <name>`
2. Invite friends: `/mf invite <player>`
3. Claim territory: stand in a chunk and run `/mf claim`
4. Check who owns a chunk: `/mf checkclaim`

## Power System

Each player has a power level (default starting power: 50). Power:

- Is spent by the faction leader each time they claim a chunk (`territoryCostsPower: true` by
  default). A claim costs the number of chunks the faction already holds times
  `chunkRequirementFactor` (0.10 by default), but never less than `minimumPowerCost` (1.0 by
  default). If the leader has less power than that, the claim is refused.
- Is lost on death (10% of the player's current power by default).
- Is not regained: unclaiming a chunk does not refund it, and nothing regenerates it.

Claimed chunks are kept whatever happens to power afterwards. `/mf force claim` costs no power.

## Known Limitations

- **A crash loses the changes made since the last save.** Factions, members, invitations,
  claimed chunks and power levels are written to disk every `autosaveMinutes` minutes (5 by
  default, see [CONFIG.md](CONFIG.md)) and when the server stops or the plugin is disabled, and
  read back when it enables. If the server crashes or is killed instead of being stopped, the
  changes made since the last autosave are lost; with `autosaveMinutes: 0`, every change made
  since it started is lost.

## Data Files

The plugin keeps its data in `plugins/MiniFactions/`, one JSON file per kind of record:

| File | Contents |
|------|----------|
| `factions.json` | Each faction's ID, name, leader, members, invitations and claimed chunk IDs. |
| `powerRecords.json` | Each player's power level. |
| `territoryChunks.json` | Each chunk that has been claimed: its world, coordinates and owning faction, if any. |

If one of these files cannot be read when the plugin enables, the error is logged, its records
are not loaded, and nothing is saved until the next restart, so the file is left as it is for you
to fix or move aside. `territory.json` is created by the plugin but is not used.

## Permissions

Every node below is registered in the plugin's `plugin.yml`, so permission managers such as
LuckPerms can list and grant them by name.

| Permission | Default | Description |
|------------|---------|-------------|
| `mf.help` | `true` | View the help menu. |
| `mf.default` | `true` | Bare `/mf` command; shows plugin version and developer info. |
| `mf.list` | `true` | List all factions. |
| `mf.info` | `true` | View faction information. |
| `mf.create` | `true` | Create a faction. |
| `mf.join` | `true` | Join a faction. |
| `mf.leave` | `true` | Leave a faction. |
| `mf.invite` | `true` | Invite a player to your faction. |
| `mf.kick` | `true` | Kick a player from your faction. |
| `mf.disband` | `true` | Disband a faction. |
| `mf.transfer` | `true` | Transfer faction ownership. |
| `mf.power` | `true` | Check power level. |
| `mf.claim` | `true` | Claim a chunk. |
| `mf.unclaim` | `true` | Unclaim a chunk. |
| `mf.checkclaim` | `true` | Check chunk ownership. |
| `mf.config` | `op` | View or change config options. |
| `mf.force` | `op` | Force admin actions. |
| `mf.force.help` | `op` | View a list of force commands. |
| `mf.force.join` | `op` | Force a player to join a faction. |
| `mf.force.invite` | `op` | Forcefully invite a player to a faction. |
| `mf.force.kick` | `op` | Forcefully kick a player from their faction. |
| `mf.force.disband` | `op` | Forcefully disband a faction. |
| `mf.force.claim` | `op` | Forcefully claim territory for a faction. |
| `mf.force.unclaim` | `op` | Forcefully unclaim territory for a faction. |

## Support

Ask questions in the [Discord server](https://discord.gg/xXtuAQ2) or open a [GitHub issue](https://github.com/Dans-Plugins/MiniFactions/issues).
