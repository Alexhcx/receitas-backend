package com.puc.bancodedados.receitas.services;

import com.puc.bancodedados.receitas.dtos.DegustadorRequestDTO;
import com.puc.bancodedados.receitas.dtos.DegustadorResponseDTO;
import com.puc.bancodedados.receitas.model.Degustador;
import com.puc.bancodedados.receitas.model.Empregado;
import com.puc.bancodedados.receitas.repository.DegustadorRepository;
import com.puc.bancodedados.receitas.repository.EmpregadoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DegustadorService {

    private final DegustadorRepository degustadorRepository;
    private final EmpregadoRepository empregadoRepository;

    @Autowired
    public DegustadorService(DegustadorRepository degustadorRepository, EmpregadoRepository empregadoRepository) {
        this.degustadorRepository = degustadorRepository;
        this.empregadoRepository = empregadoRepository;
    }

    @Transactional
    public DegustadorResponseDTO criarDegustador(DegustadorRequestDTO requestDTO) {
        Empregado empregado = empregadoRepository.findById(requestDTO.degustadorRg())
                .orElseThrow(() -> new EntityNotFoundException("Empregado com RG '" + requestDTO.degustadorRg() + "' não encontrado para associar ao degustador."));

        if (degustadorRepository.existsById(requestDTO.degustadorRg())) {
            throw new IllegalArgumentException("Degustador com RG '" + requestDTO.degustadorRg() + "' já existe.");
        }

        Degustador degustador = new Degustador();
        degustador.setEmpregado(empregado);

        Degustador degustadorSalvo = degustadorRepository.save(degustador);

        return mapToResponseDTO(degustadorSalvo);
    }

    @Transactional(readOnly = true)
    public List<DegustadorResponseDTO> listarDegustadores() {
        return degustadorRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DegustadorResponseDTO buscarDegustadorPorRg(Long rg) {
        Degustador degustador = degustadorRepository.findById(rg)
                .orElseThrow(() -> new EntityNotFoundException("Degustador não encontrado com RG: " + rg));
        return mapToResponseDTO(degustador);
    }

    // Degustador não tem campos próprios para atualizar além do RG/Empregado,
    // então um método de atualização pode não ser necessário ou pode ser apenas para
    // reassociar a um Empregado diferente (complexo e geralmente não feito).
    // Por simplicidade, não incluirei um método de atualização específico para campos do Degustador.

    @Transactional
    public void deletarDegustador(Long rg) {
        Degustador degustador = degustadorRepository.findById(rg)
                .orElseThrow(() -> new EntityNotFoundException("Degustador não encontrado com RG: " + rg));
        // Verificar se há testes associados
        if (degustador.getTestes() != null && !degustador.getTestes().isEmpty()) {
            throw new IllegalStateException("Degustador não pode ser deletado pois possui testes associados.");
        }
        degustadorRepository.deleteById(rg);
    }

    private DegustadorResponseDTO mapToResponseDTO(Degustador degustador) {
        return new DegustadorResponseDTO(
                degustador.getDegustadorRg(),
                degustador.getEmpregado() != null ? degustador.getEmpregado().getNomeEmpregado() : null
        );
    }
}
