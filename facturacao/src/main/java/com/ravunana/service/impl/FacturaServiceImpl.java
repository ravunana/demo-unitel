package com.ravunana.service.impl;

import com.ravunana.service.FacturaService;
import com.ravunana.domain.Factura;
import com.ravunana.repository.FacturaRepository;
import com.ravunana.repository.search.FacturaSearchRepository;
import com.ravunana.service.dto.FacturaDTO;
import com.ravunana.service.mapper.FacturaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Factura}.
 */
@Service
@Transactional
public class FacturaServiceImpl implements FacturaService {

    private final Logger log = LoggerFactory.getLogger(FacturaServiceImpl.class);

    private final FacturaRepository facturaRepository;

    private final FacturaMapper facturaMapper;

    private final FacturaSearchRepository facturaSearchRepository;

    public FacturaServiceImpl(FacturaRepository facturaRepository, FacturaMapper facturaMapper, FacturaSearchRepository facturaSearchRepository) {
        this.facturaRepository = facturaRepository;
        this.facturaMapper = facturaMapper;
        this.facturaSearchRepository = facturaSearchRepository;
    }

    /**
     * Save a factura.
     *
     * @param facturaDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FacturaDTO save(FacturaDTO facturaDTO) {
        log.debug("Request to save Factura : {}", facturaDTO);
        Factura factura = facturaMapper.toEntity(facturaDTO);
        factura = facturaRepository.save(factura);
        FacturaDTO result = facturaMapper.toDto(factura);
        facturaSearchRepository.save(factura);
        return result;
    }

    /**
     * Get all the facturas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FacturaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Facturas");
        return facturaRepository.findAll(pageable)
            .map(facturaMapper::toDto);
    }


    /**
     * Get one factura by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FacturaDTO> findOne(Long id) {
        log.debug("Request to get Factura : {}", id);
        return facturaRepository.findById(id)
            .map(facturaMapper::toDto);
    }

    /**
     * Delete the factura by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Factura : {}", id);
        facturaRepository.deleteById(id);
        facturaSearchRepository.deleteById(id);
    }

    /**
     * Search for the factura corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FacturaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Facturas for query {}", query);
        return facturaSearchRepository.search(queryStringQuery(query), pageable)
            .map(facturaMapper::toDto);
    }
}
