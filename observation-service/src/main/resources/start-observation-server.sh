#!/bin/sh

export CLASSPATH="./target/classes:/home/jsr/.m2/repository/ant/ant/1.6.5/ant-1.6.5.jar:/home/jsr/.m2/repository/asm/asm/3.1/asm-3.1.jar:/home/jsr/.m2/repository/com/argusat/gjl/gjl-api-java/1.0-SNAPSHOT/gjl-api-java-1.0-SNAPSHOT.jar:/home/jsr/.m2/repository/com/github/stephenc/high-scale-lib/high-scale-lib/1.1.1/high-scale-lib-1.1.1.jar:/home/jsr/.m2/repository/com/google/code/findbugs/jsr305/1.3.9/jsr305-1.3.9.jar:/home/jsr/.m2/repository/com/google/guava/guava/11.0.2/guava-11.0.2.jar:/home/jsr/.m2/repository/com/google/protobuf/protobuf-java/2.5.0/protobuf-java-2.5.0.jar:/home/jsr/.m2/repository/com/rabbitmq/amqp-client/3.3.4/amqp-client-3.3.4.jar:/home/jsr/.m2/repository/com/sun/jersey/jersey-client/1.8/jersey-client-1.8.jar:/home/jsr/.m2/repository/com/sun/jersey/jersey-core/1.8/jersey-core-1.8.jar:/home/jsr/.m2/repository/com/sun/jersey/jersey-grizzly2/1.8/jersey-grizzly2-1.8.jar:/home/jsr/.m2/repository/com/sun/jersey/jersey-json/1.8/jersey-json-1.8.jar:/home/jsr/.m2/repository/com/sun/jersey/jersey-server/1.8/jersey-server-1.8.jar:/home/jsr/.m2/repository/com/sun/xml/bind/jaxb-impl/2.2.3-1/jaxb-impl-2.2.3-1.jar:/home/jsr/.m2/repository/com/yammer/metrics/metrics-core/2.1.2/metrics-core-2.1.2.jar:/home/jsr/.m2/repository/commons-beanutils/commons-beanutils/1.7.0/commons-beanutils-1.7.0.jar:/home/jsr/.m2/repository/commons-beanutils/commons-beanutils-core/1.8.0/commons-beanutils-core-1.8.0.jar:/home/jsr/.m2/repository/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:/home/jsr/.m2/repository/commons-codec/commons-codec/1.4/commons-codec-1.4.jar:/home/jsr/.m2/repository/commons-collections/commons-collections/3.2.1/commons-collections-3.2.1.jar:/home/jsr/.m2/repository/commons-configuration/commons-configuration/1.6/commons-configuration-1.6.jar:/home/jsr/.m2/repository/commons-digester/commons-digester/1.8/commons-digester-1.8.jar:/home/jsr/.m2/repository/commons-el/commons-el/1.0/commons-el-1.0.jar:/home/jsr/.m2/repository/commons-httpclient/commons-httpclient/3.0.1/commons-httpclient-3.0.1.jar:/home/jsr/.m2/repository/commons-io/commons-io/2.1/commons-io-2.1.jar:/home/jsr/.m2/repository/commons-lang/commons-lang/2.5/commons-lang-2.5.jar:/home/jsr/.m2/repository/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar:/home/jsr/.m2/repository/commons-net/commons-net/1.4.1/commons-net-1.4.1.jar:/home/jsr/.m2/repository/hsqldb/hsqldb/1.8.0.10/hsqldb-1.8.0.10.jar:/home/jsr/.m2/repository/javax/activation/activation/1.1/activation-1.1.jar:/home/jsr/.m2/repository/javax/xml/bind/jaxb-api/2.1/jaxb-api-2.1.jar:/home/jsr/.m2/repository/junit/junit/4.8.2/junit-4.8.2.jar:/home/jsr/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:/home/jsr/.m2/repository/net/java/dev/jets3t/jets3t/0.6.1/jets3t-0.6.1.jar:/home/jsr/.m2/repository/org/apache/avro/avro/1.5.3/avro-1.5.3.jar:/home/jsr/.m2/repository/org/apache/avro/avro-ipc/1.5.3/avro-ipc-1.5.3.jar:/home/jsr/.m2/repository/org/apache/commons/commons-math/2.1/commons-math-2.1.jar:/home/jsr/.m2/repository/org/apache/hadoop/hadoop-core/1.2.1/hadoop-core-1.2.1.jar:/home/jsr/.m2/repository/org/apache/hbase/hbase/0.94.11/hbase-0.94.11.jar:/home/jsr/.m2/repository/org/apache/httpcomponents/httpclient/4.1.2/httpclient-4.1.2.jar:/home/jsr/.m2/repository/org/apache/httpcomponents/httpcore/4.1.3/httpcore-4.1.3.jar:/home/jsr/.m2/repository/org/apache/thrift/libthrift/0.8.0/libthrift-0.8.0.jar:/home/jsr/.m2/repository/org/apache/velocity/velocity/1.7/velocity-1.7.jar:/home/jsr/.m2/repository/org/apache/zookeeper/zookeeper/3.4.5/zookeeper-3.4.5.jar:/home/jsr/.m2/repository/org/codehaus/jackson/jackson-core-asl/1.8.8/jackson-core-asl-1.8.8.jar:/home/jsr/.m2/repository/org/codehaus/jackson/jackson-jaxrs/1.8.8/jackson-jaxrs-1.8.8.jar:/home/jsr/.m2/repository/org/codehaus/jackson/jackson-mapper-asl/1.8.8/jackson-mapper-asl-1.8.8.jar:/home/jsr/.m2/repository/org/codehaus/jackson/jackson-xc/1.8.8/jackson-xc-1.8.8.jar:/home/jsr/.m2/repository/org/codehaus/jettison/jettison/1.1/jettison-1.1.jar:/home/jsr/.m2/repository/org/eclipse/jdt/core/3.1.1/core-3.1.1.jar:/home/jsr/.m2/repository/org/glassfish/javax.servlet/3.1/javax.servlet-3.1.jar:/home/jsr/.m2/repository/org/glassfish/external/management-api/3.0.0-b012/management-api-3.0.0-b012.jar:/home/jsr/.m2/repository/org/glassfish/gmbal/gmbal-api-only/3.0.0-b023/gmbal-api-only-3.0.0-b023.jar:/home/jsr/.m2/repository/org/glassfish/grizzly/grizzly-framework/2.1.1/grizzly-framework-2.1.1-tests.jar:/home/jsr/.m2/repository/org/glassfish/grizzly/grizzly-framework/2.1.1/grizzly-framework-2.1.1.jar:/home/jsr/.m2/repository/org/glassfish/grizzly/grizzly-http/2.1.1/grizzly-http-2.1.1.jar:/home/jsr/.m2/repository/org/glassfish/grizzly/grizzly-http-server/2.1.1/grizzly-http-server-2.1.1.jar:/home/jsr/.m2/repository/org/glassfish/grizzly/grizzly-http-servlet/2.1.1/grizzly-http-servlet-2.1.1.jar:/home/jsr/.m2/repository/org/glassfish/grizzly/grizzly-rcm/2.1.1/grizzly-rcm-2.1.1.jar:/home/jsr/.m2/repository/org/jamon/jamon-runtime/2.3.1/jamon-runtime-2.3.1.jar:/home/jsr/.m2/repository/org/jboss/netty/netty/3.2.4.Final/netty-3.2.4.Final.jar:/home/jsr/.m2/repository/org/jruby/jruby-complete/1.6.5/jruby-complete-1.6.5.jar:/home/jsr/.m2/repository/org/mockito/mockito-all/1.9.5/mockito-all-1.9.5.jar:/home/jsr/.m2/repository/org/mortbay/jetty/jetty/6.1.26/jetty-6.1.26.jar:/home/jsr/.m2/repository/org/mortbay/jetty/jetty-util/6.1.26/jetty-util-6.1.26.jar:/home/jsr/.m2/repository/org/mortbay/jetty/jsp-2.1/6.1.14/jsp-2.1-6.1.14.jar:/home/jsr/.m2/repository/org/mortbay/jetty/jsp-api-2.1/6.1.14/jsp-api-2.1-6.1.14.jar:/home/jsr/.m2/repository/org/mortbay/jetty/servlet-api/2.5-20081211/servlet-api-2.5-20081211.jar:/home/jsr/.m2/repository/org/mortbay/jetty/servlet-api-2.5/6.1.14/servlet-api-2.5-6.1.14.jar:/home/jsr/.m2/repository/org/postgis/postgis-jdbc/2.1.2/postgis-jdbc-2.1.2.jar:/home/jsr/.m2/repository/org/postgresql/postgresql/9.2-1002-jdbc4/postgresql-9.2-1002-jdbc4.jar:/home/jsr/.m2/repository/org/slf4j/slf4j-api/1.7.5/slf4j-api-1.7.5.jar:/home/jsr/.m2/repository/org/slf4j/slf4j-log4j12/1.7.5/slf4j-log4j12-1.7.5.jar:/home/jsr/.m2/repository/org/xerial/snappy/snappy-java/1.0.3.2/snappy-java-1.0.3.2.jar:/home/jsr/.m2/repository/oro/oro/2.0.8/oro-2.0.8.jar:/home/jsr/.m2/repository/stax/stax-api/1.0.1/stax-api-1.0.1.jar:/home/jsr/.m2/repository/tomcat/jasper-compiler/5.5.12/jasper-compiler-5.5.12.jar:/home/jsr/.m2/repository/tomcat/jasper-runtime/5.5.12/jasper-runtime-5.5.12.jar:/home/jsr/.m2/repository/xmlenc/xmlenc/0.52/xmlenc-0.52.jar"

java -Djersey.test.port=9998 com.argusat.gjl.observice.Main
