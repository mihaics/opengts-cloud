#!/bin/bash
#create config from jinja templates
# environment COMPILE_DEBUG=false, COMPILE_OPTIMIZE=true
# get environment from etcd
# OPENGTS_CLIENT_ID and ETCD_SRV_ADDR should be initialized
export MYSQL_DBNAME=$(curl -L http://$ETCD_SRV_ADDR:2379/v2/keys/opengts/$OPENGTS_CLIENT_ID/MYSQL_DBNAME | jq '.node.value' | sed 's/\"//g'  )
export MYSQL_DBUSER=$(curl -L http://$ETCD_SRV_ADDR:2379/v2/keys/opengts/$OPENGTS_CLIENT_ID/MYSQL_DBUSER | jq '.node.value' | sed 's/\"//g'  )
export MYSQL_DBPASSWORD=$(curl -L http://$ETCD_SRV_ADDR:2379/v2/keys/opengts/$OPENGTS_CLIENT_ID/MYSQL_DBPASSWORD | jq '.node.value' | sed 's/\"//g'  )


j2  $GTS_HOME/build.properties.j2 > $GTS_HOME/build.properties
j2  $GTS_HOME/config.conf.j2 > $GTS_HOME/config.conf



cd $GTS_HOME; ant all
cp $GTS_HOME/build/*.war $CATALINA_HOME/webapps/
#$CATALINA_HOME/bin/catalina.sh run
/bin/bash

