#!/bin/bash
etcdctl set /opengts/cartrack/MYSQL_DBNAME dbcartrack
etcdctl set /opengts/cartrack/MYSQL_DBUSER usrcartrack
etcdctl set /opengts/cartrack/MYSQL_DBPASSWORD $( < /dev/urandom tr -dc _A-Z-a-z-0-9 | head -c${2:-10};echo;)
etcdctl set /opengts/cartrack/SYSADMIN_PASSWORD $( < /dev/urandom tr -dc _A-Z-a-z-0-9 | head -c${2:-10};echo;)
etcdctl set /opengts/cartrack/CREATE_DATABASE true

kubectl create -f opengts-rc-cartrack.yml

