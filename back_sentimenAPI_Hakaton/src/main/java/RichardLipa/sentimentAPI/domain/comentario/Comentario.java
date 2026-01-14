package RichardLipa.sentimentAPI.domain.comentario;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;




@Table(name="comentarios")
@Entity(name="Comentario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of = "id")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comentario;
    @Enumerated(EnumType.STRING)
    private Tipo prevision;
    private Float provabilidad;
    private LocalDateTime fechaRegistro; // LocalDateTime.now();
    private Boolean state;

    public Comentario(@Valid DatosRegistroComentario datos) {
        this.id = null;
        this.comentario = datos.texto();
        this.prevision = datos.prevision();
        this.provabilidad= datos.provabilidad();
        this.fechaRegistro = LocalDateTime.now();
        this.state = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Tipo getPrevision() {
        return prevision;
    }

    public void setPrevision(Tipo prevision) {
        this.prevision = prevision;
    }

    public Float getProvabilidad() {
        return provabilidad;
    }

    public void setProvabilidad(Float provabilidad) {
        this.provabilidad = provabilidad;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}

