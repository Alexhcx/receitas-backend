package com.puc.bancodedados.receitas.services;

import com.puc.bancodedados.receitas.dtos.ReceitaIngredienteItemDTO;
import com.puc.bancodedados.receitas.dtos.ReceitaRequestDTO;
import com.puc.bancodedados.receitas.dtos.ReceitaResponseDTO;
import com.puc.bancodedados.receitas.model.*;
import com.puc.bancodedados.receitas.model.ids.ReceitaIngredienteId; // Necessário se for setar o ID manualmente
import com.puc.bancodedados.receitas.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final CozinheiroRepository cozinheiroRepository;
    private final CategoriaRepository categoriaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final ReceitaIngredienteRepository receitaIngredienteRepository;

    @Autowired
    public ReceitaService(ReceitaRepository receitaRepository,
                          CozinheiroRepository cozinheiroRepository,
                          CategoriaRepository categoriaRepository,
                          IngredienteRepository ingredienteRepository,
                          ReceitaIngredienteRepository receitaIngredienteRepository) {
        this.receitaRepository = receitaRepository;
        this.cozinheiroRepository = cozinheiroRepository;
        this.categoriaRepository = categoriaRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.receitaIngredienteRepository = receitaIngredienteRepository;
    }

    @Transactional
    public ReceitaResponseDTO criarReceita(ReceitaRequestDTO requestDTO) {
        Cozinheiro cozinheiro = cozinheiroRepository.findById(requestDTO.cozinheiroRg())
                .orElseThrow(() -> new EntityNotFoundException("Cozinheiro com RG '" + requestDTO.cozinheiroRg() + "' não encontrado."));
        Categoria categoria = categoriaRepository.findById(requestDTO.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria com ID '" + requestDTO.categoriaId() + "' não encontrada."));

        receitaRepository.findByNomeReceitaAndCozinheiroCozinheiroRg(requestDTO.nomeReceita(), requestDTO.cozinheiroRg())
                .ifPresent(r -> {
                    throw new IllegalArgumentException("Cozinheiro já possui uma receita com o nome '" + requestDTO.nomeReceita() + "'.");
                });

        Receita receita = new Receita();
        receita.setNomeReceita(requestDTO.nomeReceita());
        receita.setDescricaoModoPreparo(requestDTO.descricaoModoPreparo());
        receita.setDataCriacao(requestDTO.dataCriacao());
        receita.setNumeroPorcoes(requestDTO.numeroPorcoes());
        receita.setCozinheiro(cozinheiro);
        receita.setCategoria(categoria);

        Receita receitaSalva = receitaRepository.save(receita);

        Set<ReceitaIngrediente> ingredientesProcessados = new HashSet<>();
        if (requestDTO.ingredientes() != null) {
            for (ReceitaIngredienteItemDTO itemDTO : requestDTO.ingredientes()) {
                Ingrediente ingrediente = ingredienteRepository.findById(itemDTO.ingredienteId())
                        .orElseThrow(() -> new EntityNotFoundException("Ingrediente com ID '" + itemDTO.ingredienteId() + "' não encontrado."));

                ReceitaIngrediente ri = new ReceitaIngrediente();
                ReceitaIngredienteId riId = new ReceitaIngredienteId(receitaSalva.getId(), ingrediente.getId());
                ri.setId(riId);
                ri.setReceita(receitaSalva);
                ri.setIngrediente(ingrediente);
                ri.setQuantidade(itemDTO.quantidade());
                ri.setMedida(itemDTO.medida());

                ingredientesProcessados.add(receitaIngredienteRepository.save(ri));
            }
        }

        receitaSalva.setReceitaIngredientes(ingredientesProcessados);

        return mapToResponseDTO(receitaSalva);
    }

    @Transactional(readOnly = true)
    public List<ReceitaResponseDTO> listarReceitas() {
        return receitaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReceitaResponseDTO buscarReceitaPorId(Long id) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receita não encontrada com ID: " + id));
        return mapToResponseDTO(receita);
    }

    @Transactional
    public ReceitaResponseDTO atualizarReceita(Long id, ReceitaRequestDTO requestDTO) {
        Receita receitaExistente = receitaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receita não encontrada com ID: " + id));

        Cozinheiro cozinheiro = cozinheiroRepository.findById(requestDTO.cozinheiroRg())
                .orElseThrow(() -> new EntityNotFoundException("Cozinheiro com RG '" + requestDTO.cozinheiroRg() + "' não encontrado."));
        Categoria categoria = categoriaRepository.findById(requestDTO.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria com ID '" + requestDTO.categoriaId() + "' não encontrada."));

        if (!receitaExistente.getNomeReceita().equals(requestDTO.nomeReceita()) ||
                !receitaExistente.getCozinheiro().getCozinheiroRg().equals(requestDTO.cozinheiroRg())) {
            receitaRepository.findByNomeReceitaAndCozinheiroCozinheiroRg(requestDTO.nomeReceita(), requestDTO.cozinheiroRg())
                    .ifPresent(r -> {
                        if (!r.getId().equals(id)) {
                            throw new IllegalArgumentException("Cozinheiro já possui outra receita com o nome '" + requestDTO.nomeReceita() + "'.");
                        }
                    });
        }

        receitaExistente.setNomeReceita(requestDTO.nomeReceita());
        receitaExistente.setDescricaoModoPreparo(requestDTO.descricaoModoPreparo());
        receitaExistente.setDataCriacao(requestDTO.dataCriacao());
        receitaExistente.setNumeroPorcoes(requestDTO.numeroPorcoes());
        receitaExistente.setCozinheiro(cozinheiro);
        receitaExistente.setCategoria(categoria);

        Set<ReceitaIngrediente> ingredientesAntigos = new HashSet<>(receitaExistente.getReceitaIngredientes());

        receitaExistente.getReceitaIngredientes().clear();

        if (!ingredientesAntigos.isEmpty()) {
            receitaIngredienteRepository.deleteAllInBatch(ingredientesAntigos);
        }

        if (requestDTO.ingredientes() != null) {
            for (ReceitaIngredienteItemDTO itemDTO : requestDTO.ingredientes()) {
                Ingrediente ingrediente = ingredienteRepository.findById(itemDTO.ingredienteId())
                        .orElseThrow(() -> new EntityNotFoundException("Ingrediente com ID '" + itemDTO.ingredienteId() + "' não encontrado."));

                ReceitaIngrediente ri = new ReceitaIngrediente();

                ReceitaIngredienteId riId = new ReceitaIngredienteId(receitaExistente.getId(), ingrediente.getId());
                ri.setId(riId);

                ri.setReceita(receitaExistente);
                ri.setIngrediente(ingrediente);

                ri.setQuantidade(itemDTO.quantidade());
                ri.setMedida(itemDTO.medida());

                receitaExistente.getReceitaIngredientes().add(ri);
            }
        }

        Receita receitaAtualizada = receitaRepository.save(receitaExistente);

        return mapToResponseDTO(receitaAtualizada);
    }

    @Transactional
    public void deletarReceita(Long id) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receita não encontrada com ID: " + id));
        receitaRepository.delete(receita);
    }

    private ReceitaResponseDTO mapToResponseDTO(Receita receita) {
        Set<ReceitaIngredienteItemDTO> ingredientesDTO = new HashSet<>();
        if (receita.getReceitaIngredientes() != null) { // Verifica se a coleção não é nula
            ingredientesDTO = receita.getReceitaIngredientes().stream()
                    .map(ri -> new ReceitaIngredienteItemDTO(
                            ri.getIngrediente() != null ? ri.getIngrediente().getId() : null,
                            ri.getQuantidade(),
                            ri.getMedida()
                    ))
                    .collect(Collectors.toSet());
        }


        String nomeCozinheiro = "N/A";
        if (receita.getCozinheiro() != null && receita.getCozinheiro().getEmpregado() != null) {
            nomeCozinheiro = receita.getCozinheiro().getEmpregado().getNomeEmpregado();
        } else if (receita.getCozinheiro() != null && receita.getCozinheiro().getNomeFantasia() != null) {
            nomeCozinheiro = receita.getCozinheiro().getNomeFantasia();
        }


        return new ReceitaResponseDTO(
                receita.getId(),
                receita.getNomeReceita(),
                receita.getDescricaoModoPreparo(),
                receita.getDataCriacao(),
                receita.getNumeroPorcoes(),
                receita.getCozinheiro() != null ? receita.getCozinheiro().getCozinheiroRg() : null,
                nomeCozinheiro,
                receita.getCategoria() != null ? receita.getCategoria().getId() : null,
                receita.getCategoria() != null ? receita.getCategoria().getNomeCategoria() : "N/A",
                ingredientesDTO
        );
    }
}