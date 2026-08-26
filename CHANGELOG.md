# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Added

- Every `mf.*` permission node is now registered in `plugin.yml`, not just `mf.help`. Only registered nodes are visible to permission managers such as LuckPerms, so the rest could not be listed, tab-completed or grouped there before. The registered defaults match what Bukkit was already falling back to for an unregistered node (`mf.help` and `mf.default` for everyone, all others operator-only), so no player's direct grants gain or lose access. Servers that grant a wildcard such as `mf.*` are the exception and should review their groups: permission managers expand a wildcard over the nodes plugins have registered, so a wildcard that previously reached only `mf.help` now reaches every node, including the `mf.force.*` admin actions.
- A `Dev Release` workflow, which republishes a rolling `dev` prerelease of `main` on every non-documentation push. This is what Dan's Plugin Manager's experimental channel installs from: `/dpm get minifactions --experimental` reads `releases/tags/dev`, so without it there is nothing for that command to download. The prerelease is unreleased, unreviewed code and is marked as such.

### Fixed

- `/mf help` no longer advertises a `/mf config view` sub-command that does not exist. Following that line got a player the reply "Sub-commands: show, set", the plugin contradicting its own help; the line now reads `/mf config <show | set>`, which is what both the command and `COMMANDS.md` already agreed on. The `/mf unclaim` line's description was also capitalised to match every other line in the block.
- The `/mf force invite` help line no longer misspells "Forcefully" as "Forcecefully".
- The bare `/mf` command now honours its `mf.default` permission. It is invoked outside the command service that checks permissions for every other command, so the node it declared was never queried. `mf.default` defaults to `true`, which is the access everyone had while it went unchecked; revoking it now actually denies the command.
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
