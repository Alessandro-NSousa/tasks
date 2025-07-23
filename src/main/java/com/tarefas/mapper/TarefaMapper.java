package com.tarefas.mapper;

import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.domain.user.User;
import com.tarefas.domain.enumeration.Status;
import com.tarefas.dto.TarefaPutRequestDTO;
import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.dto.TarefaResponseDTO;
import com.tarefas.repository.UserRepository;
import org.mapstruct.*;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR,
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TarefaMapper {

    @Mapping(target = "colaborador", source = "usuario.nome")
    TarefaResponseDTO TarefaToTarefaResponseDTO(Tarefa tarefa);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criacao", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "ultimaAlteracao", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
    @Mapping(target = "usuario", source = "colaborador")
    Tarefa tarefaRequestDTOToTarefa(TarefaRequestDTO requestDTO);

    // update
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criacao", ignore = true)
    @Mapping(target = "ultimaAlteracao", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
    @Mapping(target = "usuario", expression = "java(updateUser(dto.colaborador(), tarefa, userRepository))")
    void updateTarefaFromDTO(TarefaPutRequestDTO dto,
                             @MappingTarget Tarefa tarefa,
                             @Context UserRepository userRepository);


    @Named("mapStatus")
    default Status mapStatus(Status status) {
        return status != null ? status : Status.PENDENTE;
    }

    default User updateUser(User colaborador,
                            Tarefa tarefa,
                            UserRepository userRepository) {
        if (colaborador == null) {
            return tarefa.getUsuario();
        }
        if (tarefa.getUsuario() != null &&
                tarefa.getUsuario().getId().equals(colaborador.getId())) {
            return tarefa.getUsuario();
        }
        return userRepository.findById(colaborador.getId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
    }


}
