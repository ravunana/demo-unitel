package com.ravunana.web.rest;

import com.ravunana.FacturacaoApp;
import com.ravunana.config.SecurityBeanOverrideConfiguration;
import com.ravunana.domain.Factura;
import com.ravunana.repository.FacturaRepository;
import com.ravunana.repository.search.FacturaSearchRepository;
import com.ravunana.service.FacturaService;
import com.ravunana.service.dto.FacturaDTO;
import com.ravunana.service.mapper.FacturaMapper;
import com.ravunana.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.ravunana.web.rest.TestUtil.sameInstant;
import static com.ravunana.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ravunana.domain.enumeration.Tipo;
/**
 * Integration tests for the {@link FacturaResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FacturacaoApp.class})
public class FacturaResourceIT {

    private static final Tipo DEFAULT_TIPO = Tipo.COMPRA;
    private static final Tipo UPDATED_TIPO = Tipo.VENDA;

    private static final String DEFAULT_BENEFICIARIO = "AAAAAAAAAA";
    private static final String UPDATED_BENEFICIARIO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUTO_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_PRODUTO_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUTO_NOME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUTO_NOME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRODUTO_PRECO = 0D;
    private static final Double UPDATED_PRODUTO_PRECO = 1D;

    private static final Integer DEFAULT_PRODUTO_QUANTIDADE = 1;
    private static final Integer UPDATED_PRODUTO_QUANTIDADE = 2;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private FacturaMapper facturaMapper;

    @Autowired
    private FacturaService facturaService;

    /**
     * This repository is mocked in the com.ravunana.repository.search test package.
     *
     * @see com.ravunana.repository.search.FacturaSearchRepositoryMockConfiguration
     */
    @Autowired
    private FacturaSearchRepository mockFacturaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFacturaMockMvc;

    private Factura factura;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FacturaResource facturaResource = new FacturaResource(facturaService);
        this.restFacturaMockMvc = MockMvcBuilders.standaloneSetup(facturaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createEntity(EntityManager em) {
        Factura factura = new Factura()
            .tipo(DEFAULT_TIPO)
            .beneficiario(DEFAULT_BENEFICIARIO)
            .data(DEFAULT_DATA)
            .numero(DEFAULT_NUMERO)
            .produtoCodigo(DEFAULT_PRODUTO_CODIGO)
            .produtoNome(DEFAULT_PRODUTO_NOME)
            .produtoPreco(DEFAULT_PRODUTO_PRECO)
            .produtoQuantidade(DEFAULT_PRODUTO_QUANTIDADE);
        return factura;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createUpdatedEntity(EntityManager em) {
        Factura factura = new Factura()
            .tipo(UPDATED_TIPO)
            .beneficiario(UPDATED_BENEFICIARIO)
            .data(UPDATED_DATA)
            .numero(UPDATED_NUMERO)
            .produtoCodigo(UPDATED_PRODUTO_CODIGO)
            .produtoNome(UPDATED_PRODUTO_NOME)
            .produtoPreco(UPDATED_PRODUTO_PRECO)
            .produtoQuantidade(UPDATED_PRODUTO_QUANTIDADE);
        return factura;
    }

    @BeforeEach
    public void initTest() {
        factura = createEntity(em);
    }

    @Test
    @Transactional
    public void createFactura() throws Exception {
        int databaseSizeBeforeCreate = facturaRepository.findAll().size();

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);
        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isCreated());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate + 1);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testFactura.getBeneficiario()).isEqualTo(DEFAULT_BENEFICIARIO);
        assertThat(testFactura.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testFactura.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testFactura.getProdutoCodigo()).isEqualTo(DEFAULT_PRODUTO_CODIGO);
        assertThat(testFactura.getProdutoNome()).isEqualTo(DEFAULT_PRODUTO_NOME);
        assertThat(testFactura.getProdutoPreco()).isEqualTo(DEFAULT_PRODUTO_PRECO);
        assertThat(testFactura.getProdutoQuantidade()).isEqualTo(DEFAULT_PRODUTO_QUANTIDADE);

        // Validate the Factura in Elasticsearch
        verify(mockFacturaSearchRepository, times(1)).save(testFactura);
    }

    @Test
    @Transactional
    public void createFacturaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = facturaRepository.findAll().size();

        // Create the Factura with an existing ID
        factura.setId(1L);
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Factura in Elasticsearch
        verify(mockFacturaSearchRepository, times(0)).save(factura);
    }


    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setTipo(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBeneficiarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setBeneficiario(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProdutoCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setProdutoCodigo(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProdutoNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setProdutoNome(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProdutoPrecoIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setProdutoPreco(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProdutoQuantidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setProdutoQuantidade(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFacturas() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList
        restFacturaMockMvc.perform(get("/api/facturas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].beneficiario").value(hasItem(DEFAULT_BENEFICIARIO)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].produtoCodigo").value(hasItem(DEFAULT_PRODUTO_CODIGO)))
            .andExpect(jsonPath("$.[*].produtoNome").value(hasItem(DEFAULT_PRODUTO_NOME)))
            .andExpect(jsonPath("$.[*].produtoPreco").value(hasItem(DEFAULT_PRODUTO_PRECO.doubleValue())))
            .andExpect(jsonPath("$.[*].produtoQuantidade").value(hasItem(DEFAULT_PRODUTO_QUANTIDADE)));
    }
    
    @Test
    @Transactional
    public void getFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get the factura
        restFacturaMockMvc.perform(get("/api/facturas/{id}", factura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(factura.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.beneficiario").value(DEFAULT_BENEFICIARIO))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.produtoCodigo").value(DEFAULT_PRODUTO_CODIGO))
            .andExpect(jsonPath("$.produtoNome").value(DEFAULT_PRODUTO_NOME))
            .andExpect(jsonPath("$.produtoPreco").value(DEFAULT_PRODUTO_PRECO.doubleValue()))
            .andExpect(jsonPath("$.produtoQuantidade").value(DEFAULT_PRODUTO_QUANTIDADE));
    }

    @Test
    @Transactional
    public void getNonExistingFactura() throws Exception {
        // Get the factura
        restFacturaMockMvc.perform(get("/api/facturas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura
        Factura updatedFactura = facturaRepository.findById(factura.getId()).get();
        // Disconnect from session so that the updates on updatedFactura are not directly saved in db
        em.detach(updatedFactura);
        updatedFactura
            .tipo(UPDATED_TIPO)
            .beneficiario(UPDATED_BENEFICIARIO)
            .data(UPDATED_DATA)
            .numero(UPDATED_NUMERO)
            .produtoCodigo(UPDATED_PRODUTO_CODIGO)
            .produtoNome(UPDATED_PRODUTO_NOME)
            .produtoPreco(UPDATED_PRODUTO_PRECO)
            .produtoQuantidade(UPDATED_PRODUTO_QUANTIDADE);
        FacturaDTO facturaDTO = facturaMapper.toDto(updatedFactura);

        restFacturaMockMvc.perform(put("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testFactura.getBeneficiario()).isEqualTo(UPDATED_BENEFICIARIO);
        assertThat(testFactura.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testFactura.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testFactura.getProdutoCodigo()).isEqualTo(UPDATED_PRODUTO_CODIGO);
        assertThat(testFactura.getProdutoNome()).isEqualTo(UPDATED_PRODUTO_NOME);
        assertThat(testFactura.getProdutoPreco()).isEqualTo(UPDATED_PRODUTO_PRECO);
        assertThat(testFactura.getProdutoQuantidade()).isEqualTo(UPDATED_PRODUTO_QUANTIDADE);

        // Validate the Factura in Elasticsearch
        verify(mockFacturaSearchRepository, times(1)).save(testFactura);
    }

    @Test
    @Transactional
    public void updateNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc.perform(put("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Factura in Elasticsearch
        verify(mockFacturaSearchRepository, times(0)).save(factura);
    }

    @Test
    @Transactional
    public void deleteFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeDelete = facturaRepository.findAll().size();

        // Delete the factura
        restFacturaMockMvc.perform(delete("/api/facturas/{id}", factura.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Factura in Elasticsearch
        verify(mockFacturaSearchRepository, times(1)).deleteById(factura.getId());
    }

    @Test
    @Transactional
    public void searchFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);
        when(mockFacturaSearchRepository.search(queryStringQuery("id:" + factura.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(factura), PageRequest.of(0, 1), 1));
        // Search the factura
        restFacturaMockMvc.perform(get("/api/_search/facturas?query=id:" + factura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].beneficiario").value(hasItem(DEFAULT_BENEFICIARIO)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].produtoCodigo").value(hasItem(DEFAULT_PRODUTO_CODIGO)))
            .andExpect(jsonPath("$.[*].produtoNome").value(hasItem(DEFAULT_PRODUTO_NOME)))
            .andExpect(jsonPath("$.[*].produtoPreco").value(hasItem(DEFAULT_PRODUTO_PRECO.doubleValue())))
            .andExpect(jsonPath("$.[*].produtoQuantidade").value(hasItem(DEFAULT_PRODUTO_QUANTIDADE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Factura.class);
        Factura factura1 = new Factura();
        factura1.setId(1L);
        Factura factura2 = new Factura();
        factura2.setId(factura1.getId());
        assertThat(factura1).isEqualTo(factura2);
        factura2.setId(2L);
        assertThat(factura1).isNotEqualTo(factura2);
        factura1.setId(null);
        assertThat(factura1).isNotEqualTo(factura2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacturaDTO.class);
        FacturaDTO facturaDTO1 = new FacturaDTO();
        facturaDTO1.setId(1L);
        FacturaDTO facturaDTO2 = new FacturaDTO();
        assertThat(facturaDTO1).isNotEqualTo(facturaDTO2);
        facturaDTO2.setId(facturaDTO1.getId());
        assertThat(facturaDTO1).isEqualTo(facturaDTO2);
        facturaDTO2.setId(2L);
        assertThat(facturaDTO1).isNotEqualTo(facturaDTO2);
        facturaDTO1.setId(null);
        assertThat(facturaDTO1).isNotEqualTo(facturaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(facturaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(facturaMapper.fromId(null)).isNull();
    }
}
