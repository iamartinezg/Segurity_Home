package com.example.proyecto.modelos;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Mensaje {

    private String uuid;
    private String message;
    private boolean hasImage;
    private String imagePath;
    private String from;
    private long stamp;
}
