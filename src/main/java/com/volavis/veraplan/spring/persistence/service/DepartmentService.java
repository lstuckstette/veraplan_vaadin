package com.volavis.veraplan.spring.persistence.service;

import com.volavis.veraplan.spring.persistence.entities.organisation.Building;
import com.volavis.veraplan.spring.persistence.entities.organisation.Department;
import com.volavis.veraplan.spring.persistence.repository.DepartmentRepository;
import com.volavis.veraplan.spring.views.components.DepartmentField;
import com.volavis.veraplan.spring.views.components.EntityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class DepartmentService implements EntityService<Department, EntityFilter<DepartmentField>> {

    private DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public int countAll() {
        return (int) departmentRepository.count();
    }

    @Override
    public int countAll(EntityFilter<DepartmentField> filter) {
        return (int) departmentRepository.count(this.getExampleFromFilter(filter));
    }

    @Override
    public Stream<Department> getAllInRange(int offset, int limit) {
        List<Department> departments = departmentRepository.findAll(new OffsetLimitRequest(offset, limit)).getContent();
        return departments.stream();
    }

    @Override
    public Stream<Department> getAllInRange(EntityFilter<DepartmentField> filter, int offset, int limit) {
        List<Department> departments = departmentRepository.findAll(getExampleFromFilter(filter), new OffsetLimitRequest(offset, limit)).getContent();
        return departments.stream();
    }

    @Override
    public Example<Department> getExampleFromFilter(EntityFilter<DepartmentField> filter) {
        Department department = new Department();
        switch (filter.getType()) {
            case ID:
                try {
                    department.setId(Long.valueOf(filter.getFilterText()));
                } catch (Exception e) {
                    //numbercast-exception... return nothing!
                    department.setId(-1337L);
                }
                break;
            case NAME:
                department.setName(filter.getFilterText());
                break;
            default:
                department.setId(-1337L);
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.STARTING);

        return Example.of(department, matcher);
    }

    @Override
    public void saveChanges(Department entity) {
        departmentRepository.save(entity);
    }
}
