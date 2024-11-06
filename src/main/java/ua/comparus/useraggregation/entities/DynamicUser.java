package ua.comparus.useraggregation.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor
public class DynamicUser {
    @Id
    private String id;
    private String username;
    private String name;
    private String surname;
}
