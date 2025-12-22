# MAINTENANCE REPORT — Exe1 (Release v0.0.1)

**Status**
- Tag: `v0.0.1` (pushed to `origin`)
- Branch: `main` updated with maintenance commits
- Author: Maintenance performed by course team
- Date: 2025-12-17

## 1 Executive summary
This maintenance cycle normalised Java package names, fixed test package mismatches, added minimal package-level Javadoc stubs, produced the Maven site reports, and prepared artifacts and scripts to track remaining quality work. All functional tests pass locally. Several static-analysis issues remain and require small code-style fixes.

Key outcomes:
- Source and test packages normalised to `iso2.exe1`.
- `package-info.java` files added to satisfy package Javadoc checks.
- `mvn clean site` run and site snapshot saved as `site-comparison/after.zip`.
- Helper script added: `scripts/gh-issues-exe1.ps1` to create/close GitHub issues.

## 2 Scope (what we changed)
- Normalise package names and file layout in source and test trees.
- Correct `package` declarations inside Java files that referenced wrong case or path.
- Add lightweight `package-info.java` files for `iso2.exe1` and `iso2.exe1.domain`.
- Run Maven verification and site generation to collect reports (Checkstyle, PMD, Surefire, JaCoCo).
- Produce release tag `v0.0.1` and a markdown maintenance report for the project wiki.

## 3 Actions performed (commands executed)
From repository root:

```powershell
mvn -B -DskipTests=false clean test
mvn -B clean site
Compress-Archive -Path target/site -DestinationPath site-comparison/after.zip
git add docs scripts site-comparison src
git commit -m "chore(maintenance): add Maintenance-Exe1 docs, scripts and site comparison" || Write-Host "No changes to commit"
git push origin HEAD
git tag -a v0.0.1 -m "Release v0.0.1"
git push origin v0.0.1
```

Note: `gh release create` is prepared in the Publish section below; run it locally when ready.

## 4 Diagnostics & evidence (local)
- Surefire: 1 test executed — Tests run: 1, Failures: 0, Errors: 0 (see `target/surefire-reports/TEST-iso2.exe1.AppTest.xml`).
- Checkstyle: 47 reported issues (see `target/site/checkstyle.html`). Primary categories: missing Javadoc on public elements, `LineLength`, `MagicNumber`, non-final parameters, `HiddenField`, `LeftCurly`.
- PMD: no critical findings in the quick scan (see `target/site/pmd.html`).
- JaCoCo: `target/jacoco.exec` generated; however, agent instrumentation failed for some JDK 25 classes (incompatibility). Recommendation: run coverage generation under JDK 17 or upgrade JaCoCo.
- Site snapshot: `site-comparison/after.zip` contains the generated `target/site/` output from the run.

## 5 Files changed and commits
- Added: `src/main/java/iso2/exe1/package-info.java`
- Added: `src/main/java/iso2/exe1/domain/package-info.java`
- Added: `scripts/gh-issues-exe1.ps1`
- Added/Updated: `docs/Maintenance-Exe1.md` (this report)
- Snapshot: `site-comparison/after.zip`

Relevant commits:
- `af4b8ef` Normalize packages to lowercase; add package-info-ready structure
- `daee24f` Remove legacy uppercase files and normalize packages
- `32cac32` Fix merged-case files: keep lowercase package versions
- `bcef1df` chore(maintenance): add Maintenance-Exe1 docs, scripts and site comparison
- `05fa2bb` chore(maintenance): add package-info for iso2.exe1 packages to satisfy Checkstyle

## 6 Impact analysis

Functional impact:
- Low. Changes were limited to package declarations and file placement; business logic was not modified.
- Unit tests executed successfully locally (1 test). Additional test coverage should be run via CI.

Build/CI impact:
- Medium. JaCoCo instrumentation failed under JDK 25 during local site generation. This may cause unreliable coverage reports in CI if the runner uses the same JDK.
- Checkstyle currently reports 47 issues — these will not break the build unless Checkstyle is enforced in CI. If CI enforces stricter rules, a follow-up PR will be required.

Developer & user impact:
- Small positive impact: tests and package layout now follow standard conventions improving IDE experience and enabling tooling.
- No user-visible behavior changes expected.

Documentation impact:
- Updated maintenance documentation added; maintainers should publish this to the repository wiki.

Security impact:
- None identified from these changes.

## 7 Risk assessment and mitigation
- Risk: JaCoCo instrumentation failure under JDK 25 leads to missing/incorrect coverage reports.
  - Mitigation: Run CI jobs on JDK 17 or upgrade JaCoCo to a version compatible with JDK 25 before trusting coverage numbers.

- Risk: Checkstyle issues may be enforced in CI and block merges.
  - Mitigation: Create a follow-up PR that fixes the trivial Checkstyle items (Javadoc stubs, line length, trailing whitespace). For larger design issues, open tracked issues and stage fixes.

- Risk: Accidental behavioral change while renaming/moving files.
  - Mitigation: Run full test suite (unit+integration) in CI and perform smoke test, plus provide a rollback plan (see below).

## 8 Validation & acceptance criteria
Before releasing v0.0.1 on GitHub, validate:
1. CI pipeline completes successfully with tests passing.
2. Checkstyle is either relaxed in CI for now or the follow-up PR addressing the 47 issues has been merged.
3. JaCoCo coverage is produced reliably (CI uses JDK 17 or JaCoCo upgraded).
4. The site at `target/site/index.html` opens locally and contains Checkstyle/PMD reports.

Validation steps (manual):
```powershell
mvn -B -DskipTests=false clean test
mvn -B clean site
start target/site/index.html
```

Acceptance criteria:
- Unit tests: all pass.
- No functional regressions detected by the test suite.
- Maintenance documentation exists in `docs/Maintenance-Exe1.md` and is suitable for wiki publication.

## 9 Rollback plan
If an issue is discovered after publishing the release:
1. Create a hotfix branch from `main` (or revert the release commit if needed).
2. Fix the problem, run tests locally and in CI.
3. Publish new patch release (e.g., `v0.0.2`) or revert tag and update release notes.

## 10 Release notes (draft)
Release v0.0.1 — Maintenance snapshot
- Normalized Java packages to `iso2.exe1`.
- Fixed test package mismatches.
- Added package-level Javadoc stubs to reduce Checkstyle noise.
- Generated site reports and included a site snapshot for review.

## 11 Publish steps (commands)
Run locally (requires `gh` CLI and authentication):

```powershell
git add docs scripts site-comparison src
git commit -m "chore(maintenance): add Maintenance-Exe1 docs, scripts and site comparison" || Write-Host "No changes to commit"
git push origin HEAD
git tag -a v0.0.1 -m "Release v0.0.1"
git push origin v0.0.1
gh release create v0.0.1 --title "v0.0.1" --notes-file docs/Maintenance-Exe1.md --target main
```

## 12 Artifacts produced
- `target/site/` — generated Maven site (locally)
- `site-comparison/after.zip` — snapshot of the generated site
- `docs/Maintenance-Exe1.md` — this report

## 13 Estimated effort and next steps
- Estimated effort to fix remaining Checkstyle issues: 1–3 hours depending on scope (most are Javadoc and formatting).
- Recommended immediate actions:
  1. Decide JaCoCo strategy (CI JDK 17 vs JaCoCo upgrade).
  2. Create a small PR fixing trivial Checkstyle issues (I can prepare it).
  3. Publish release once CI validation is green.