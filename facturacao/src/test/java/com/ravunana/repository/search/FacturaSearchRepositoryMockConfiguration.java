package com.ravunana.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link FacturaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FacturaSearchRepositoryMockConfiguration {

    @MockBean
    private FacturaSearchRepository mockFacturaSearchRepository;

}
