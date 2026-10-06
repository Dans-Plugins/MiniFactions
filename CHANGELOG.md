# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Changed

- The usage-reporting "Details" link (startup notice, `config.yml` and the docs) now points at https://danielstephenson.dev/usage-reporting, a public page; the previous link led to a private repository and returned 404 for everyone. The vendored trace client is now 0.6.1, which carries the same link in the `plugins/trace/config.yml` header it writes. Details: https://github.com/Stephenson-Software/trace-client-java/releases/tag/0.6.1.

### Added

- The supported Minecraft versions are now declared in `minecraft-versions.json` (currently 1.19.4, 1.21.11 and 26.2) and listed in the README. Every build checks that the plugin only uses Bukkit API present on each of them, and every stable release candidate is booted on a server of each before it is published.

### Changed

- The player commands now work without a permissions plugin. `mf.list`, `mf.info`, `mf.create`, `mf.join`, `mf.leave`, `mf.invite`, `mf.kick`, `mf.disband`, `mf.transfer`, `mf.power`, `mf.claim`, `mf.unclaim` and `mf.checkclaim` default to `true` in `plugin.yml`; they were `op`, so on a server without a permissions plugin an ordinary player could use nothing beyond `/mf` and `/mf help` (#102). `mf.config` and every `mf.force` node stay `op`. A server that grants or denies these nodes through a permissions plugin is unaffected.
- The vendored trace client is now 0.4.0 (by way of 0.3.0). `plugins/trace/config.yml` can carry a `tags:` block whose entries are added to every event reported by every plugin on the server (the release gates write `ci: "true"` there so that test-server boots are left out of real-installation figures); nothing changes for a server without a `tags:` block. Every event, `command` as well as `startup`, now carries the plugin version, which previously only the `startup` event did. Details: https://github.com/Stephenson-Software/trace-client-java/releases/tag/0.3.0

### Fixed

- `/mf disband` now refuses anyone but the faction's leader, with "You are not the leader of your faction.", as `/mf kick`, `/mf invite`, `/mf transfer`, `/mf claim` and `/mf unclaim` already did. Any member holding `mf.disband` could delete the faction and its claims. The gap was harmless while that node was op-only, but it now defaults to `true`.
- `/mf info <faction>` now shows the named faction. It read the faction name from the second word after `info` instead of the first, so with one word it failed with an internal error rather than looking the faction up. Traced through the source; not reproduced on a live server.
- `/mf unclaim` and `/mf force unclaim <faction>` no longer release a chunk held by a different faction. Unclaiming cleared the owner of whatever chunk the sender stood in, so a faction leader could free another faction's land (answered with "Something went wrong." while it happened), and the freed chunk stayed counted in its real owner's territory. Both commands now refuse with "This territory is not claimed by …" and leave the chunk as it was.
- `/mf force invite <ign> <faction>` and `/mf force join <ign> <faction>` now tell the admin who ran them when the faction is not found ("That faction wasn't found.") or the lookup fails ("Something went wrong."). Both messages went to the target player instead, so the admin got no reply. Traced through the source; not reproduced on a live server.
- `/mf force unclaim <faction>` on a chunk that is not claimed now answers "This territory is not claimed by <faction>." instead of failing with an internal error, and its usage line names `/mf force unclaim` rather than `/mf force claim`.

### Documentation

- `/mf help` and `COMMANDS.md` now show the optional arguments of `/mf info [faction]` and `/mf power [player]`, which both commands accepted but neither reference mentioned.

## [0.2.0] – 2026-09-19

### Changed

- Usage reporting is now disclosed at every startup: an INFO line says that MiniFactions sends its name, version and command names to the trace server and how to turn it off, or, when it is off, why (`environment`, `server-wide config: plugins/trace/config.yml`, `config.yml` or `no key`). It can now also be turned off for every plugin on the server that reports this way with `enabled: false` in `plugins/trace/config.yml` (created by the first such plugin to enable), or for the whole server process with `TRACE_USAGE_REPORTING=off` / `DO_NOT_TRACK=1`. The `usage-reporting` block is written into an existing `config.yml` that lacks it, once, so the switch is visible on servers upgraded from before it existed. The README and `CONFIG.md` describe what is sent and every way to turn it off. Nothing about what is sent changed.

### Added

- The plugin now reports usage events — `startup` on enable, `command` on each of its commands — to the author's trace server so it is known which plugins are in use. Events carry the plugin name, the event name, and the plugin version or command name; nothing about players or the server. Reporting runs off the main thread, never delays a tick, drops silently when the server is unreachable, and is turned off with `usage-reporting.enabled: false` in `config.yml`. The default config carries the plugin's key, so reporting is active out of the box unless turned off — including on servers upgraded from a version before the `usage-reporting` block existed, whose `config.yml` is only rewritten on a version change: the plugin reads the bundled defaults for any key the file lacks.
- Every `mf.*` permission node is now registered in `plugin.yml`, not just `mf.help`. Only registered nodes are visible to permission managers such as LuckPerms, so the rest could not be listed, tab-completed or grouped there before. The registered defaults match what Bukkit was already falling back to for an unregistered node (`mf.help` and `mf.default` for everyone, all others operator-only), so no player's direct grants gain or lose access. Servers that grant a wildcard such as `mf.*` are the exception and should review their groups: permission managers expand a wildcard over the nodes plugins have registered, so a wildcard that previously reached only `mf.help` now reaches every node, including the `mf.force.*` admin actions.
- A `Dev Release` workflow, which republishes a rolling `dev` prerelease of `main` on every non-documentation push. This is what Dan's Plugin Manager's experimental channel installs from: `/dpm get minifactions --experimental` reads `releases/tags/dev`, so without it there is nothing for that command to download. The prerelease is unreleased, unreviewed code and is marked as such.

### Fixed

- `/mf help` no longer advertises a `/mf config view` sub-command that does not exist. Following that line got a player the reply "Sub-commands: show, set", the plugin contradicting its own help; the line now reads `/mf config <show | set>`, which is what both the command and `COMMANDS.md` already agreed on. The `/mf unclaim` line's description was also capitalised to match every other line in the block.
- The `/mf force invite` help line no longer misspells "Forcefully" as "Forcecefully".
- The bare `/mf` command now honours its `mf.default` permission. It is invoked outside the command service that checks permissions for every other command, so the node it declared was never queried. `mf.default` defaults to `true`, which is the access everyone had while it went unchecked; revoking it now actually denies the command.
- `PersistentData.hasPowerRecord()` no longer creates the very record it is asked about. It delegated to `getPowerRecord()`, which creates a record on demand, so the answer was always `true` and asking the question added a power record at the configured starting power for any UUID given to it. It is now a plain lookup that returns `false` when no record is held. No player-facing behaviour changes: the sole caller, the join handler, ended up with a record created either way, and `getPowerRecord()` keeps creating on demand for the commands and handlers that rely on it.
- The `Dev Release` workflow now retries publishing the `dev` prerelease before giving up. The release and its tag have to be deleted and recreated for the tag to move to the new commit, and a transient API failure inside that window previously left the repository with no `dev` release at all until the workflow was re-run by hand. Each attempt now starts from a clean slate, and an exhausted retry fails loudly.

## [0.2.0-SNAPSHOT-8-8-2026] – 2026-08-08

### Changed
- MiniFactions is now developed AI-first. Day-to-day feature work, grooming, review and maintenance run through AI agents working directly against this repository, with the maintainers setting direction and approving what lands. The version bump marks that change in how the project is built — it is not a break in behaviour, configuration or stored data, and existing installations can upgrade in place. Released as `0.2.0-SNAPSHOT-8-8-2026`: the AI-first line has not yet been verified in live operation, and the dated snapshot designation stays until it has.

### Fixed
- `/mf claim`, `/mf force claim`, and `/mf checkclaim` no longer error out (or silently fail) on a territory chunk left claimed by a faction that has since been disbanded. Re-claiming a previously-existing, currently-unclaimed chunk now correctly registers it with the new owning faction so a later disband releases it again, and any already-stale claim is now automatically unclaimed with a message instead of crashing.

### Documentation
- `COMMANDS.md` and `USER_GUIDE.md` now document the bare `/mf` command and the seven `/mf force <subcommand>` permissions (`mf.force.help`, `mf.force.join`, `mf.force.invite`, `mf.force.kick`, `mf.force.disband`, `mf.force.claim`, `mf.force.unclaim`), which existed in code but were previously undocumented.

## [0.1-ALPHA]

### Added
- Faction creation, disbanding, and management commands
- Player invitation and kick system
- Faction territory claiming via chunk system
- Power system: players gain and lose power, which limits territory size
- Config options for power costs, death penalty, and chunk requirements
- `/mf config` command for in-game configuration management
