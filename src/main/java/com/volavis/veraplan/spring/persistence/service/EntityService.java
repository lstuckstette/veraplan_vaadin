package com.volavis.veraplan.spring.persistence.service;

import com.volavis.veraplan.spring.views.components.EntityFilter;

import java.util.stream.Stream;

public interface EntityService<B, F extends EntityFilter> {

    int countAll();

    int countAll(F filter);

    Stream<B> getAllInRange(int offset, int limit);

    Stream<B> getAllInRange(F filter, int offset, int limit);

    void saveChanges(B entity);


}
