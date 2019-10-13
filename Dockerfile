FROM maven:3.6.2-jdk-12
ARG BUILDSRC=/buildsrc
COPY ./ ${BUILDSRC}
RUN mvn clean package && mvn jar:jar

FROM openjdk:12-alpine
ARG DEPENDENCY=${BUILDSRC}/target/dependency
COPY --from=0 ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=0 ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=0 ${DEPENDENCY}/BOOT-INF/classes /app
COPY keystore.p12 /etc
EXPOSE 80
EXPOSE 443
ENTRYPOINT ["java","-Dspring.config.name=mindy,application,production,user-authorization","-cp","app:app/lib/*","com.zackrbrown.site.Application"]
