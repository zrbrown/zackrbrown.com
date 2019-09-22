FROM openjdk:12-alpine
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
COPY keystore.p12 /etc
EXPOSE 80
EXPOSE 443
ENTRYPOINT ["java","-Dspring.config.name=mindy,application,production,user-authorization","-cp","app:app/lib/*","com.zackrbrown.site.Application"]
