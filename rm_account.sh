#!/bin/bash
kubectl delete po app-$1 
kubectl delete svc svc-$1 

etcdctl rm /opengts/$1/CREATE_DATABASE
etcdctl rm /opengts/$1/MYSQL_DBNAME 
etcdctl rm /opengts/$1/MYSQL_DBPASSWORD
etcdctl rm /opengts/$1/MYSQL_DBUSER
etcdctl rm /opengts/$1/SYSADMIN_PASSWORD
etcdctl rmdir /opengts/$1/

MYSQL_ROOT_PASSWORD=$(etcdctl get /opengts/database/mysql/MYSQL_ROOT_PASSWORD)

mysqladmin -f -uroot -p$MYSQL_ROOT_PASSWORD drop $1;


