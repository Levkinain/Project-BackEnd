package ru.vsu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vsu.entity.DriverEntity;
import ru.vsu.services.serviceImpl.DriverService;

import java.util.List;

@RestController
public class DriverRestController {
    private DriverService driverService;

    @Autowired
    public DriverRestController(DriverService driverService) {
        this.driverService = driverService;
    }

    @RequestMapping(value = "/drivers", method = RequestMethod.GET)
    public List<DriverEntity> getOrders(@RequestHeader("Authorization") String a) {
        return driverService.getAll();
    }

    @RequestMapping(value = "/drivers", method = RequestMethod.POST)
    public void createOrder(@RequestBody DriverEntity d, @RequestHeader("Authorization") String a) {
        driverService.insert(d);
    }

    @RequestMapping(value = "/drivers", method = RequestMethod.PATCH)
    public void updateOrder(@RequestBody DriverEntity d, @RequestHeader("Authorization") String a) {
        driverService.update(d);
    }

    @RequestMapping(value = "/drivers/{id}", method = RequestMethod.DELETE)
    public void deleteOrder(@PathVariable("id") long id, @RequestHeader("Authorization") String a) {
        driverService.delete(id);
    }

    @RequestMapping(value = "/drivers/{id}", method = RequestMethod.GET)
    public DriverEntity getOrder(@PathVariable("id") long id, @RequestHeader("Authorization") String a) {
        return driverService.getObjectById(id);
    }
}
