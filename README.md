# Blåhaj × SmoothPlayerAnimations compatibility

Client-side NeoForge 1.21.1 patch for Red's Blåhaj Fork 1.0.2 and SmoothPlayerAnimations 1.0.4+1.21.1. Install the built JAR alongside those two mods and SmoothPlayerAnimations' own dependencies.

This patch is licensed under the MIT License; see [LICENSE](LICENSE). The two target mods retain their own licenses.

Red's fork makes its `CuddlyItem` use the vanilla `CROSSBOW_HOLD` pose and sets both arm angles in `HumanoidModel`. SmoothPlayerAnimations (SPA) then animates player bones in its own lower and upper layers. This patch intercepts SPA's per-player upper-body decision: when either hand holds a `CuddlyItem`, it clears SPA's previous upper action and returns `YIELD`. SPA then masks both arms out of its base animation, preserving its movement and other body animation. The mask also stays in place during SPA's sprint-stop transition. Releasing the item restores SPA's ordinary selection on the next tick. It does not alter Red's mod or SPA JARs.

## Build

Use Java 21, then run `./gradlew build` (`.\gradlew.bat build` on Windows). Gradle downloads NeoForge and compile-only copies of the two target mods from Modrinth. The finished JAR is under `build/libs/`. Neither target mod is packaged into this patch.

## Scope

The patch targets the exact SPA 1.0.4 `PlayerStateMachine.tickUpperBody` and `replayIfChanged` hooks inspected for this project. Red's fork 1.0.2 is the build target even though its internal NeoForge mod version is `1.0.0`. The public upstream source provides the original cuddle logic; Red's project does not publish a source repository, so its released 1.0.2 JAR was also inspected. The project builds successfully with Java 21 and Gradle 9.2.1. In-game visual behavior has not been checked. SPA's optional CEM mode and other animation mods may affect the final pose; check those combinations in game. Only the client needs this patch.

Sources examined: [Red's published fork](https://modrinth.com/mod/reds-blahaj-fork/version/1.0.2), [its public upstream](https://github.com/rhysdh540/Blahaj/tree/1.21.1), and [SmoothPlayerAnimations 1.0.4](https://modrinth.com/mod/smoothplayeranimations/version/1.0.4%2B1.21.1).
