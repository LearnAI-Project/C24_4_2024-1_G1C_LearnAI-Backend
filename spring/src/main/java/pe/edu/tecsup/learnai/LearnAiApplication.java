package pe.edu.tecsup.learnai;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LearnAiApplication {

    public static void main(String[] args) {
        // Cargar el archivo .env
        Dotenv dotenv = Dotenv.load();
        // Acceder a las variables de entorno
        String dbHost = dotenv.get("DB_URL");  // URL de la base de datos
        String dbPort = dotenv.get("MAIL_USERNAME");  // Usuario de email
        String dbName = dotenv.get("MAIL_PASSWORD");  // Contraseña de email
        String dbUser = dotenv.get("GOOGLE_OAUTH_CLIENT_ID");  // ID de cliente de Google OAuth
        String dbPassword = dotenv.get("GOOGLE_OAUTH_CLIENT_SECRET");  // Secreto de cliente de Google OAuth
        String apiKey = dotenv.get("HOST_FRONTEND");  // URL del frontend

        // Imprimir las variables para verificar
        System.out.println("URL de base de datos: " + dbHost);
        System.out.println("Usuario de email: " + dbPort);
        System.out.println("Contraseña de email: " + dbName);
        System.out.println("ID de cliente Google OAuth: " + dbUser);
        System.out.println("Secreto cliente Google OAuth: " + dbPassword);
        System.out.println("URL del frontend: " + apiKey);

        SpringApplication.run(LearnAiApplication.class, args);
    }

}
