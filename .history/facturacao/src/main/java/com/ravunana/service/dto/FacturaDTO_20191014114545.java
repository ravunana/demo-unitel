package com.ravunana.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.ravunana.domain.enumeration.Tipo;

/**
 * A DTO for the {@link com.ravunana.domain.Factura} entity.
 */
public class FacturaDTO implements Serializable {

    private Long id;

    @NotNull
    private Tipo tipo;

    @NotNull
    private String beneficiario;

    private ZonedDateTime data;


    private String numero;

    @NotNull
    private String produtoCodigo;

    @NotNull
    private String produtoNome;

    @NotNull
    @DecimalMin(value = "0")
    private Double produtoPreco;

    @NotNull
    @Min(value = 1)
    private Integer produtoQuantidade;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getProdutoCodigo() {
        return produtoCodigo;
    }

    public void setProdutoCodigo(String produtoCodigo) {
        this.produtoCodigo = produtoCodigo;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public Double getProdutoPreco() {
        return produtoPreco;
    }

    public void setProdutoPreco(Double produtoPreco) {
        this.produtoPreco = produtoPreco;
    }

    public Integer getProdutoQuantidade() {
        return produtoQuantidade;
    }

    public void setProdutoQuantidade(Integer produtoQuantidade) {
        this.produtoQuantidade = produtoQuantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FacturaDTO facturaDTO = (FacturaDTO) o;
        if (facturaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), facturaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FacturaDTO{" +
            "id:" + getId() +
            ", tipo:'" + getTipo() + "'" +
            ", beneficiario:'" + getBeneficiario() + "'" +
            ", data:'" + getData() + "'" +
            ", numero:'" + getNumero() + "'" +
            ", produtoCodigo:'" + getProdutoCodigo() + "'" +
            ", produtoNome:'" + getProdutoNome() + "'" +
            ", produtoPreco:" + getProdutoPreco() +
            ", produtoQuantidade:" + getProdutoQuantidade() +
            "}";
    }
}
