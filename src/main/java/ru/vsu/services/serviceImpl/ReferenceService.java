package ru.vsu.services.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.dao.daoImpl.ReferenceDao;
import ru.vsu.entity.ReferenceEntity;
import ru.vsu.services.MyService;

import java.util.List;

@Service
public class ReferenceService implements MyService<ReferenceEntity> {
    private ReferenceDao referenceDao;

    @Autowired
    public ReferenceService(ReferenceDao referenceDao) {
        this.referenceDao = referenceDao;
    }

    @Override
    public void delete(ReferenceEntity obj) {
        referenceDao.delete(obj);
    }

    @Override
    public void insert(ReferenceEntity obj) {
        referenceDao.insert(obj);
    }

    @Override
    public void update(ReferenceEntity obj) {
        referenceDao.update(obj);
    }

    @Override
    public List<ReferenceEntity> getAll() {
        return referenceDao.getAll();
    }
}