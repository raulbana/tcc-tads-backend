# Imagem base do Java para a aplicação
FROM openjdk:21-ea-17-slim-bullseye

WORKDIR /app

# instala ffmpeg (embutir a dependência no container)
RUN apt-get update \
    && apt-get install -y --no-install-recommends ffmpeg \
    && rm -rf /var/lib/apt/lists/*

# Copia os arquivos da pasta target para a imagem Docker
COPY target /app/target

# Obtém o nome do arquivo JAR mais recente na pasta target e copia para o diretório /app
RUN ARTIFACT_NAME=$(find /app/target -name "daily-iu-services-*.jar" | head -n 1 | xargs -n 1 basename) && \
    if [ -n "$ARTIFACT_NAME" ]; then \
        cp /app/target/$ARTIFACT_NAME /app/app.jar; \
    else \
        echo "No JAR file found in target directory"; \
        exit 1; \
    fi

ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar"]