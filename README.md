# Meclipse #

Meclipse is an [Eclipse][eclipse] plugin for [MongoDB][mongodb]

*Author*: [Flavio Percoco Premoli](https://github.com/FlaPer87)

*Contributors*: [Joey Mink](https://github.com/walknwind)

## How to install? ##

Use the update site: `http://update.exoanalytic.com/org.mongodb.meclipse/`

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

## Credits ##

 * We use a subset of the beautiful [Silk Icons](http://www.famfamfam.com/lab/icons/silk/) from famfamfam:
 * [ExoAnalytic Solutions][exo] for hosting the [update site][update]


[eclipse]: http://eclipse.org "Eclipse"
[mongodb]: http://mongodb.org "MongoDB"
[exo]: http://exoanalytic.com "ExoAnalytic Solutions"
[update]: http://update.exoanalytic.com/org.mongodb.meclipse/ "Meclipse Update Site"