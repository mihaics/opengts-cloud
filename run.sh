#!/bin/bash
#create config from jinja templates

cd $GTS_HOME; ant all
cp $GTS_HOME/build/*.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/catalina.sh run

