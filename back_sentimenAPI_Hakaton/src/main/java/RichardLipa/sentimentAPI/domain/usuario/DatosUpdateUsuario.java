package RichardLipa.sentimentAPI.domain.usuario;

public record DatosUpdateUsuario(

) {
}
/*

//anotaciones de @Valid

public record DatosRegistroMedico(
        @NotBlank String nombre, // @NotBlank indica que este campo no puede ser null ni un string vacion
        @NotBlank @Email String email, // @Email que es un email
        @NotNull Especialidad especialidad,// ya que es un Enum solo validamos que no sea nulo
        @NotBlank  @Pattern(regexp = "\\d{9}")  String telefono,
        @NotBlank @Pattern(regexp = "\\d{8}")  String documento, // @Paterrn no permite agregar un expresion regular en este caso que dian 8 digitos
        @NotNull @Valid DatosDireccion direccion // le colocamos @Valid para solicitar validacion de sus campos ya que es un Clase
) {

}
 */