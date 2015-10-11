#!/bin/bash

OPENGTS_CLIENT_ID = $1
etcdctl set /opengts/$OPENGTS_CLIENT_ID/MYSQL_DBNAME $OPENGTS_CLIENT_ID
etcdctl set /opengts/$OPENGTS_CLIENT_ID/MYSQL_DBUSER $OPENGTS_CLIENT_ID
etcdctl set /opengts/$OPENGTS_CLIENT_ID/MYSQL_DBPASSWORD $( < /dev/urandom tr -dc _A-Z-a-z-0-9 | head -c${1:-16};echo;) 


