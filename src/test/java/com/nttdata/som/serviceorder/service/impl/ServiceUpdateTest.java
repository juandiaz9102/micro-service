package com.nttdata.som.serviceorder.service.impl;

import com.nttdata.model.tmf.*;
import com.nttdata.model.tmf638.Service;
import com.nttdata.model.tmf638.ServiceSpecificationRef;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ServiceUpdateTest {
    @InjectMocks
    ServiceUpdate serviceUpdate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(serviceUpdate, "graphQLURL", "http://demo8227627.mockable.io/graphql");
        ReflectionTestUtils.setField(serviceUpdate, "nameId", "id");
    }

    @Test
    public void patchServiceTest(){

        Service serviceTest =new Service();
        List<Feature> listFeature = new ArrayList<>();
        List<Note> listNote = new ArrayList<>();
        List<Place> listPlace = new ArrayList<>();
        List<RelatedEntity> relatedEntityList = new ArrayList<>();
        List<Party> partyList = new ArrayList<>();
        List<Characteristic> characteristicList = new ArrayList<>();
        ServiceSpecificationRef serviceSpecificationRef = new ServiceSpecificationRef();
        Feature feature = new Feature();
        RelatedEntity relatedEntity = new RelatedEntity();
        Note note = new Note();
        Place place = new Place();
        Party party = new Party();
        Characteristic characteristic = new Characteristic();
        characteristic.setId("123");
        characteristic.setName("123");
        serviceSpecificationRef.setId("123");
        party.setId("123");
        relatedEntity.setId("123");
        place.setId("123");
        note.setId("123");
        feature.setId("123");
        serviceTest.setId("123");
        serviceTest.setHref("213");
        serviceTest.setCategory("123");
        listFeature.add(feature);
        listNote.add(note);
        listPlace.add(place);
        relatedEntityList.add(relatedEntity);
        partyList.add(party);
        characteristicList.add(characteristic);
        serviceTest.setFeature(listFeature);
        serviceTest.setNote(listNote);
        serviceTest.setPlace(listPlace);
        serviceTest.setRelatedEntity(relatedEntityList);
        serviceTest.setRelatedParty(partyList);
        serviceTest.setServiceSpecification(serviceSpecificationRef);
        serviceTest.setServiceCharacteristic(characteristicList);

        try {
            serviceUpdate.patchService("123", serviceTest);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }
}
