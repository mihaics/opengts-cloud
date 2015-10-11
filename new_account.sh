#!/bin/bash
etcdctl set /opengts/$1/MYSQL_DBNAME $1
etcdctl set /opengts/$1/MYSQL_DBUSER $1
etcdctl set /opengts/$1/MYSQL_DBPASSWORD $( < /dev/urandom tr -dc _A-Z-a-z-0-9 | head -c${2:-10};echo;)



