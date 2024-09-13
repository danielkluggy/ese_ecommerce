# Usar a imagem Maven para buildar o projeto
FROM maven:3.8.5-openjdk-17-slim AS build

# Diretório de trabalho dentro do container
WORKDIR /app

# Copiar o arquivo pom.xml e baixar as dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar o código fonte
COPY src /app/src

# Build do projeto
RUN mvn clean install

# Usar uma imagem OpenJDK leve para rodar o projeto buildado
FROM openjdk:17-alpine

# Diretório de trabalho no container
WORKDIR /app

# Copiar o arquivo jar gerado
COPY --from=build /app/target/*.jar /app/app.jar

# Expor a porta padrão do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]