# Usamos la imagen oficial de Java 17 (versión ligera)
FROM eclipse-temurin:17-jdk-alpine

# Etiqueta de metadata
LABEL maintainer="juank@example.com"
LABEL description="Product Catalog Microservice"

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR compilado
# Asumimos que primero se corre `mvn clean package` localmente.
COPY target/*.jar app.jar

# Exponemos el puerto definido en application.properties
EXPOSE 9090

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
