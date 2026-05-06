# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy POMs first so the full reactor is resolvable before any source is present.
# This also gives better layer caching: source changes don't bust dependency downloads.
COPY pom.xml .
COPY core/pom.xml core/pom.xml
COPY web-ui/pom.xml web-ui/pom.xml

# Resolve all dependencies (both modules) into the local Maven cache
RUN mvn dependency:go-offline -DskipTests -q || true

# Build and install the root (parent) POM so sub-module parent references resolve
RUN mvn install -N -DskipTests -q

# Build and install core (web-ui depends on it at compile time)
COPY core/src core/src
RUN mvn install -pl core -DskipTests -q

# Build web-ui with Vaadin production frontend bundling.
# The vaadin-maven-plugin downloads Node/npm on first run.
COPY web-ui/src web-ui/src
RUN mvn package -pl web-ui -Pproduction -DskipTests -q

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Create a non-root user and the data directory for the H2 file-based database
RUN groupadd --system cln && useradd --system --gid cln cln \
    && mkdir -p /app/data && chown cln:cln /app/data
USER cln

COPY --from=build /app/web-ui/target/*.jar app.jar

EXPOSE 8080
VOLUME ["/app/data"]

ENTRYPOINT ["java", "-jar", "app.jar"]
