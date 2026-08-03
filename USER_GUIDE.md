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

- Is spent when claiming chunks (`territoryCostsPower: true` by default).
- Is lost on death (10% by default).
- Determines how much territory your faction can hold.

## Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `mf.help` | `true` | View the help menu. |
| `mf.default` | n/a (not currently enforced) | Bare `/mf` command; shows plugin version and developer info. |
| `mf.list` | `op` | List all factions. |
| `mf.info` | `op` | View faction information. |
| `mf.create` | `op` | Create a faction. |
| `mf.join` | `op` | Join a faction. |
| `mf.leave` | `op` | Leave a faction. |
| `mf.invite` | `op` | Invite a player to your faction. |
| `mf.kick` | `op` | Kick a player from your faction. |
| `mf.disband` | `op` | Disband a faction. |
| `mf.transfer` | `op` | Transfer faction ownership. |
| `mf.power` | `op` | Check power level. |
| `mf.claim` | `op` | Claim a chunk. |
| `mf.unclaim` | `op` | Unclaim a chunk. |
| `mf.checkclaim` | `op` | Check chunk ownership. |
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
