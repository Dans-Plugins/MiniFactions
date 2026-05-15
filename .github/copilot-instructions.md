# Copilot Instructions

This repository follows the DPC (Dans Plugins Community) conventions defined at
https://github.com/Dans-Plugins/dpc-conventions. Read those conventions before
making any changes.

## Technology Stack

- Language: Java
- Build tool: Maven
- Target platform: Spigot / Paper (Minecraft plugin)
- API version: 1.13+

## Project Structure

- `src/main/java/dansplugins/minifactions/` – Plugin source code
- `src/main/java/dansplugins/minifactions/commands/` – Command handlers
- `src/main/java/dansplugins/minifactions/eventhandlers/` – Event listeners
- `src/main/java/dansplugins/minifactions/objects/` – Domain objects (Faction, FactionPlayer, etc.)
- `src/main/java/dansplugins/minifactions/services/` – Services (config, etc.)
- `src/main/resources/` – `plugin.yml` and resource files

## Coding Conventions

- Follow existing package structure when adding new classes.
- Commands extend `AbstractMFCommand`.
- Config options are managed through `LocalConfigService`.

## Contribution Workflow

- Branch from `main` for all changes.
- Open a pull request against `main`.
- Reference the related GitHub issue in every pull request description.
