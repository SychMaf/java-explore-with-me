package ru.practicum.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutputUpdateStatusRequestsDto {
    private List<OutputRequestDto> confirmedRequests;
    private List<OutputRequestDto> rejectedRequests;
}
