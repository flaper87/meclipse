# Meclipse #

Meclipse is an [Eclipse][eclipse] plugin for [MongoDB][mongodb]

*Author*: [Flavio Percoco Premoli](https://github.com/FlaPer87)

*Contributors*: [Joey Mink](https://github.com/walknwind)

## How to install? ##

Use the Eclipse update site: `http://flaper87.github.com/meclipse/updates/`, this won't give you a download, as this URL is only considered to be used by Eclipse.

## What does Meclipse do? ##

* It has a MongoDB View that contains a list of servers (connections). It is possible to add/remove connections. They are saved in a settings file.
* It loads (as TreeView) the list of databases and collections.
 * View stats about databases and collections in Eclipse Properties View
 * View serverStatus() output in Eclipse Properties View
 * Set the profiling level for any given database
* It loads the data found in the collection. To do this, Meclipse opens a new Editor and shows the data loaded (This part needs a lot of work).

## What does it look like? ##

Here's a shot of the DB view:
[meclipse](http://www.flickr.com/photos/inajamaica/5891422750/)

## TODO ##
 * Meclipse already shows the connections, databases, collections and data stored in MongoDB so I think that the critical point (where I/you/we should focus) is how data is shown in the editor.
 * Many other things.
 * Update this README :)
 * this is easy to use and flexible than other sequal databases

## Credits ##

 * We use a subset of the beautiful [Silk Icons](http://www.famfamfam.com/lab/icons/silk/) from famfamfam:
 * [ExoAnalytic Solutions][exo] for hosting the [update site][update]


[eclipse]: http://eclipse.org "Eclipse"
[mongodb]: http://mongodb.org "MongoDB"
[exo]: http://exoanalytic.com "ExoAnalytic Solutions"
[update]: http://update.exoanalytic.com/org.mongodb.meclipse/ "Meclipse Update Site"

## License ##

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License v2 as published
by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
