package sanotesnoteservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Builder
@Document(collection = "note")
public class NoteModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String topic;
    private String text;

}
