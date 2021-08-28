package com.textcaptcha.textingest.dto;

import com.textcaptcha.dto.ArticleHashPairDto;

public class IngestResultDto extends ArticleHashPairDto {
    public IngestResultDto(String urlHash, String textHash) {
        super(urlHash, textHash);
    }
}
