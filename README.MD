# Resource Pack Converter
[![](https://jitpack.io/v/agentdid127/ResourcePackConverter.svg)](https://jitpack.io/#agentdid127/ResourcePackConverter)

This is a program that converts most issues with resource packs between any version from 1.7.2 all the way to 1.21.4.

This should convert most things, but if you notice anything that does not work correctly, or should be added, create an issue on Github.

Also if any other developers would like to open any PRs with fixes and additions please feel free.

While this program will copy your resource packs before converting them, we still recommend backing them up, just in case!

## Usage
[Download the compiled jar file](https://github.com/agentdid127/ResourcePackConverter/releases/latest), or compile the source yourself.  
The program will look for any valid resource packs in the current directory and is easily run by doing this.
(Converts from 1.12.x to 1.13.x)

    java -jar ResourcePackConverter.jar

You can set the input directory using one of the following parameters.
`-i <path>`, `--input <path>` or `--input-dir <path>`.

    java -jar ResourcePackConverter.jar --input input/

To update to any other version than 1.12-1.13, you can use these parameters.
`--from <version>` and `--to <version>`

	java -jar ResourcePackConverter.jar --from 1.12 --to 1.16.2

We hope this helps out!

## Not On the list to add
- Horse, Zombified Piglin, and Bat Conversion (the models were changed, which may make that much more difficult to convert)

## In-Development Versions
Before a version of the converter is ready for release, it will be automatically built on a Jenkins server, where you can get non-production builds. Note: that there is no guaruntee that these versions are stable, however, feel free to post issue reports on the issues page.
[Jenkins Server](https://jenkins.dc1.agentdid127.com/job/agentdid127/job/ResourcePackConverter/job/master/)

### Upstream
This program was originally derived from Hypixel's Resource Pack Converter, though has been essentially reworked over the span of 5 years. Very few, if any bits of the original software remain, but in case that does exist, their original license is included in the licenses' directory.
