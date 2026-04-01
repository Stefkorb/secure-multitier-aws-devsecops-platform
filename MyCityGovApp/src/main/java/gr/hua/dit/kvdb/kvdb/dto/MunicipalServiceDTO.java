package gr.hua.dit.kvdb.kvdb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MunicipalServiceDTO {
    private Long id;
    private String name;
    private String description;
    private int slaDays;
    private boolean active;
}
