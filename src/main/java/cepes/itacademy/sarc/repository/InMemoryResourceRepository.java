package cepes.itacademy.sarc.repository;

import cepes.itacademy.sarc.domain.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryResourceRepository implements ResourceRepository {
    private final List<Resource> resources = new ArrayList<>();

    public InMemoryResourceRepository() {
        resources.add(new Notebook("CEPES1983", LocalDate.of(2023, 6, 1), "Dell Latitude"));
        resources.add(new Notebook("CEPES1995", LocalDate.of(2023, 6, 1), "Dell Inspiron"));
        resources.add(new Notebook("CEPES2017", LocalDate.of(2024, 2, 15), "Dell Alienware"));
        resources.add(new Notebook("CEPES1984", LocalDate.of(2023, 8, 20), "Dell XPS"));
        resources.add(new Room("512", 30, false));
        resources.add(new Room("511", 35, true));
        resources.add(new Room("401", 40, true));
        resources.add(new Room("ROOM101", 25, false));


        resources.add(new Lab("411", 30, "O que o BlueJ ja viu nesse lab o copilot nao tem armazenado"));
        resources.add(new Lab("312", 30, "Maquinas formatadas em janeiro de 2026, dual boot"));
        resources.add(new Lab("LAPRO", 60, "Duvidas peguntar para Giba"));
        resources.add(new Lab("LAB202", 30, "Laboratorio de pesquisa"));
    }

    @Override
    public Optional<Resource> findById(String id) {
        return resources.stream()
                .filter(r -> r.getId().equalsIgnoreCase(id))
                .findFirst();
    }
}
