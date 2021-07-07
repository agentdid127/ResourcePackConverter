# Resource Pack Converter
[![](https://jitpack.io/v/agentdid127/ResourcePackConverter.svg)](https://jitpack.io/#agentdid127/ResourcePackConverter)


This is my fork of Hypixel's Resource Pack Converter. It updates to any newer version from 1.8.x - 1.17

We know that many use resource packs in nonstandard and quirky ways - but giving this a shot *may* reduce quite a bit of your pain and workload for the 1.13 conversion.

This should convert most things, but if it doesn't please let me know what didn't work so I can fix it.

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



# To be Added
Of course there will always be some discrepencies and issues when converting that developers won't always notice, so if you find one, feel free to fix it and send a PR or write an issue here on Github.

Known issues:
 - Particles aren't converting very nicely with larger resource packs.

Things that we want to add/need to be added:
 - Converting to older versions.

# Not On the list to add
 - Horse and Zombified Piglin Conversion (the models were changed, which may make that much more difficult to convert)
