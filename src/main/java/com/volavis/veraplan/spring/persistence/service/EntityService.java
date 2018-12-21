package com.volavis.veraplan.spring.persistence.service;

import com.volavis.veraplan.spring.persistence.entities.organisation.Building;
import com.volavis.veraplan.spring.views.components.BuildingField;
import com.volavis.veraplan.spring.views.components.EntityFilter;
import org.springframework.data.domain.Example;

import java.util.stream.Stream;

public interface EntityService<B, F extends EntityFilter> {

    int countAll();

    int countAll(F filter);

    Stream<B> getAllInRange(int offset, int limit);

    Stream<B> getAllInRange(F filter, int offset, int limit);

    Example<B> getExampleFromFilter(F filter);

    void saveChanges(B entity);

}
