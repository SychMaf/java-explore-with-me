package org.dtoPoint.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InputUpdateStatusRequestDto {
    private List<Long> requestIds;
    private RequestStatus status;
}
