package ua.nure.cherkashyn.hotel.db.entity;


import java.io.Serializable;

/**
 * All entities have id field.
 *
 * @author Vladimir Cherkashyn
 */
public abstract class Entity implements Serializable {
    private static final long serialVersionUID = -2236423371138236784L;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

