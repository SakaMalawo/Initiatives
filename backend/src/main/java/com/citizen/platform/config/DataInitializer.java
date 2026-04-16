package com.citizen.platform.config;

import com.citizen.platform.entity.Category;
import com.citizen.platform.entity.Zone;
import com.citizen.platform.repository.CategoryRepository;
import com.citizen.platform.repository.ZoneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ZoneRepository zoneRepository;

    public DataInitializer(CategoryRepository categoryRepository, ZoneRepository zoneRepository) {
        this.categoryRepository = categoryRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public void run(String... args) {
        // Initialize categories if empty
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category(null, "Environnement", "Initiatives liées à l'environnement et à l'écologie"));
            categoryRepository.save(new Category(null, "Transports", "Amélioration des transports en commun et mobilité"));
            categoryRepository.save(new Category(null, "Sécurité", "Sécurité publique et prévention"));
            categoryRepository.save(new Category(null, "Culture", "Événements culturels et patrimoine"));
            categoryRepository.save(new Category(null, "Sports", "Infrastructures sportives et activités"));
            categoryRepository.save(new Category(null, "Éducation", "Écoles, formations et apprentissage"));
            categoryRepository.save(new Category(null, "Urbanisme", "Aménagement urbain et espaces publics"));
            categoryRepository.save(new Category(null, "Social", "Aide sociale et solidarité"));
            System.out.println("Categories initialized!");
        }

        // Initialize zones if empty
        if (zoneRepository.count() == 0) {
            zoneRepository.save(new Zone(null, "Centre-ville", "Paris", "75001", "Île-de-France", "Zone centrale de la ville"));
            zoneRepository.save(new Zone(null, "Quartier Nord", "Paris", "75018", "Île-de-France", "Quartier nord de la ville"));
            zoneRepository.save(new Zone(null, "Quartier Sud", "Paris", "75014", "Île-de-France", "Quartier sud de la ville"));
            zoneRepository.save(new Zone(null, "Quartier Est", "Paris", "75020", "Île-de-France", "Quartier est de la ville"));
            zoneRepository.save(new Zone(null, "Quartier Ouest", "Paris", "75016", "Île-de-France", "Quartier ouest de la ville"));
            System.out.println("Zones initialized!");
        }
    }
}
