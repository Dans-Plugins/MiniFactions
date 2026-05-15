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
| `mf.help` | `true` | Access to all MiniFactions commands. |

## Support

Ask questions in the [Discord server](https://discord.gg/xXtuAQ2) or open a [GitHub issue](https://github.com/Dans-Plugins/MiniFactions/issues).
