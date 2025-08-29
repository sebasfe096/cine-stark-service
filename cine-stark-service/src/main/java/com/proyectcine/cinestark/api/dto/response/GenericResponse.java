package com.proyectcine.cinestark.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse {

    private Object result;

    private Long resultCode;

    private String message ;

    @JsonIgnore
    private static final String META_ATTRIBUTE = "meta";

    @JsonIgnore
    private static final String MESSAGE_OBJECT_ATTRIBUTE = "message";

    public static GenericResponse buildGenericPortalResponseDTO(String attributeName, Object data,
                                                                         MetaResponseDTO meta, MessageDTO messageDTO, Long resultCode,
                                                                         String message){
        Map<String, Object> dynamicDataResponse = new LinkedHashMap<>();
        if (attributeName != null) {
            dynamicDataResponse.put(attributeName, data);
            if (meta != null) dynamicDataResponse.put(META_ATTRIBUTE, meta);
            if (messageDTO != null) dynamicDataResponse.put(MESSAGE_OBJECT_ATTRIBUTE, messageDTO);
        }
        return GenericResponse.builder()
                .result(attributeName != null ? dynamicDataResponse : data)
                .resultCode(resultCode)
                .message(message).build();
    }
}
