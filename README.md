# Resource Pack Converter

This is our 1.8-1.12 -> 1.13/1.14/1.15 Resource pack converter. We used this to do most of the grunt work to convert Hypixel's various resource packs for the 1.13 update.

We know that many use resource packs in nonstandard and quirky ways - but giving this a shot *may* reduce quite a bit of your pain and workload for the 1.13 conversion.

Please note again that while this code worked for the packs we use (ranging from 1.8 to 1.12 versions), it might not convert everything perfectly;  You may need to fix some things by hand, some items may have been mapped wrong, some things are no longer possible in the newer version, etc. So please be sure to throughly test the results!

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
