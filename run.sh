#!/bin/bash
#create config from jinja templates
# environment COMPILE_DEBUG=false, COMPILE_OPTIMIZE=true
j2  $GTS_HOME/build.properties.j2 > $GTS_HOME/build.properties
j2  $GTS_HOME/config.conf.j2 > $GTS_HOME/config.conf



cd $GTS_HOME; ant all
cp $GTS_HOME/build/*.war $CATALINA_HOME/webapps/
#$CATALINA_HOME/bin/catalina.sh run
/bin/bash

