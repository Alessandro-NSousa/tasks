package com.tarefas.mapper;

import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.domain.user.User;
import com.tarefas.domain.enumeration.Status;
import com.tarefas.dto.TarefaPutRequestDTO;
import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.dto.TarefaResponseDTO;
import com.tarefas.dto.TarefaResponsePutDTO;
import com.tarefas.repository.UserRepository;
import org.mapstruct.*;

import java.util.UUID;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR,
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TarefaMapper {

    @Mapping(target = "colaborador", source = "usuario")
    TarefaResponseDTO tarefaToTarefaResponseDTO(Tarefa tarefa);

    //target = alvo - source = fonte
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criacao", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "ultimaAlteracao", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
    @Mapping(target = "usuario.id", source = "colaboradorId")
    @Mapping(target = "ativo", constant = "true")
    Tarefa tarefaRequestDTOToTarefa(TarefaRequestDTO requestDTO);





    @Mapping(target = "colaborador", source = "usuario.nome")
    TarefaResponsePutDTO tarefaToTarefaResponsePutDTO(Tarefa tarefa);


    // update
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criacao", ignore = true)
    @Mapping(target = "ultimaAlteracao", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
    @Mapping(target = "usuario", expression = "java(buscarUsuario(dto.colaboradorId(), tarefa, userRepository))")
    @Mapping(target = "ativo", ignore = true)
    void updateTarefaFromDTO(TarefaPutRequestDTO dto,
                             @MappingTarget Tarefa tarefa,
                             @Context UserRepository userRepository);


    @Named("mapStatus")
    default Status mapStatus(Status status) {
        return status != null ? status : Status.PENDENTE;
    }

    default User buscarUsuario(UUID colaboradorId,
                               Tarefa tarefa,
                               UserRepository userRepository) {
        if (colaboradorId == null) {
            return tarefa.getUsuario();
        }
        if (tarefa.getUsuario() != null &&
                tarefa.getUsuario().getId().equals(colaboradorId)) {
            return tarefa.getUsuario();
        }

        return userRepository.findById(colaboradorId)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
    }


}
