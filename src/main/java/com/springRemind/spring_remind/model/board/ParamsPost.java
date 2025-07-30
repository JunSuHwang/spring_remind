package com.springRemind.spring_remind.model.board;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParamsPost {
    @NotEmpty
    @Size(min = 2, max = 50)
    @Schema(description = "작성자명", requiredMode = Schema.RequiredMode.REQUIRED)
    private String author;
    @NotEmpty
    @Size(min = 2, max = 100)
    @Schema(description = "제목", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    @NotEmpty
    @Size(min = 2, max = 500)
    @Schema(description = "내용", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;
}
