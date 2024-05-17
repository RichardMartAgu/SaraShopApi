package es.svalero.SaraShopApi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 25, message = "El nombre no puede tener más de 25 caracteres")
    @NotNull(message = "El nombre es obligatorio")
    @Column
    private String name;
    @NotNull
    @Column
    private String description;
    @Column
    @NotNull
    private float price;
    @Column
    @NotNull
    private boolean stock;
    @Column
    private LocalDate creation_date;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

}

