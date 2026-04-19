FROM zookeeper:3.9

USER root

RUN mkdir -p /opt/jmx \
	&& apt-get update \
	&& apt-get install -y --no-install-recommends curl ca-certificates \
	&& rm -rf /var/lib/apt/lists/* \
	&& curl -fsSL -o /opt/jmx/jmx.jar \
		https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.20.0/jmx_prometheus_javaagent-0.20.0.jar

COPY jmx-config.yml /opt/jmx/jmx-config.yml

ENV JVMFLAGS="-Xmx512m -javaagent:/opt/jmx/jmx.jar=9414:/opt/jmx/jmx-config.yml"

EXPOSE 2181 2888 3888 9414

USER zookeeper
