package ca.uqtr.fitbit.entity.view;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Immutable
@Table(name = "minutes_view")
public class Minutes implements Serializable {


    static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "medical_file_id")
    private String medicalFileId;
    @Column(name = "date")
    private Timestamp date;
    @Column(name = "sedentary")
    private int sedentary;
    @Column(name = "lightly_active")
    private int lightly_active;
    @Column(name = "fairly_active")
    private int fairly_active;
    @Column(name = "very_active")
    private int very_active;

}
