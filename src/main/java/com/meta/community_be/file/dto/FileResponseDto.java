package com.meta.community_be.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileResponseDto {
    private String originalFileName;
    private String storedFileName;
    private String filePath;
}