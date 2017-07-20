package com.prosystemingegneri.preesence.business.entity;

import java.io.Serializable;

/**
 *
 * @author Bauke Scholtz
 * @param <T> IDs must extend Number class
 */
public abstract class BaseEntity<T extends Number> implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract T getId();

    //public abstract void setId(T id); //ID's are auto-generated, so there is no need to set them

    @Override
    public int hashCode() {
        return (getId() != null) 
            ? (getClass().getSimpleName().hashCode() + getId().hashCode())
            : super.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return (other != null && getId() != null
                && other.getClass().isAssignableFrom(getClass()) 
                && getClass().isAssignableFrom(other.getClass())) 
            ? getId().equals(((BaseEntity<?>) other).getId())
            : (other == this);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
    }

}