package com.textcaptcha.textingest.service.coref;

import com.textcaptcha.textingest.exception.IngestException;
import com.textcaptcha.textingest.pojo.ReceivedArticle;
import com.textcaptcha.textingest.service.IngestService;
import org.springframework.stereotype.Service;

@Service
public class CorefIngestService implements IngestService {

    @Override
    public void ingest(ReceivedArticle article) throws IngestException {
        // TODO
    }

}
