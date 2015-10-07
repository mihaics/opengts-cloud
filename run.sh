#!/bin/bash
#create config from jinja templates
j2  /usr/local/opengts-cloud-master/config/build.properties.j2 /usr/local/opengts-cloud-master/config/build.properties.ini > $GTS_HOME/build.properties


cd $GTS_HOME; ant all
cp $GTS_HOME/build/*.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/catalina.sh run

