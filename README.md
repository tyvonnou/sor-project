# Sor project

Master's 1 project on shared objects to records pictures in MariaDB.

There are two parts projects:

* RMI (Database connection)
* Tomcat (API and static files hosting)

## RMI

### Dependencies

* mysql-connector-java (jar)

### Edit

In eclipse, create a java project remove `src` directory and create symbolic link between `rmi-src` of this repository and `src` on your java project.

### Config

You can create your own configuration for RMI (create `rmi-src/config/config.properties`) with theses properties :

* `port` port to use to share objects (by default `10000`)
* `service` name of RMI service (by default `sor-project`)
* `database.host` Database host (by default `localhost`)
* `database.port` Database port (by default `3306`)
* `database.user` Database user (by default `ubo`)
* `database.password` Database password (by default `ubo`)
* `bufferSize` Buffer size for picture part (by default `1000`)

## Tomcat

### Edit

In eclipse, create a java project remove `src` directory and create symbolic link between `tomcat-src` of this repository and `src` on your java project.
You have to download tomcat too and import it in eclipse.

## How to create symbolic link

* On Windows (`cmd.exe`): `mklink /D <link> <directory>`
* On GNU/Linux: `ln -s <directory> <link>`

## Authors

* [Théo Yvonnou](https://github.com/tyvonnou)
* [Loïc Penaud](https://github.com/lpenaud)
