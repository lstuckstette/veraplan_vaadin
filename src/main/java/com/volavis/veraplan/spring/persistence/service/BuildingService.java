package com.volavis.veraplan.spring.persistence.service;

import com.volavis.veraplan.spring.persistence.entities.organisation.Building;
import com.volavis.veraplan.spring.persistence.repository.BuildingRepository;
import com.volavis.veraplan.spring.views.components.BuildingField;
import com.volavis.veraplan.spring.views.components.EntityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
public class BuildingService implements EntityService<Building, EntityFilter<BuildingField>> {


    private BuildingRepository buildingRepository;

    @Autowired
    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    @Override
    public int countAll() {
        return (int) buildingRepository.count();
    }

    @Override
    public int countAll(EntityFilter<BuildingField> filter) {
        return (int) buildingRepository.count(getExampleFromFilter(filter));
    }

    @Override
    public Stream<Building> getAllInRange(int offset, int limit) {

        List<Building> buildings = buildingRepository.findAll(new OffsetLimitRequest(offset, limit)).getContent();
        return buildings.stream();
    }

    @Override
    public Stream<Building> getAllInRange(EntityFilter<BuildingField> filter, int offset, int limit) {
        List<Building> buildings = buildingRepository.findAll(getExampleFromFilter(filter), new OffsetLimitRequest(offset, limit)).getContent();
        return buildings.stream();
    }

    private Example<Building> getExampleFromFilter(EntityFilter<BuildingField> filter) {
        Building b = new Building();

        switch (filter.getType()) {
            case ID:
                try {
                    b.setId(Long.valueOf(filter.getFilterText()));
                } catch (Exception e) {
                    //can not cast to Long:
                    b.setId(-1337l);
                }
                break;
            case NAME:
                b.setName(filter.getFilterText());
                break;
            case SHORTNAME:
                b.setShortName(filter.getFilterText());
                break;
            default:
                b.setId(-1337l);

        }

        return Example.of(b);
    }

    @Transactional
    public void saveBuilding(Building b) {
        buildingRepository.save(b);
    }

    public void removeBuilding(Building b) {
        buildingRepository.delete(b);
    }
}
