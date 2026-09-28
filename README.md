# MiniFactions

## About
This Minecraft plugin aims to introduce factions into the game in a simple, easy to use, expandable way.

### Characteristics
- Simple
- Minimal
- Expandable

### Donation Request
The plan is to request $1 donations from those that download the software. Users will be able to download the plugin for no cost or donate more than $1 if they wish. The plugin will always remain open source, so individuals will be free to access the source code and create forks of the project.

## Supported Minecraft Versions
This plugin is supported on the Minecraft versions listed in [`minecraft-versions.json`](minecraft-versions.json): currently **1.19.4**, **1.21.11** and **26.2** (Spigot and its forks). Every stable release is booted on a real server of each of these versions before it is published, and every build checks that the plugin only uses Bukkit API that exists on all of them. Other versions from 1.19.4 onwards are expected to work but are not tested. To support another version, add it to the file: both checks pick it up.

## Target Features
- [x] Social Organization
- [x] Territory
- [ ] Configurability
- [ ] Localization

## Commands To Implement
### Social Organization
- [x] List
- [x] Info
- [x] Join
- [x] Leave
- [x] Create
- [x] Invite
- [x] Disband
- [x] Kick
- [x] Transfer

### Territory
- [x] Power
- [x] Claim
- [x] Unclaim
- [x] CheckClaim

### Configurability
- [x] Config
  - [x] View
  - [x] Set
- [ ] Flags
  - [ ] View
  - [ ] Set
- [ ] Perm
  - [ ] View
  - [ ] Get
  - [ ] Set
- [x] Force
  - [x] Help
  - [x] Join
  - [x] Invite
  - [x] Disband
  - [x] Kick
  - [x] Claim
  - [x] Unclaim

### Localization
- [ ] Lang
  - [ ] Get
  - [ ] Set

## bStats
[Check out the bStats page here!](https://bstats.org/plugin/bukkit/MiniFactions/14969)

## Usage reporting

Usage reporting is on by default: MiniFactions sends its name, version and command names (a `startup` event when it enables and a `command` event each time one of its commands is used) to <https://trace.danielstephenson.dev> so it is known which plugins are actually in use. Nothing about players, worlds, IPs or the server is sent, and nothing typed after a command is sent either.

To turn it off:

- for this plugin: `usage-reporting.enabled: false` in `plugins/MiniFactions/config.yml`
- for every plugin on the server that reports this way: `enabled: false` in `plugins/trace/config.yml` (created the first time such a plugin enables)
- for the whole server process: the environment variable `TRACE_USAGE_REPORTING=off` or `DO_NOT_TRACK=1`

Each startup logs whether reporting is on or, if it is off, why. Details: <https://github.com/Stephenson-Software/trace#usage-reporting>
