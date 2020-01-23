package com.ravunana.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;

import com.ravunana.domain.enumeration.Tipo;

/**
 * A Factura.
 */
@Entity
@Table(name = "factura")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "factura")
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Tipo tipo;

    @NotNull
    @Column(name = "beneficiario", nullable = false)
    private String beneficiario;

    @Column(name = "data")
    private ZonedDateTime data;

    
    @Column(name = "numero", unique = true)
    private String numero;

    @NotNull
    @Column(name = "produto_codigo", nullable = false)
    private String produtoCodigo;

    @NotNull
    @Column(name = "produto_nome", nullable = false)
    private String produtoNome;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "produto_preco", nullable = false)
    private Double produtoPreco;

    @NotNull
    @Min(value = 1)
    @Column(name = "produto_quantidade", nullable = false)
    private Integer produtoQuantidade;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Factura tipo(Tipo tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public Factura beneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
        return this;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public Factura data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public String getNumero() {
        return numero;
    }

    public Factura numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getProdutoCodigo() {
        return produtoCodigo;
    }

    public Factura produtoCodigo(String produtoCodigo) {
        this.produtoCodigo = produtoCodigo;
        return this;
    }

    public void setProdutoCodigo(String produtoCodigo) {
        this.produtoCodigo = produtoCodigo;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public Factura produtoNome(String produtoNome) {
        this.produtoNome = produtoNome;
        return this;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public Double getProdutoPreco() {
        return produtoPreco;
    }

    public Factura produtoPreco(Double produtoPreco) {
        this.produtoPreco = produtoPreco;
        return this;
    }

    public void setProdutoPreco(Double produtoPreco) {
        this.produtoPreco = produtoPreco;
    }

    public Integer getProdutoQuantidade() {
        return produtoQuantidade;
    }

    public Factura produtoQuantidade(Integer produtoQuantidade) {
        this.produtoQuantidade = produtoQuantidade;
        return this;
    }

    public void setProdutoQuantidade(Integer produtoQuantidade) {
        this.produtoQuantidade = produtoQuantidade;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Factura)) {
            return false;
        }
        return id != null && id.equals(((Factura) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Factura{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", beneficiario='" + getBeneficiario() + "'" +
            ", data='" + getData() + "'" +
            ", numero='" + getNumero() + "'" +
            ", produtoCodigo='" + getProdutoCodigo() + "'" +
            ", produtoNome='" + getProdutoNome() + "'" +
            ", produtoPreco=" + getProdutoPreco() +
            ", produtoQuantidade=" + getProdutoQuantidade() +
            "}";
    }
}
