package com.hitachi.aiops.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Situation entity.
 */
public class SituationDTO implements Serializable {

    private Long id;

    private String name;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SituationDTO situationDTO = (SituationDTO) o;
        if (situationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), situationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SituationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
