package ru.vsu.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.vsu.annotation.ObjectTypeId;
import ru.vsu.annotation.ParamAttributeId;
import ru.vsu.annotation.Reference;
import ru.vsu.entity.ObjectEntity;
import ru.vsu.services.serviceImpl.ObjectService;
import ru.vsu.services.serviceImpl.ParamsService;
import ru.vsu.services.serviceImpl.ReferenceService;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class MyAbstractEntityService<T extends ObjectEntity> implements MyService<T> {
    //получение класса T
    private final Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    private ObjectService<ObjectEntity> objectService;//или напрямую к дао?(что скорее всего не так)
    private ParamsService paramsService;
    private ReferenceService referenceService;

    @Autowired
    public MyAbstractEntityService(ObjectService<ObjectEntity> objectService, ParamsService paramsService, ReferenceService referenceService) {
        this.objectService = objectService;
        this.paramsService = paramsService;
        this.referenceService = referenceService;
    }

    @Override
    public void delete(T obj) {
        objectService.delete(obj);
        paramsService.deleteByObjectId(obj.getId());
        referenceService.deleteByObjectId(obj.getId());
    }

    public void delete(long id) {
        objectService.deleteByObjectId(id);
        paramsService.deleteByObjectId(id);
        referenceService.deleteByObjectId(id);
    }

    @Override
    public void insert(T obj) {
        long realObjectId = objectService.insertAndReturnId(obj);
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ParamAttributeId.class)) {
                try {
                    field.setAccessible(true);
                    paramsService.insert(field.getAnnotation(ParamAttributeId.class).id(), realObjectId, (String) field.get(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (field.isAnnotationPresent(Reference.class)) {
                try {
                    field.setAccessible(true);
                    referenceService.insert(field.getLong(obj), realObjectId, field.getAnnotation(Reference.class).attrId());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(T obj) {
        objectService.update(obj);
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ParamAttributeId.class)) {
                try {
                    field.setAccessible(true);
                    paramsService.update((String) field.get(obj), field.getAnnotation(ParamAttributeId.class).id(), obj.getId());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (field.isAnnotationPresent(Reference.class)) {
                try {
                    field.setAccessible(true);
                    referenceService.update(field.getLong(obj), obj.getId(), field.getAnnotation(Reference.class).attrId());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<T> getAll() {
        List<T> listOfEntities = new ArrayList<>();
        for (long id :
                objectService.getListOfObjectIdByObjectTypeId(clazz.getAnnotation(ObjectTypeId.class).id())) {
            listOfEntities.add((T) objectService.findById(id, clazz));
        }
        return listOfEntities;
    }

    public T getObjectById(long id) {
        return (T) objectService.findById(id, clazz);
    }

}
