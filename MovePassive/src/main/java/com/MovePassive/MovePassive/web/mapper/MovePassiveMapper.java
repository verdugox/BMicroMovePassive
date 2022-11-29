package com.MovePassive.MovePassive.web.mapper;

import com.MovePassive.MovePassive.entity.MovePassive;
import com.MovePassive.MovePassive.web.model.MovePassiveModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovePassiveMapper {

    MovePassive modelToEntity (MovePassiveModel model);
    MovePassiveModel entityToModel(MovePassive event);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget MovePassive entity, MovePassive updateEntity);


}
