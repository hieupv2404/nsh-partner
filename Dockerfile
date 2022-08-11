# syntax = docker/dockerfile:1.3
FROM maven:3.8.4-jdk-11-slim@sha256:fc997dd4617b9fad64524a56e6807ea29c25f62cd0f06485611290f980480003 as jarBuilder

WORKDIR /src

ARG REGISTRY_SAOBANG_REPO_USER
ARG REGISTRY_SAOBANG_REPO_PASS

ENV REGISTRY_SAOBANG_REPO_USER=${REGISTRY_SAOBANG_REPO_USER}
ENV REGISTRY_SAOBANG_REPO_PASS=${REGISTRY_SAOBANG_REPO_PASS}

COPY . .

RUN --mount=type=cache,target=/root/.m2 mvn -T 1C -U -B -s .m2/settings.xml clean package -DskipTests

FROM adoptopenjdk:11.0.11_9-jre-openj9-0.26.0-focal@sha256:db3504a5a4c1572c0879027cf5124a5598318aaecefbb9971d80d9a3ba98b0a5 as builder

WORKDIR /application

ARG APM_JAVA_AGENT_VER=1.26.0
RUN curl -Lo apm.jar https://search.maven.org/remotecontent\?filepath\=co/elastic/apm/elastic-apm-agent/${APM_JAVA_AGENT_VER}/elastic-apm-agent-${APM_JAVA_AGENT_VER}.jar

COPY --from=jarBuilder /src/target/*.jar ./application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk:11.0.11_9-jre-openj9-0.26.0-focal@sha256:db3504a5a4c1572c0879027cf5124a5598318aaecefbb9971d80d9a3ba98b0a5

ARG DUMBINIT_VER=1.2.5
RUN curl -Lo /usr/local/bin/dumb-init https://github.com/Yelp/dumb-init/releases/download/v${DUMBINIT_VER}/dumb-init_${DUMBINIT_VER}_x86_64 && \
    chmod +x /usr/local/bin/dumb-init

ENV TZ=Asia/Ho_Chi_Minh

WORKDIR /application

COPY --from=builder /application/apm.jar ./apm.jar
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application/ ./

ENTRYPOINT ["dumb-init", "java", "-javaagent:apm.jar", "org.springframework.boot.loader.JarLauncher"]
