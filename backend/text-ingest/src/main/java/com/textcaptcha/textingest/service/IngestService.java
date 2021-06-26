package com.textcaptcha.textingest.service;

import com.textcaptcha.textingest.exception.IngestException;
import com.textcaptcha.textingest.pojo.ReceivedArticle;

public interface IngestService {
    void ingest(ReceivedArticle article) throws IngestException;
}
