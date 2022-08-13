package sanotesnoteservice.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import sanotesnoteservice.model.audit.UserAudit;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "notes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"note_id"})})
@JsonIdentityInfo(scope = NoteContainerModel.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class NoteContainerModel extends UserAudit {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "note_id", nullable = false)
    @NaturalId(mutable = true)
    private String noteId;

    @Transient
    private String topic;

    @Transient
    private String text;


    @Column(name = "note_book_id", nullable = false)
    private UUID noteBookId;


    @ElementCollection
    @CollectionTable(name="note_tags", joinColumns=@JoinColumn(name="id"))
    @Column(name="tag")
    public List<UUID> tags;

}
