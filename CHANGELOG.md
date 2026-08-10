# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Added

- A `Dev Release` workflow, which republishes a rolling `dev` prerelease of `main` on every non-documentation push. This is what Dan's Plugin Manager's experimental channel installs from: `/dpm get minifactions --experimental` reads `releases/tags/dev`, so without it there is nothing for that command to download. The prerelease is unreleased, unreviewed code and is marked as such.

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
