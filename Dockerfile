FROM maven:3.9.15-eclipse-temurin-25-alpine
ARG BUILDSRC=/buildsrc
COPY ./ ${BUILDSRC}
WORKDIR ${BUILDSRC}
RUN mvn clean package

FROM eclipse-temurin:22.0.2_9-jdk-alpine
ARG DEPENDENCY=/buildsrc/target/dependency
COPY --from=0 ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=0 ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=0 ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 80
EXPOSE 443
ENTRYPOINT ["java","-Dspring.config.name=mindecrire,application,production,user-authorization","-cp","app:app/lib/*","com.zackrbrown.site.Application"]
