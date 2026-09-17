# Crisp

客户端辅助 Forge mod：修原版/其他 mod 不合理手感（如 break delay、jump delay、TACZ 后坐力）。不是内容模组，不要加方块/物品/创造页。

## 栈

- MC 1.20.1 / Forge 47.4.23 / Java 17
- mappings：`official` 1.20.1（Mojmap，禁止 Yarn/Parchment 名）
- MixinGradle 0.7-SNAPSHOT + `org.spongepowered:mixin:0.8.5:processor`
- Gradle 8.8（`gradle/wrapper/gradle-wrapper.properties` 几乎只有 `distributionUrl`；仓库没有 `gradlew` / wrapper jar）

## 入口

- `@Mod`：`me.jeyor.crisp.Crisp`，modid `crisp`（必须与 `gradle.properties` 的 `mod_id` 一致）
- Mixin 包：`me.jeyor.crisp.mixin`；原版：`crisp.mixins.json`；TACZ：`crisp.tacz.mixins.json`（`required: false` + `TaczMixinPlugin`）
- 入口：`Crisp.java`；配置：`ClientConfig.java`（`ModConfig.Type.CLIENT`）

## 客户端约束

- 手感补丁放 mixin `"client"`，不要塞进 common `"mixins"`（专用服不该跑这些）
- 做成客户端-only 时：`Dist.CLIENT` + mods.toml `displayTest = "IGNORE_SERVER_VERSION"`
- 配置用 `ModConfig.Type.CLIENT`，不要用示例的 COMMON
- TACZ 对齐 **1.1.8-hotfix**（Curse `8141310-sources-8141355`）。optional：`compileOnly` + `runtimeOnly`，`mods.toml` `mandatory = false` / `[1.1.8-hotfix,)`。无 TACZ 时 plugin 不 apply tacz mixin
- 1.1.8：SEMI 走 `ShootKey.autoShoot`（已无 `semiShoot`）

## Mixin / 构建坑

- `overwrites.requireAnnotations: true`：`@Overwrite` 必须带注解
- mixins.json `compatibilityLevel` 仍是模板 `JAVA_8`，不要顺手改成 JAVA_17 除非确认 MixinGradle 接受
- `processResources` 只 expand `META-INF/mods.toml` 和 `pack.mcmeta`
- `jar` `finalizedBy reobfJar`；发布物看 reobf 后的 jar
- `copyIdeResources = true`；运行目录 `run/`（gitignore）
- Java 源文件 UTF-8

## 命令

没有 CI、测试、lint 脚本。有完整 wrapper 之后：

- `gradlew.bat build`
- `gradlew.bat runClient`

没有 wrapper 时用本机 Gradle 8.8 跑同样任务。不要猜 `npm`/`./gradlew test`。
