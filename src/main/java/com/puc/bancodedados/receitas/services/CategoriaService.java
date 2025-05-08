package com.puc.bancodedados.receitas.services;


import com.puc.bancodedados.receitas.dtos.CategoriaRequestDTO;
import com.puc.bancodedados.receitas.dtos.CategoriaResponseDTO;
import com.puc.bancodedados.receitas.model.Categoria;
import com.puc.bancodedados.receitas.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO categoriaRequestDTO) {
        categoriaRepository.findByNomeCategoria(categoriaRequestDTO.nomeCategoria())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Categoria com nome '" + categoriaRequestDTO.nomeCategoria() + "' já existe.");
                });

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(categoriaRequestDTO.nomeCategoria());
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return mapToCategoriaResponseDTO(categoriaSalva);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::mapToCategoriaResponseDTO) // O método de mapeamento é reutilizado
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + id));
        return mapToCategoriaResponseDTO(categoria);
    }

    @Transactional
    public CategoriaResponseDTO atualizarCategoria(Long id, CategoriaRequestDTO categoriaRequestDTO) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + id));

        categoriaRepository.findByNomeCategoria(categoriaRequestDTO.nomeCategoria())
                .ifPresent(c -> {
                    if (!c.getId().equals(id)) {
                        throw new IllegalArgumentException("Outra categoria com nome '" + categoriaRequestDTO.nomeCategoria() + "' já existe.");
                    }
                });

        categoriaExistente.setNomeCategoria(categoriaRequestDTO.nomeCategoria());
        Categoria categoriaAtualizada = categoriaRepository.save(categoriaExistente);
        return mapToCategoriaResponseDTO(categoriaAtualizada);
    }

    @Transactional
    public void deletarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + id));

        if (categoria.getReceitas() != null && !categoria.getReceitas().isEmpty()) {
            throw new IllegalStateException("Categoria não pode ser deletada pois possui receitas associadas.");
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaResponseDTO mapToCategoriaResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(categoria.getId(), categoria.getNomeCategoria());
    }
}
