package io.github.raphaelmun1z.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_vaga", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"fonte", "codigoVaga"})
})
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String codigoVaga;
    private String titulo;
    private String empresa;
    private String salario;
    private String dataAnuncio;
    private String local;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String regime;

    @Column(columnDefinition = "TEXT")
    private String linkCandidatura;

    private String modalidade;
    private String fonte;

    public Vaga() {
    }

    public Vaga(String id, String codigoVaga, String titulo, String empresa, String salario, String dataAnuncio, String local, String descricao, String regime, String linkCandidatura, String modalidade, String fonte) {
        this.id = id;
        this.codigoVaga = codigoVaga;
        this.titulo = titulo;
        this.empresa = empresa;
        this.salario = salario;
        this.dataAnuncio = dataAnuncio;
        this.local = local;
        this.descricao = descricao;
        this.regime = regime;
        this.linkCandidatura = linkCandidatura;
        this.modalidade = modalidade;
        this.fonte = fonte;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoVaga() {
        return codigoVaga;
    }

    public void setCodigoVaga(String codigoVaga) {
        this.codigoVaga = codigoVaga;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getSalario() {
        return salario;
    }

    public void setSalario(String salario) {
        this.salario = salario;
    }

    public String getDataAnuncio() {
        return dataAnuncio;
    }

    public void setDataAnuncio(String dataAnuncio) {
        this.dataAnuncio = dataAnuncio;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRegime() {
        return regime;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    public String getLinkCandidatura() {
        return linkCandidatura;
    }

    public void setLinkCandidatura(String linkCandidatura) {
        this.linkCandidatura = linkCandidatura;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vaga vaga = (Vaga) o;
        return Objects.equals(codigoVaga, vaga.codigoVaga) && Objects.equals(fonte, vaga.fonte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoVaga, fonte);
    }

    @Override
    public String toString() {
        return """
            {
                "id": %s,
                "fonte": "%s",
                "codigo": "%s",
                "titulo": "%s",
                "empresa": "%s",
                "salario": "%s",
                "local": "%s",
                "modalidade": "%s",
                "regime": "%s",
                "link": "%s",
                "descricao": "%s"
            }""".formatted(
                id,
                fonte,
                codigoVaga,
                titulo,
                empresa,
                salario,
                local,
                modalidade,
                regime,
                linkCandidatura,
                descricao != null ? descricao.replace("\"", "'").replace("\n", " ") : ""
        );
    }
}
