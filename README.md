<img src="https://i.loli.net/2019/02/27/5c760f8570e8e.png">

## Mohist Made in China

[![](https://ci.codemc.org/buildStatus/icon?job=PFCraft%2FMohist)](https://ci.codemc.org/job/PFCraft/job/Mohist/)
![](https://img.shields.io/github/stars/PFCraft/Mohist.svg?label=Stars)
![](https://img.shields.io/github/license/PFCraft/Mohist.svg)

Since CraftBukkit has modified a lot of underlying layers, some of the core mods are not available, we are working hard to improve.

### Getting Help
   [**Discord**](https://discord.gg/HNmmrCV)
   [**Home**](https://www.mohist.red/)
   [**QQ**](https://jq.qq.com/?_wv=1027&k=5q7lcCb)
   [**bStats**](https://bstats.org/plugin/bukkit/Mohist)

### Info
* Java 8u212 JDK or higher
* Forge-1.12.2 - 14.23.5.2838
* Paper-1.12.2

### Download
* [**jenkins**](https://ci.codemc.org/job/PFCraft/job/Mohist/)

### Building
* Checkout project
  * You can use IDE or clone from console:
  `git clone https://github.com/PFCraft/Mohist.git`
* Setup
  * Setting up submodules:
  `git submodule update --init --recursive`
* Building
  * Build the project for Linux:
  `./gradlew launch4j`
  * or for Windows:
  `./gradlew.bat launch4j `

All builds will be in `.\Mohist\build\distributions\`

If you are a plugin developer, you can add the following files to your library file `.\Mohist\build\localCache\Mohist\recompiled.jar`

Mohist-xxxxx-universal.jar - is the server we should run it

### Installation
* Read the wiki in detail [**WIKI**](https://github.com/PFCraft/Mohist/wiki/Install-Mohist)

## How to install Mohsit: For those wishing to work on Mohsit itself

If you wish to actually inspect Mohsit, submit PRs or otherwise work
 with Mohsit itself, you're in the right place!
 
 [See the guide to setting up a Forge workspace](http://mcforge.readthedocs.io/en/latest/forgedev/).

## Pull requests

[See the "Making Changes and Pull Requests" section in the Forge documentation](http://mcforge.readthedocs.io/en/latest/forgedev/#making-changes-and-pull-requests).


### Credits
* [**Bukkit**](https://hub.spigotmc.org/stash/scm/spigot/bukkit.git) - plugin support.
* [**Paper**](https://github.com/PaperMC/Paper.git) - performance optimizations.
* [**CraftBukkit**](https://hub.spigotmc.org/stash/scm/spigot/craftbukkit.git) - plugin support.
* [**Spigot**](https://hub.spigotmc.org/stash/scm/spigot/spigot.git) - plugin support.
* [**MinecraftForge**](https://github.com/MinecraftForge/MinecraftForge.git) - mod support.
* [**Atom**](https://gitlab.com/divinecode/atom/Atom.git) - Partial code source.
* [**Thermos**](https://github.com/CyberdyneCC/Thermos.git) - Partial code source.
* [**um_bukkit**](https://github.com/TechCatOther/um_bukkit.git) - Partial code source.
