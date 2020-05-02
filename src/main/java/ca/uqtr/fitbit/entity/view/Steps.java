package ca.uqtr.fitbit.entity.view;


import org.hibernate.annotations.Immutable;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Immutable
@Table(name = "steps_view")
public class Steps implements Serializable {


    static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "medical_file_id", nullable = false)
    private String medicalFileId;
    @Column(name = "date", nullable = false)
    private Timestamp date;
    @Column(name = "steps", nullable = false)
    private Double steps;

}
