RecommendizR
===============

RecommendizR is a simple recommendation website.

See an example here : http://www.recommendizr.fr

Installation
----------------

* Install Play! Framework 1.2 http://www.playframework.com
* Install Redis 2.x.x
* Install MySQl or another SQL Databases Server
* clone this repository
* configure the app in `conf/application.conf` to setup your redis server and your SQL server.
- run `play dependencies` to update dependencies
- run `play start` on application root directory and that's all :)


API
--------
This application support Cross Origin Resources Sharing (CORS), 
so you can use it in you own website with ajax call.
But be carefull, some browser, like IE doesn't support CORS.

