package com.ravunana.repository.search;
import com.ravunana.domain.Factura;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Factura} entity.
 */
public interface FacturaSearchRepository extends ElasticsearchRepository<Factura, Long> {
}
