package com.puc.bancodedados.receitas.init;

import com.puc.bancodedados.receitas.model.*;
import com.puc.bancodedados.receitas.model.ids.ReceitaIngredienteId;
import com.puc.bancodedados.receitas.model.ids.ReceitaLivroId;
import com.puc.bancodedados.receitas.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EmpregadoRepository empregadoRepository;
    private final CozinheiroRepository cozinheiroRepository;
    private final DegustadorRepository degustadorRepository;
    private final EditorRepository editorRepository;
    private final CategoriaRepository categoriaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final RestauranteRepository restauranteRepository;
    private final LivroRepository livroRepository;
    private final ReceitaRepository receitaRepository;
    private final ReceitaIngredienteRepository receitaIngredienteRepository;
    private final TesteRepository testeRepository; // Repositório da entidade Teste
    private final ReceitaLivroRepository receitaLivroRepository;

    public DataInitializer(EmpregadoRepository empregadoRepository,
                           CozinheiroRepository cozinheiroRepository,
                           DegustadorRepository degustadorRepository,
                           EditorRepository editorRepository,
                           CategoriaRepository categoriaRepository,
                           IngredienteRepository ingredienteRepository,
                           RestauranteRepository restauranteRepository,
                           LivroRepository livroRepository,
                           ReceitaRepository receitaRepository,
                           ReceitaIngredienteRepository receitaIngredienteRepository,
                           TesteRepository testeRepository, // Injeção correta
                           ReceitaLivroRepository receitaLivroRepository) {
        this.empregadoRepository = empregadoRepository;
        this.cozinheiroRepository = cozinheiroRepository;
        this.degustadorRepository = degustadorRepository;
        this.editorRepository = editorRepository;
        this.categoriaRepository = categoriaRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.restauranteRepository = restauranteRepository;
        this.livroRepository = livroRepository;
        this.receitaRepository = receitaRepository;
        this.receitaIngredienteRepository = receitaIngredienteRepository;
        this.testeRepository = testeRepository;
        this.receitaLivroRepository = receitaLivroRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Criar Empregados
        List<Empregado> empregadosSource = Arrays.asList(
            new Empregado(100000001L, "João Silva", LocalDate.of(2020, 1, 15), new BigDecimal("3500.00")),
            new Empregado(100000002L, "Maria Oliveira", LocalDate.of(2019, 5, 20), new BigDecimal("4200.00")),
            new Empregado(100000003L, "Carlos Pereira", LocalDate.of(2021, 3, 10), new BigDecimal("3800.00")),
            new Empregado(100000004L, "Ana Costa", LocalDate.of(2022, 7, 1), new BigDecimal("5000.00")),
            new Empregado(100000005L, "Pedro Martins", LocalDate.of(2020, 11, 5), new BigDecimal("3200.00")),
            new Empregado(100000006L, "Sofia Ferreira", LocalDate.of(2023, 2, 28), new BigDecimal("4500.00"))
        );
        List<Empregado> empregados = empregadoRepository.saveAll(empregadosSource);

        List<Cozinheiro> cozinheirosSalvos = new ArrayList<>();
        List<Degustador> degustadoresSalvos = new ArrayList<>();
        List<Editor> editoresSalvos = new ArrayList<>();

        // 2. Criar Cozinheiros, Degustadores, Editores
        if (empregados.size() >= 1) {
            Cozinheiro cozinheiro1 = new Cozinheiro(null, empregados.get(0), "Chef J", LocalDate.of(2020,2,1), 10, 30, new HashSet<>(), new HashSet<>());
            cozinheirosSalvos.add(cozinheiro1);
        }
        if (empregados.size() >= 2) {
            Cozinheiro cozinheiro2 = new Cozinheiro(null, empregados.get(1), "Chef M", LocalDate.of(2019,6,1),15, 25, new HashSet<>(), new HashSet<>());
            cozinheirosSalvos.add(cozinheiro2);
        }
        if (!cozinheirosSalvos.isEmpty()) cozinheirosSalvos = cozinheiroRepository.saveAll(cozinheirosSalvos);


        if (empregados.size() >= 3) {
            Degustador degustador1 = new Degustador(null, empregados.get(2), LocalDate.of(2021, 4, 1), new HashSet<>());
            degustadoresSalvos.add(degustador1);
        }
        if (empregados.size() >= 4) {
            Degustador degustador2 = new Degustador(null, empregados.get(3), LocalDate.of(2022, 8, 1), new HashSet<>());
            degustadoresSalvos.add(degustador2);
        }
        if(!degustadoresSalvos.isEmpty()) degustadoresSalvos = degustadorRepository.saveAll(degustadoresSalvos);


        if (empregados.size() >= 5) {
            Editor editor1 = new Editor(null, empregados.get(4), LocalDate.of(2020, 12, 1),new HashSet<>());
            editoresSalvos.add(editor1);
        }
        if (empregados.size() >= 6) {
            Editor editor2 = new Editor(null, empregados.get(5), LocalDate.of(2023, 3, 1),new HashSet<>());
            editoresSalvos.add(editor2);
        }
        if(!editoresSalvos.isEmpty()) editoresSalvos = editorRepository.saveAll(editoresSalvos);


        // 3. Criar Categorias
        List<Categoria> categoriasSource = Arrays.asList(
            new Categoria(null, "Massas", new HashSet<>()),
            new Categoria(null, "Sobremesas", new HashSet<>()),
            new Categoria(null, "Carnes", new HashSet<>()),
            new Categoria(null, "Saladas", new HashSet<>())
        );
        List<Categoria> categoriasSalvas = categoriaRepository.saveAll(categoriasSource);

        // 4. Criar Ingredientes
        List<Ingrediente> ingredientesSource = Arrays.asList(
            new Ingrediente(null, "Farinha de Trigo", "Tipo 1", new HashSet<>()),
            new Ingrediente(null, "Açúcar Refinado", "Branco", new HashSet<>()),
            new Ingrediente(null, "Sal Marinho", "Grosso", new HashSet<>()),
            new Ingrediente(null, "Ovo de Galinha", "Tipo Grande Branco", new HashSet<>()),
            new Ingrediente(null, "Leite Integral", "Tipo A", new HashSet<>())
        );
        List<Ingrediente> ingredientesSalvos = ingredienteRepository.saveAll(ingredientesSource);

        // 5. Criar Restaurantes
        if (!cozinheirosSalvos.isEmpty()) {
            Restaurante restaurante1 = new Restaurante(null, "Cantina do Chef J", cozinheirosSalvos.get(0));
            restauranteRepository.save(restaurante1);
            if (cozinheirosSalvos.size() > 1) {
                Restaurante restaurante2 = new Restaurante(null, "Delícias da Chef M", cozinheirosSalvos.get(1));
                restauranteRepository.save(restaurante2);
            }
        }

        List<Livro> livrosSalvos = new ArrayList<>();
        // 6. Criar Livros
        if (!editoresSalvos.isEmpty()) {
            Livro livro1 = new Livro("978-0321765723", "Cozinha Clássica", editoresSalvos.get(0), new HashSet<>());
            livrosSalvos.add(livro1);
            if (editoresSalvos.size() > 1) {
                Livro livro2 = new Livro("978-0062316097", "Segredos da Confeitaria", editoresSalvos.get(1), new HashSet<>());
                livrosSalvos.add(livro2);
            }
            if(!livrosSalvos.isEmpty()) livrosSalvos = livroRepository.saveAll(livrosSalvos);
        }

        List<Receita> receitasSalvas = new ArrayList<>();
        // 7. Criar Receitas
        if (!categoriasSalvas.isEmpty() && !cozinheirosSalvos.isEmpty()) {
            Receita receita1 = new Receita(null, "Bolo de Chocolate", "Misture tudo e asse.", LocalDate.now(), 8, cozinheirosSalvos.get(0), categoriasSalvas.get(Math.min(1, categoriasSalvas.size()-1)), new HashSet<>(), new HashSet<>(), new HashSet<>());
            receitasSalvas.add(receita1);

            if (cozinheirosSalvos.size() > 1) {
                Receita receita2 = new Receita(null, "Macarrão ao Sugo", "Cozinhe o macarrão e adicione o molho.", LocalDate.now(), 4, cozinheirosSalvos.get(1), categoriasSalvas.get(0), new HashSet<>(), new HashSet<>(), new HashSet<>());
                receitasSalvas.add(receita2);
            }
            if(!receitasSalvas.isEmpty()) receitasSalvas = receitaRepository.saveAll(receitasSalvas);

            // 8. Criar ReceitaIngrediente
            if (!receitasSalvas.isEmpty() && !ingredientesSalvos.isEmpty()) {
                List<ReceitaIngrediente> rIsToSave = new ArrayList<>();

                if (receitasSalvas.size() > 0 && ingredientesSalvos.size() > 0) {
                    ReceitaIngredienteId ri1Id = new ReceitaIngredienteId(receitasSalvas.get(0).getId(), ingredientesSalvos.get(0).getId());
                    ReceitaIngrediente ri1 = new ReceitaIngrediente(ri1Id, receitasSalvas.get(0), ingredientesSalvos.get(0), new BigDecimal("2.0"), "xícaras");
                    rIsToSave.add(ri1);
                }
                if (receitasSalvas.size() > 0 && ingredientesSalvos.size() > 1) {
                    ReceitaIngredienteId ri2Id = new ReceitaIngredienteId(receitasSalvas.get(0).getId(), ingredientesSalvos.get(1).getId());
                    ReceitaIngrediente ri2 = new ReceitaIngrediente(ri2Id, receitasSalvas.get(0), ingredientesSalvos.get(1), new BigDecimal("1.5"), "xícaras");
                    rIsToSave.add(ri2);
                }

                if (receitasSalvas.size() > 1 && ingredientesSalvos.size() > 2) {
                    ReceitaIngredienteId ri3Id = new ReceitaIngredienteId(receitasSalvas.get(1).getId(), ingredientesSalvos.get(2).getId());
                    ReceitaIngrediente ri3 = new ReceitaIngrediente(ri3Id, receitasSalvas.get(1), ingredientesSalvos.get(2), new BigDecimal("500.0"), "gramas");
                    rIsToSave.add(ri3);
                }
                if (receitasSalvas.size() > 1 && ingredientesSalvos.size() > 3) {
                    ReceitaIngredienteId ri4Id = new ReceitaIngredienteId(receitasSalvas.get(1).getId(), ingredientesSalvos.get(3).getId());
                    ReceitaIngrediente ri4 = new ReceitaIngrediente(ri4Id, receitasSalvas.get(1), ingredientesSalvos.get(3), new BigDecimal("1.0"), "unidade");
                    rIsToSave.add(ri4);
                }
                if(!rIsToSave.isEmpty()) receitaIngredienteRepository.saveAll(rIsToSave);
            }

            // 9. Criar Testes
            if (!degustadoresSalvos.isEmpty() && !receitasSalvas.isEmpty()) {
                 List<Teste> testesToSave = new ArrayList<>(); // CORRIGIDO: List<Teste>
                
                Teste teste1 = new Teste(null, LocalDate.now(), 8.5, degustadoresSalvos.get(0), receitasSalvas.get(0)); // CORRIGIDO: Teste
                testesToSave.add(teste1);

                if (degustadoresSalvos.size() > 1 && receitasSalvas.size() > 1) {
                    Teste teste2 = new Teste(null, LocalDate.now().minusDays(1), 9.0, degustadoresSalvos.get(1), receitasSalvas.get(1)); // CORRIGIDO: Teste
                    testesToSave.add(teste2);
                }
                if(!testesToSave.isEmpty()) testeRepository.saveAll(testesToSave);
            }
            
            // 10. Criar ReceitaLivro
            if (!livrosSalvos.isEmpty() && !receitasSalvas.isEmpty()) {
                List<ReceitaLivro> rlsToSave = new ArrayList<>();
                ReceitaLivro rl1 = new ReceitaLivro(new ReceitaLivroId(livrosSalvos.get(0).getIsbn(), receitasSalvas.get(0).getId()), livrosSalvos.get(0), receitasSalvas.get(0));
                rlsToSave.add(rl1);

                if (livrosSalvos.size() > 1 && receitasSalvas.size() > 1) {
                    ReceitaLivro rl2 = new ReceitaLivro(new ReceitaLivroId(livrosSalvos.get(1).getIsbn(), receitasSalvas.get(1).getId()), livrosSalvos.get(1), receitasSalvas.get(1));
                    rlsToSave.add(rl2);
                }
                if(!rlsToSave.isEmpty()) receitaLivroRepository.saveAll(rlsToSave);
            }
        }
        System.out.println("Dados iniciais inseridos com sucesso!");
    }
}