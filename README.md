# opengts-cloud

 docker run --name opengts-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql
 docker run -it --name opengts-app --link opengts-mysql:mysql -e ETCD_SRV_ADDR=x.x.x.x -e OPENGTS_CLIENT_ID=clientid   -d mcsaky/opengts-cloud