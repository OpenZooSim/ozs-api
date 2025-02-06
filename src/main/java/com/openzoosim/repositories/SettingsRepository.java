package com.openzoosim.repositories;

import com.openzoosim.entities.SettingsEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SettingsRepository implements PanacheRepository<SettingsEntity> {

   public SettingsEntity getSettings(){
       return findAll().firstResult();
   }
}
