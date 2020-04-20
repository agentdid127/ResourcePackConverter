# Resource Pack Converter

This is my fork of Hypixel's Resource Pack Converter. It updates to any newer version from 1.9 - 1.15.2

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
	
To update to a newer version than 1.13, you can use these parameters.
`--from <version>` and `--to <version>`

	java -jar ResourcePackConverter.jar --from 1.12 --to 1.15

We hope this helps out!
