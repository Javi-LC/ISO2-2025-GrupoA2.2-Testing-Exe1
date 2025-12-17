# MAINTENANCE — Release v0.0.1

**Status**
- Release: v0.0.1 (tag pushed and release published) — (adjust if you publish later)
- Maintenance branch: merged into main (merge commit: _insert-hash-if-applicable_)

## Short Summary
This maintenance work normalizes package names and test locations, fixes compilation and test issues caused by duplicated or mis-cased files, adds small documentation and helper scripts, and generates quality reports (`mvn site`). Some remaining style and tooling items are listed below.

## Changes (high level)
- Normalized package names and moved tests/sources to lowercase packages (`iso2.exe1`).
- Added or fixed package declarations in Java source files.
- Removed duplicate legacy files that caused compilation/package mismatches.
- Added reporting artifacts and (optional) comparison archives under `site-comparison/`.
- Added `scripts/gh-issues-exe1.ps1` to create/close maintenance issues.

## Commits of interest
- Maintenance commit: _insert-commit-hash_
- Merge to main: _insert-merge-hash_
- Version bump: _insert-version-bump-hash_

## Diagnostics (current)
- Checkstyle: (run `mvn clean site` to get current numbers) — check `target/site/checkstyle.html`.
- PMD: review `target/site/pmd.html`.
- JaCoCo: coverage reports may be unreliable under JDK 25 (instrumentation failures observed). Recommendation: run CI under JDK 17 or update JaCoCo plugin to a version compatible with your JDK.

## Comparison summary
Key metrics (fill after generating both before/after snapshots):

- Checkstyle errors: (before → after)
- PMD warnings: (before → after)
- JaCoCo: note about JDK compatibility

Artifacts (repository):

- `site-comparison/before.zip` — baseline site snapshot (if available)
- `site-comparison/after.zip` — final site snapshot

## Issues (summary)
The following maintenance issues were created and can be closed once work is done (examples):

- ISSUE-1: Fix test / package name mismatches — resolved
- ISSUE-2: Complete remaining Checkstyle fixes — in progress
- ISSUE-3: Re-enable SpotBugs / FindBugs analysis — logged
- ISSUE-4: Obtain reliable coverage reports (JaCoCo/JDK) — logged

See `issues/` or the repository issue tracker for original markdowns and discussion.

## How to reproduce the analysis locally
From repository root:

```powershell
mvn clean site
# then open target/site/index.html in your browser
mvn test
```

## Report locations (local `target/site`)

- Checkstyle: `target/site/checkstyle.html`
- PMD: `target/site/pmd.html`
- JaCoCo aggregate: `target/site/jacoco/index.html` (may be incomplete under JDK 25)
- Surefire report: `target/site/surefire-report.html`

## Files changed in this maintenance
- Several `*.java` files under `src/main/java/iso2/exe1/` — package declarations and renames
- Several `*.java` files under `src/test/java/iso2/exe1/` — test package fixes
- `scripts/gh-issues-exe1.ps1` — added helper to create/close issues

## Remaining tasks / recommended next steps

1. Fix remaining Checkstyle errors (add missing `@param/@return`, wrap long lines). Priority: Medium. Estimated effort: small. Owner: @student.
2. Resolve Surefire test-class/package mismatches if any remain (ensure source file paths and package declarations match). Priority: High. Owner: @student.
3. Decide JaCoCo path: update to a version supporting your JDK or run CI under JDK 17. Priority: Medium. Owner: maintainer/CI owner.
4. Re-enable SpotBugs and analyze results after the tool/JDK decision. Priority: Low→Medium.
5. Finalize release: if the team accepts v0.0.1 RC, update `pom.xml` to the release version, tag and create release notes, and close the maintenance loop.

## Release artifacts and links

- Repo: https://github.com/<owner>/ISO2-2025-GrupoA2.2-Testing-Exe1
- (When available) Release page: https://github.com/<owner>/ISO2-2025-GrupoA2.2-Testing-Exe1/releases/tag/v0.0.1
- Site comparison archives: `site-comparison/before.zip`, `site-comparison/after.zip`

---

*Generated: 2025-12-17*
