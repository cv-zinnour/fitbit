package ca.uqtr.fitbit.entity.view;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Immutable
@Table(name = "steps_view")
public class Steps implements Serializable {


    static final long serialVersionUID = 1L;

    @Id
    @Column(name = "row_num")
    private int id;

    @Column(name = "medical_file_id", nullable = false)
    private String medicalFileId;
    @Column(name = "date", nullable = false)
    private Timestamp date;
    @Column(name = "steps", nullable = false)
    private Double steps;

}
