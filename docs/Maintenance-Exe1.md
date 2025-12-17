
# MAINTENANCE — Release v0.0.1

**Status**
- Tag `v0.0.1` pushed to remote (`origin`). A GitHub release has not been created from this environment; run the `gh` command below to publish the release page.
- Maintenance changes have been applied locally and committed to `main`.

## Resumen ejecutivo
Se ha realizado mantenimiento funcional y de calidad en el repositorio Exe1. Objetivos cumplidos localmente:

- Normalizar nombres de paquete y rutas de tests a `iso2.exe1`.
- Corregir declaraciones `package` en los ficheros afectados.
- Generar informes de calidad con `mvn clean site` y empaquetar el sitio final en `site-comparison/after.zip`.
- Añadir documentación de mantenimiento y script para gestionar issues.

## Cambios principales

- Paquetes normalizados y ficheros movidos/renombrados bajo:
	- `src/main/java/iso2/exe1/...`
	- `src/test/java/iso2/exe1/...`
- Añadidos `package-info.java` para `iso2.exe1` y `iso2.exe1.domain`.
- Añadido script: `scripts/gh-issues-exe1.ps1`.
- Añadido informe: `docs/Maintenance-Exe1.md` (este fichero).
- Creado snapshot del sitio final: `site-comparison/after.zip`.

## Commits relevantes

- `af4b8ef` Normalize packages to lowercase; add package-info-ready structure
- `daee24f` Remove legacy uppercase files and normalize packages
- `32cac32` Fix merged-case files: keep lowercase package versions
- `bcef1df` chore(maintenance): add Maintenance-Exe1 docs, scripts and site comparison
- `05fa2bb` chore(maintenance): add package-info for iso2.exe1 packages to satisfy Checkstyle

## Estado actual de diagnóstico (local)

- Tests (Surefire): 1 test ejecutado — `Tests run: 1, Failures: 0, Errors: 0` (ver `target/surefire-reports/TEST-iso2.exe1.AppTest.xml`).
- Checkstyle: 47 errores detectados en 2 ficheros. Informe completo en `target/site/checkstyle.html`.
	- Problemas más frecuentes: falta de Javadoc en elementos públicos, `LineLength`, `MagicNumber`, parámetros no `final`, `HiddenField`, `LeftCurly`.
- PMD: sin problemas críticos detectados en inspección rápida; ver `target/site/pmd.html`.
- JaCoCo: `target/jacoco.exec` generado, pero la instrumentación dio errores con JDK 25 (incompatibilidad de versión). Recomendación: ejecutar CI con JDK 17 o actualizar JaCoCo a una versión compatible con JDK 25.

## Resumen de métricas (local)

- Checkstyle errores (after): 47
- PMD: sin alertas críticas locales
- JaCoCo: cobertura presente pero con instrumentos poco fiables bajo JDK 25

## Artefactos producidos

- `target/site/` — informes locales (abre `target/site/index.html`).
- `site-comparison/after.zip` — snapshot del sitio final generado.

## Issues relacionadas (resumen)

- ISSUE-1: Fix test / package name mismatches — (resuelto localmente)
- ISSUE-2: Complete remaining Checkstyle fixes — (pendiente)
- ISSUE-3: Re-enable SpotBugs / FindBugs analysis — (registrado)
- ISSUE-4: Obtain reliable coverage reports (JaCoCo/JDK) — (registrado)

## Cómo reproducir localmente
Desde la raíz del repositorio:

```powershell
Set-Location -LiteralPath 'C:\Users\javiy\Documents\[3] CURSO\PRIMER CUATRI\ISO II\REPOS\ISO2-2025-GrupoA2.2-Testing-Exe1'
mvn -B -DskipTests=false clean test
mvn -B clean site
# luego abrir target/site/index.html en el navegador
```

## Ubicación de informes

- Checkstyle: `target/site/checkstyle.html`
- PMD: `target/site/pmd.html`
- JaCoCo aggregate: `target/site/jacoco/index.html` (puede estar incompleto bajo JDK 25)
- Surefire report: `target/site/surefire-report.html`
- Raw surefire xml: `target/surefire-reports/TEST-iso2.exe1.AppTest.xml`

## Ficheros modificados en este mantenimiento

- `src/main/java/ISO2/Exe1/App.java` — ajustes de paquete/estilo
- `src/main/java/ISO2/Exe1/Domain/CustomDate.java` — ajustes de paquete/estilo
- `src/test/java/iso2/exe1/AppTest.java` — normalización del paquete de test
- `src/main/java/iso2/exe1/package-info.java` — añadido
- `src/main/java/iso2/exe1/domain/package-info.java` — añadido
- `scripts/gh-issues-exe1.ps1` — añadido
- `docs/Maintenance-Exe1.md` — este informe
- `site-comparison/after.zip` — snapshot del sitio final

## Próximos pasos recomendados

1. Corregir los 47 errores de Checkstyle (priorizar Javadoc, `LineLength`, eliminar trailing spaces y resolver parámetros ocultos). Tiempo estimado: pequeño.
2. Decidir la estrategia de JaCoCo/JDK: ejecutar CI con JDK 17 (recomendado) o actualizar JaCoCo.
3. Rehabilitar SpotBugs tras actualización de toolchain.
4. Finalizar release: pushear commits (si no están), empujar tag y crear la release en GitHub.

## Publicar release (comandos)
Ejecuta desde la raíz del repo cuando quieras publicar la release (requiere `gh` autenticado):

```powershell
git add docs scripts site-comparison
git commit -m "chore(maintenance): add Maintenance-Exe1 docs, scripts and site comparison" || Write-Host "No hay cambios para commitear"
git push origin HEAD
git tag -a v0.0.1 -m "Release v0.0.1"
git push origin v0.0.1
gh release create v0.0.1 --title "v0.0.1" --notes-file docs/Maintenance-Exe1.md --target main
```

---

*Generado: 2025-12-17*
