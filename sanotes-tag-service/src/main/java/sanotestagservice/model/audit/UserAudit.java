package sanotestagservice.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdBy", "lastModifiedBy"},
        allowGetters = true
)
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UserAudit extends DateAudit {
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(updatable = false)
    private UUID createdBy;

    @LastModifiedBy
    private UUID lastModifiedBy;
}
