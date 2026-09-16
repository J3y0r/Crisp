# J3ToolBox

[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-62b47a?style=flat-square)](https://www.minecraft.net/)
[![Forge](https://img.shields.io/badge/Forge-47.4.23-c7543a?style=flat-square)](https://files.minecraftforge.net/)
[![Java](https://img.shields.io/badge/Java-17-e76f00?style=flat-square)](https://adoptium.net/)
[![License](https://img.shields.io/badge/License-MIT-2d6cdf?style=flat-square)](LICENSE)

## 功能

### 原版

- **移除破坏延迟**：方块挖完后不再空等 5 tick，可以立刻开始下一次挖掘。
- **移除跳跃延迟**：落地后不再空等 10 tick，可以连续起跳。

### TACZ（可选）

未安装 [TACZ](https://www.curseforge.com/minecraft/mc-mods/timeless-and-classics-zero) 时，以下选项不会出现。需要 *
*1.1.8-hotfix** 及以上。

- **取消后坐力**：关闭镜头后坐力。
- **空仓自动换弹**：弹匣打空后自动换弹；背包供弹的枪会跳过。
- **取消射击间隔 / 拉栓等待**：去掉客户端间隔。专用服仍会执行自己的冷却。
- **半自动改全自动**：按住射击键时，半自动按全自动处理。

## 安装

1. Minecraft **1.20.1** + Forge **47.4.23+**
2. 把 jar 放进 `.minecraft/mods`
3. 启动后在模组列表打开 J3ToolBox 配置页

纯客户端模组，服务器不用装。

## 配置

游戏内：`模组` → `J3ToolBox` → 配置。

也可以改客户端配置文件：

```
.minecraft/config/j3toolbox-client.toml
```

| 分组        | 键                  | 默认     | 作用               |
|-----------|--------------------|--------|------------------|
| `vanilla` | `removeBreakDelay` | `true` | 移除破坏延迟           |
| `vanilla` | `removeJumpDelay`  | `true` | 移除跳跃延迟           |
| `tacz`    | `removeRecoil`     | `true` | 取消镜头后坐力          |
| `tacz`    | `autoReload`       | `true` | 空仓自动换弹           |
| `tacz`    | `removeShootDelay` | `true` | 取消客户端射击间隔 / 拉栓等待 |
| `tacz`    | `semiAsAuto`       | `true` | 半自动按住连发          |

## 构建

Java 17，Gradle 8.8。

```bat
gradlew.bat build
gradlew.bat runClient
```

## 协议

[MIT License](LICENSE) © 2026 Jeyor
