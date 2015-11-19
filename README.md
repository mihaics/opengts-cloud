# opengts-cloud

 docker run --name opengts-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql

 docker run -it --name opengts-app --link opengts-mysql:mysql -e ETCD_SRV_ADDR=x.x.x.x -e OPENGTS_CLIENT_ID=clientid   -d mcsaky/opengts-cloud

apiVersion: v1

kind: ReplicationController

metadata:

  name: app-cartrack

  labels:

    name: app-cartrack

spec:

  replicas: 2

  selector:

    app: app-cartrack

  template:

    metadata:

     labels:

       app: app-cartrack

    spec:

     containers:

       - image: mcsaky/opengts-cloud

         name: app-cartrack

         env:

          - name: ETCD_SRV_ADDR

            value: 192.168.122.10

          - name: OPENGTS_CLIENT_ID

            value: cartrack

         ports:

          - containerPort: 8080

            name: http-cartrack

          - containerPort: 8009

            name: ajp-cartrack

--------------------------

apiVersion: v1

kind: Service

metadata:

  name: svc-cartrack

  labels:

    name: svc-cartrack

spec:

  ports:

  - port: 8080

    targetPort: 8080

    protocol: TCP

    name: http-cartrack

  - port: 8009

    targetPort: 8009

    protocol: TCP

    name: ajp-cartrack

  selector:

    app: app-cartrack

  type: NodePort

