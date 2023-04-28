package edu.pja.s18776.sri2.sri2.services;

import edu.pja.s18776.sri2.sri2.dto.DTOEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class SimpleMappingService {
    private ModelMapper mapper;

    public SimpleMappingService(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public DTOEntity convertToDto(Object obj, DTOEntity dto) {
        return mapper.map(obj, dto.getClass());
    }
    public Object convertToEntity(Object obj, DTOEntity dto) {
        return mapper.map(dto, obj.getClass());
    }
}
