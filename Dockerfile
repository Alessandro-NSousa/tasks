# Imagem base com Java 17 (ajuste se usar outra versão)
FROM eclipse-temurin:17-jdk-alpine

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia o jar gerado pelo Maven
COPY target/*.jar app.jar

# Expondo a porta da aplicação
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
