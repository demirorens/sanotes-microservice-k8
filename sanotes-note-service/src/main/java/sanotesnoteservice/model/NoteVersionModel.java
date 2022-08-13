package sanotesnoteservice.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import sanotesnoteservice.model.audit.UserAudit;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "notes_version",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "note_id"})})
@JsonIdentityInfo(scope = NoteVersionModel.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class NoteVersionModel extends UserAudit {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "note_container_id", nullable = false)
    @NaturalId
    private UUID noteContainerId;

    @Column(name = "note_id", nullable = false)
    @NaturalId
    private String noteId;

    @Transient
    private String topic;
    @Transient
    private String text;

    @Column(name = "note_book_id", nullable = false)
    private UUID noteBookId;
}
