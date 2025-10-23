# VariableHeightBanner (Jetpack Compose)

一个在横向滑动时容器高度平滑过渡的 Banner 示例，类似淘宝首页横向滑动且每屏高度不一的效果（随滑动实时插值过渡）。

## 功能
- 使用 `HorizontalPager` 实现横向滑动
- 不同页定义不同高度
- 根据滑动偏移，容器高度在当前页和目标页之间平滑插值，形成过渡动画

## 运行
1. 用 Android Studio (Giraffe/Koala 以上版本) 打开本项目根目录。
2. 连接设备或启动模拟器。
3. 运行 `app` 模块。

## 关键代码
- `MainActivity.kt` 内：
  - `HorizontalPager` 承载 Banner 页
  - `rememberVariableHeight(...)` 根据 `PagerState.currentPageOffsetFraction` 计算插值高度

## 自定义
- 在 `BannerDemoScreen` 中修改 `items` 列表，可设置每页的标题、颜色和高度，例如：
```kotlin
BannerItem(title = "数码会场", color = Color(0xFF42A5F5), height = 250.dp)
```
- 如需更平滑/带弹性的动画，可将 `rememberVariableHeight` 中的插值值替换为 `Animatable` 或 `animateDpAsState`。
