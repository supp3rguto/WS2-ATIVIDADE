package services;

import io.github.raphaelmun1z.dto.req.VagaRequestDTO;
import io.github.raphaelmun1z.dto.res.VagaResponseDTO;
import io.github.raphaelmun1z.entities.Vaga;
import io.github.raphaelmun1z.repositories.VagaRepository;
import io.github.raphaelmun1z.services.VagaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VagaServiceTest {
    @Mock
    private VagaRepository repository;

    @InjectMocks
    private VagaService service;

    @Captor
    private ArgumentCaptor<Example<Vaga>> exampleCaptor;

    private VagaRequestDTO criarVagaRequestDTO() {
        return criarVagaRequestDTO("12345", "LinkedIn");
    }

    private VagaRequestDTO criarVagaRequestDTO(String codigo, String fonte) {
        return new VagaRequestDTO(
                codigo,
                "Desenvolvedor Java",
                "Empresa Tech",
                "R$ 10.000",
                "14/02/2026",
                "São Paulo, SP",
                "Descrição completa da vaga...",
                "CLT",
                "https://empresa.com/vaga/123",
                "Híbrido",
                fonte
        );
    }

    private Vaga criarVagaEntidade() {
        Vaga vaga = new Vaga();
        vaga.setId("uuid-123");
        vaga.setCodigoVaga("12345");
        vaga.setTitulo("Desenvolvedor Java");
        vaga.setEmpresa("Empresa Tech");
        vaga.setSalario("R$ 10.000");
        vaga.setDataAnuncio("14/02/2026");
        vaga.setLocal("São Paulo, SP");
        vaga.setDescricao("Descrição completa da vaga...");
        vaga.setRegime("CLT");
        vaga.setLinkCandidatura("https://empresa.com/vaga/123");
        vaga.setModalidade("Híbrido");
        vaga.setFonte("LinkedIn");
        return vaga;
    }

    @Nested
    @DisplayName("Cenários de Salvar (Individual)")
    class SalvarTests {

        @Test
        @DisplayName("Deve salvar uma nova vaga quando ela ainda não existir")
        void deveSalvarNovaVaga() {
            // Given
            VagaRequestDTO dto = criarVagaRequestDTO();
            Vaga vagaSalvaSimulada = criarVagaEntidade();

            when(repository.findByFonteAndCodigoVaga(dto.fonte(), dto.codigoVaga()))
                    .thenReturn(Optional.empty());
            when(repository.save(any(Vaga.class))).thenReturn(vagaSalvaSimulada);

            // When
            VagaResponseDTO result = service.salvar(dto);

            // Then
            assertThat(result).isNotNull();

            verify(repository, times(1)).save(any(Vaga.class));

            ArgumentCaptor<Vaga> vagaCaptor = ArgumentCaptor.forClass(Vaga.class);
            verify(repository).save(vagaCaptor.capture());

            Vaga vagaCapturada = vagaCaptor.getValue();

            assertThat(vagaCapturada.getFonte()).isEqualTo("LinkedIn");
            assertThat(vagaCapturada.getCodigoVaga()).isEqualTo(dto.codigoVaga());

            assertThat(vagaCapturada.getTitulo()).isEqualTo("Desenvolvedor Java");
            assertThat(vagaCapturada.getEmpresa()).isEqualTo(dto.empresa());
            assertThat(vagaCapturada.getLinkCandidatura()).isEqualTo(dto.linkCandidatura());
        }

        @Test
        @DisplayName("Deve retornar vaga existente sem salvar novamente quando já existir")
        void deveRetornarExistente() {
            // Given
            VagaRequestDTO dto = criarVagaRequestDTO();
            Vaga vagaExistente = criarVagaEntidade();

            when(repository.findByFonteAndCodigoVaga(dto.fonte(), dto.codigoVaga()))
                    .thenReturn(Optional.of(vagaExistente));

            // When
            VagaResponseDTO result = service.salvar(dto);

            // Then
            assertThat(result).isNull();
            assertThat(result.id()).isEqualTo(vagaExistente.getId());

            verify(repository, times(1)).save(any(Vaga.class));
        }
    }

    @Nested
    @DisplayName("Cenários de Salvar Várias (Lote)")
    class SalvarVariasTests {

        @Test
        @DisplayName("Deve processar lista de DTOs corretamente")
        void deveSalvarLista() {
            // Given
            VagaRequestDTO dto1 = criarVagaRequestDTO("001", "Site A");
            VagaRequestDTO dto2 = criarVagaRequestDTO("002", "Site B");
            List<VagaRequestDTO> lista = List.of(dto1, dto2);

            when(repository.findByFonteAndCodigoVaga(anyString(), anyString()))
                    .thenReturn(Optional.empty());
            when(repository.save(any(Vaga.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            service.salvarVarias(lista);

            // Then
            verify(repository, times(2)).findByFonteAndCodigoVaga(anyString(), anyString());

            verify(repository, times(3)).save(any(Vaga.class));
        }
    }

    @Nested
    @DisplayName("Cenários de Busca por ID")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Deve retornar DTO quando ID existe")
        void deveRetornarVagaPorId() {
            // Given
            String id = "uuid-123";
            Vaga vaga = criarVagaEntidade();
            when(repository.findById(id)).thenReturn(Optional.of(vaga));

            // When
            VagaResponseDTO result = service.buscarPorId(id);

            // Then
            assertThat(result).isNotNull();

            assertThat(result.id()).isEqualTo("uuid-123");
        }

        @Test
        @DisplayName("Deve lançar RuntimeException quando ID não existe")
        void deveLancarExcecaoQuandoNaoEncontrado() {
            // Given
            String id = "uuid-invalido";
            when(repository.findById(id)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> service.buscarPorId(id))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Vaga não encontrada com o ID: " + id);
        }
    }

    @Nested
    @DisplayName("Cenários de Listagem e Filtros")
    class ListagemTests {

        @Test
        @DisplayName("Deve retornar página de vagas listadas")
        void deveListarTodas() {
            // Given
            Pageable pageable = Pageable.unpaged();
            List<Vaga> vagas = List.of(criarVagaEntidade());
            Page<Vaga> page = new PageImpl<>(vagas);

            when(repository.findAll(pageable)).thenReturn(page);

            // When
            Page<VagaResponseDTO> result = service.listarTodas(pageable);

            // Then
            assertThat(result).isNotEmpty();

            assertThat(result.getContent()).hasSize(5);

            assertThat(result.getContent().get(0).fonte()).isEqualTo("Glassdoor");
        }

        @Test
        @DisplayName("Deve buscar utilizando Example Matcher ao filtrar")
        void deveListarComFiltros() {
            // Given
            VagaRequestDTO filtros = criarVagaRequestDTO();
            Pageable pageable = Pageable.unpaged();
            Page<Vaga> page = new PageImpl<>(List.of(criarVagaEntidade()));

            when(repository.findAll(any(), eq(pageable))).thenReturn(page);

            // When
            Page<VagaResponseDTO> result = service.listarComFiltros(filtros, pageable);

            // Then
            assertThat(result).isNotEmpty();

            verify(repository).findAll(exampleCaptor.capture(), eq(pageable));

            Vaga probe = exampleCaptor.getValue().getProbe();
            assertThat(probe.getFonte()).isEqualTo(filtros.fonte());

            assertThat(probe.getCodigoVaga()).isEqualTo("12345");
        }
    }
}