package RichardLipa.sentimentAPI;

//import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class SentimentApiApplication {

	public static void main(String[] args) {

        SpringApplication.run(SentimentApiApplication.class, args);
	}



/*
	//!!IMPORTANTE  //CODIGO PARA REPARA FLYWAY_SHCEMA_HISTORY//CUANDO EXISTA UN VERSIONDE MIGRACION ATASCADA QUE NO SE EJECUTO POR COMPLETO
	// DEVIDO A ERRORES DE SINTASIS EN EL ARCHIVO DE MIGRACION SQL//elimina la version atascada si el campo succes esta en false en la tabla de flywy_schema_history
	// Este Bean se ejecutará cuando la aplicación arranque.
	// Lo marcamos con @Profile("!prod") para que no se ejecute en producción si tienes perfiles.
	// Si no usas perfiles, puedes quitar la anotación @Profile.
	@Bean
	@Profile("!prod") // Esto significa que se ejecutará en cualquier perfil EXCEPTO 'prod'
	public CommandLineRunner flywayRepairRunner(Flyway flyway) {
		return args -> {
			System.out.println("------------------------------------");
			System.out.println("  Ejecutando Flyway Repair...      ");
			System.out.println("  ¡IMPORTANTE! Eliminar este código");
			System.out.println("  después de que el repair se complete.");
			System.out.println("------------------------------------");

			flyway.repair(); // Ejecuta el comando repair

			System.out.println("Flyway Repair completado.");
			System.out.println("------------------------------------");
			// Opcional: System.exit(0); si quieres que la app se detenga después del repair
		};
	}
*/

}
