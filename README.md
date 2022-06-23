# PowerToys
<sub>Java application with tools to help with specific tasks</sub>
Currently in Pre-Alpha Private. Heading to milestone **1.0**!

## Features to be created
- [] [#1](https://github.com/Labhatorian/PowerToys) File sorter based on filename
- [] [#2](https://github.com/Labhatorian/PowerToys) Random file chooser that is flexible to use
- [] [#3](https://github.com/Labhatorian/PowerToys) File unsorter

## Ideas
### General
An idea to help with optimalisation is to have the interface be in Java and run the features from Python scripts.
First we'd have to check if that is possible and what upsides and downsides there are doing it this way.

### 1
Based on https://github.com/Labhatorian/file-sorter-python
Use a library to compare filename to foldername. Allow for ignored characters and words.
Another idea is to do it based on tags from sites i.e. use [AniList](https://anilist.co/) to find similiar tags or even the series it belongs to

### 2
Based on an unpublished Python script in hands of @Labhatorian
Crawl directories with chosen roots. Then create a list to be cached and choose a random file to open. Allow for ignored characters, words and files.
Possible idea is to keep track of opened (supported) programs to continue choosing another file.

### 3
Use a `json` or just a `txt` file to put everything back that has been done by the file sorter.
Possible allow a nuclear option.