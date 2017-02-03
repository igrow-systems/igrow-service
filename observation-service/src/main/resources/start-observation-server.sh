#!/bin/sh

if [ -z "${M2_REPO}" ]; then
    echo Please set M2_REPO to the path containing your maven repository.  Exiting.
    exit 1
fi

lockdir=/var/tmp/observation-service.lock

if mkdir "$lockdir"
then
echo >&2 "Successfully acquired lock"

# Remove lockdir when the script finishes, or when it receives a signal
trap 'rm -rf "$lockdir"' 0    # remove directory when script finishes

# Optionally create temporary files in this directory, because
# they will be removed automatically:
tmpfile=$lockdir/pid

echo $$ > $tmpfile

else
echo >&2 "Cannot acquire lock, giving up on $lockdir"
exit 0
fi


export CLASSPATH="./target/classes:$M2_REPO/net/java/dev/jets3t/jets3t/0.6.1/jets3t-0.6.1.jar:$M2_REPO/com/argusat/gjl/gjl-api-java/1.0-SNAPSHOT/gjl-api-java-1.0-SNAPSHOT.jar:$M2_REPO/commons-el/commons-el/1.0/commons-el-1.0.jar:$M2_REPO/org/apache/httpcomponents/httpcore/4.1.3/httpcore-4.1.3.jar:$M2_REPO/log4j/log4j/1.2.17/log4j-1.2.17.jar:$M2_REPO/com/google/protobuf/protobuf-java/2.5.0/protobuf-java-2.5.0.jar:$M2_REPO/commons-net/commons-net/1.4.1/commons-net-1.4.1.jar:$M2_REPO/org/codehaus/jackson/jackson-mapper-asl/1.8.8/jackson-mapper-asl-1.8.8.jar:$M2_REPO/org/postgis/postgis-jdbc/2.1.3/postgis-jdbc-2.1.3.jar:$M2_REPO/org/eclipse/jdt/core/3.1.1/core-3.1.1.jar:$M2_REPO/org/mockito/mockito-all/1.9.5/mockito-all-1.9.5.jar:$M2_REPO/org/apache/avro/avro/1.5.3/avro-1.5.3.jar:$M2_REPO/org/codehaus/jackson/jackson-jaxrs/1.8.8/jackson-jaxrs-1.8.8.jar:$M2_REPO/org/glassfish/grizzly/grizzly-framework/2.1.1/grizzly-framework-2.1.1-tests.jar:$M2_REPO/com/sun/xml/bind/jaxb-impl/2.2.3-1/jaxb-impl-2.2.3-1.jar:$M2_REPO/com/github/stephenc/high-scale-lib/high-scale-lib/1.1.1/high-scale-lib-1.1.1.jar:$M2_REPO/org/glassfish/grizzly/grizzly-framework/2.1.1/grizzly-framework-2.1.1.jar:$M2_REPO/org/mortbay/jetty/jsp-2.1/6.1.14/jsp-2.1-6.1.14.jar:$M2_REPO/org/apache/hbase/hbase/0.94.11/hbase-0.94.11.jar:$M2_REPO/org/postgresql/postgresql/9.2-1002-jdbc4/postgresql-9.2-1002-jdbc4.jar:$M2_REPO/org/mortbay/jetty/jetty-util/6.1.26/jetty-util-6.1.26.jar:$M2_REPO/org/slf4j/slf4j-log4j12/1.7.5/slf4j-log4j12-1.7.5.jar:$M2_REPO/org/glassfish/grizzly/grizzly-http-server/2.1.1/grizzly-http-server-2.1.1.jar:$M2_REPO/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar:$M2_REPO/com/yammer/metrics/metrics-core/2.1.2/metrics-core-2.1.2.jar:$M2_REPO/org/slf4j/slf4j-api/1.7.5/slf4j-api-1.7.5.jar:$M2_REPO/hsqldb/hsqldb/1.8.0.10/hsqldb-1.8.0.10.jar:$M2_REPO/xmlenc/xmlenc/0.52/xmlenc-0.52.jar:$M2_REPO/org/glassfish/gmbal/gmbal-api-only/3.0.0-b023/gmbal-api-only-3.0.0-b023.jar:$M2_REPO/org/apache/zookeeper/zookeeper/3.4.5/zookeeper-3.4.5.jar:$M2_REPO/org/glassfish/hk2/external/aopalliance-repackaged/2.3.0-b10/aopalliance-repackaged-2.3.0-b10.jar:$M2_REPO/org/jruby/jruby-complete/1.6.5/jruby-complete-1.6.5.jar:$M2_REPO/org/apache/httpcomponents/httpclient/4.1.2/httpclient-4.1.2.jar:$M2_REPO/org/apache/hadoop/hadoop-core/1.2.1/hadoop-core-1.2.1.jar:$M2_REPO/javax/xml/bind/jaxb-api/2.1/jaxb-api-2.1.jar:$M2_REPO/org/glassfish/grizzly/grizzly-rcm/2.1.1/grizzly-rcm-2.1.1.jar:$M2_REPO/org/apache/commons/commons-lang3/3.3.2/commons-lang3-3.3.2.jar:$M2_REPO/org/xerial/snappy/snappy-java/1.0.3.2/snappy-java-1.0.3.2.jar:$M2_REPO/org/glassfish/hk2/hk2-api/2.3.0-b10/hk2-api-2.3.0-b10.jar:$M2_REPO/org/jamon/jamon-runtime/2.3.1/jamon-runtime-2.3.1.jar:$M2_REPO/org/apache/thrift/libthrift/0.8.0/libthrift-0.8.0.jar:$M2_REPO/org/glassfish/grizzly/grizzly-http-servlet/2.1.1/grizzly-http-servlet-2.1.1.jar:$M2_REPO/org/apache/commons/commons-math/2.1/commons-math-2.1.jar:$M2_REPO/com/rabbitmq/amqp-client/3.3.4/amqp-client-3.3.4.jar:$M2_REPO/org/codehaus/jackson/jackson-xc/1.8.8/jackson-xc-1.8.8.jar:$M2_REPO/org/glassfish/hk2/hk2-utils/2.3.0-b10/hk2-utils-2.3.0-b10.jar:$M2_REPO/org/glassfish/external/management-api/3.0.0-b012/management-api-3.0.0-b012.jar:$M2_REPO/commons-configuration/commons-configuration/1.6/commons-configuration-1.6.jar:$M2_REPO/com/google/code/findbugs/jsr305/1.3.9/jsr305-1.3.9.jar:$M2_REPO/commons-digester/commons-digester/1.8/commons-digester-1.8.jar:$M2_REPO/org/apache/avro/avro-ipc/1.5.3/avro-ipc-1.5.3.jar:$M2_REPO/com/sun/jersey/jersey-core/1.8/jersey-core-1.8.jar:$M2_REPO/ant/ant/1.6.5/ant-1.6.5.jar:$M2_REPO/commons-beanutils/commons-beanutils/1.7.0/commons-beanutils-1.7.0.jar:$M2_REPO/commons-httpclient/commons-httpclient/3.0.1/commons-httpclient-3.0.1.jar:$M2_REPO/commons-collections/commons-collections/3.2.1/commons-collections-3.2.1.jar:$M2_REPO/org/jboss/netty/netty/3.2.4.Final/netty-3.2.4.Final.jar:$M2_REPO/javax/activation/activation/1.1/activation-1.1.jar:$M2_REPO/commons-io/commons-io/2.1/commons-io-2.1.jar:$M2_REPO/tomcat/jasper-runtime/5.5.12/jasper-runtime-5.5.12.jar:$M2_REPO/com/sun/jersey/jersey-grizzly2/1.8/jersey-grizzly2-1.8.jar:$M2_REPO/asm/asm/3.1/asm-3.1.jar:$M2_REPO/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:$M2_REPO/commons-codec/commons-codec/1.4/commons-codec-1.4.jar:$M2_REPO/org/glassfish/grizzly/grizzly-http/2.1.1/grizzly-http-2.1.1.jar:$M2_REPO/javax/inject/javax.inject/1/javax.inject-1.jar:$M2_REPO/org/mortbay/jetty/servlet-api/2.5-20081211/servlet-api-2.5-20081211.jar:$M2_REPO/commons-beanutils/commons-beanutils-core/1.8.0/commons-beanutils-core-1.8.0.jar:$M2_REPO/commons-lang/commons-lang/2.5/commons-lang-2.5.jar:$M2_REPO/tomcat/jasper-compiler/5.5.12/jasper-compiler-5.5.12.jar:$M2_REPO/org/codehaus/jettison/jettison/1.1/jettison-1.1.jar:$M2_REPO/com/sun/jersey/jersey-server/1.8/jersey-server-1.8.jar:$M2_REPO/org/mortbay/jetty/jetty/6.1.26/jetty-6.1.26.jar:$M2_REPO/org/mortbay/jetty/servlet-api-2.5/6.1.14/servlet-api-2.5-6.1.14.jar:$M2_REPO/com/google/guava/guava/11.0.2/guava-11.0.2.jar:$M2_REPO/com/sun/jersey/jersey-json/1.8/jersey-json-1.8.jar:$M2_REPO/oro/oro/2.0.8/oro-2.0.8.jar:$M2_REPO/org/glassfish/javax.servlet/3.1/javax.servlet-3.1.jar:$M2_REPO/org/codehaus/jackson/jackson-core-asl/1.8.8/jackson-core-asl-1.8.8.jar:$M2_REPO/org/apache/velocity/velocity/1.7/velocity-1.7.jar:$M2_REPO/org/mortbay/jetty/jsp-api-2.1/6.1.14/jsp-api-2.1-6.1.14.jar:$M2_REPO/stax/stax-api/1.0.1/stax-api-1.0.1.jar"

java -Djersey.test.port=9998 $@ com.igrow.observice.Main

