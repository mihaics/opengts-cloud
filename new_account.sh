#!/bin/bash
etcdctl set /opengts/$1/MYSQL_DBNAME $1
etcdctl set /opengts/$1/MYSQL_DBUSER $1
etcdctl set /opengts/$1/MYSQL_DBPASSWORD $( < /dev/urandom tr -dc _A-Z-a-z-0-9 | head -c${2:-10};echo;)
etcdctl set /opengts/$1/SYSADMIN_PASSWORD $( < /dev/urandom tr -dc _A-Z-a-z-0-9 | head -c${2:-10};echo;)
etcdctl set /opengts/$1/CREATE_DATABASE true



cp opengts-app.yml.j2 opengts-app.template
sed -i "s/demo/$1/g" opengts-app-$1.yml
kubectl create -f opengts-app-$1.yml
