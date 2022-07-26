# PowerToys
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://github.com/Labhatorian/PowerToys/blob/main/LICENSE)


> Java application with tools to help with specific tasks

Currently in Alpha and in a private repository. Heading for milestone **1.0**!

## Features to be created
- [ ] #1 File sorter
- [ ] #2 Random file chooser *(Currently worked on)*
- [ ] #3 File unsorter

## How it works
### #1 File sorter
Based on https://github.com/Labhatorian/file-sorter-python <br>
Use a library to compare filename to foldername. Allow for ignored characters and words. Save the new paths of the files to be used by the file unsorter.
<br><br>An idea is to do it based on tags from sites i.e. use [AniList](https://anilist.co/) to find similiar tags or even the series it belongs to.
This had to be a dynamic feature so you can easily add new sites. 

### #2 Random file chooser
Crawl directories, add them to a list and then have a button to randomise. Give an option to keep track of the opened program to
open the next randomised file after opening. <br>
Allow for ignored files and directories. Make the user be able to save the lists for later use.
Mainly so the user does not have to crawl every time if nothing has changed, though Java seems to optimise by caching the crawled files on its own.

### #3 File unsorter
Use a `json` or just a `txt` file to put everything back that has been done by the file sorter. 
<br> Nuclear option available<br><br>
Might get merged with **#1 File sorter**
### #4 File renamer *(Being considered)*
Using the extensions from the file sorter. Make it rename the files to have the tags, artist, season, episode etc.
<br> Great for data hoarders to easily rename and then sort files based on existing data on the web.