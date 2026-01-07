package com.tarefas.mapper;

import com.tarefas.domain.user.User;
import com.tarefas.dto.ColaboradorDTO;
import com.tarefas.dto.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDTO UserToUserResponseDTO(User user);

    ColaboradorDTO UserToColaboradorDTO(User user);
}
