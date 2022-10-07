# Skins

> This project will keep `SNAPSHOT`, you can fork and release by yourself.

## Version

[![](https://jitpack.io/v/Zhupff/Skins.svg)](https://jitpack.io/#Zhupff/Skins)

## Download

Add `jitpack` maven and classpath.

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    classpath 'com.github.Zhupff.Skins:plugin:$version'
}
```

Apply `plugin` for application-module and skin-module.

```groovy
// For application module.
apply plugin: zhupff.skins.SkinPlugin
// For skin module.
apply plugin: zhupff.skins.SkinPackagePlugin
```

Then add dependencies to module build.gradle.

```groovy
dependencies {
    implementation 'com.github.Zhupff.Skins:core:$version'
}
```

## Usage(TODO)

See the usage in **sample**.

## LICENSE

```markdown
Copyright 2022 Zhupff

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```