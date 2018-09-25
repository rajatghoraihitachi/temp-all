package com.hitachi.aiops.service.mapper;

import com.hitachi.aiops.domain.*;
import com.hitachi.aiops.service.dto.SituationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Situation and its DTO SituationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SituationMapper extends EntityMapper<SituationDTO, Situation> {



    default Situation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Situation situation = new Situation();
        situation.setId(id);
        return situation;
    }
}
