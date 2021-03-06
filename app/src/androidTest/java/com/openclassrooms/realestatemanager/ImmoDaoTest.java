package com.openclassrooms.realestatemanager;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.models.local.Agent;
import com.openclassrooms.realestatemanager.models.local.immovables.Immo;
import com.openclassrooms.realestatemanager.models.database.RealEstateDB;
import com.openclassrooms.realestatemanager.models.local.immovables.Picture;
import com.openclassrooms.realestatemanager.models.local.immovables.Vicinity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ImmoDaoTest {

    // --- FOR DATA ---
    private RealEstateDB database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateDB.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    // --- DATA SET FOR TEST ---
    private static int AGENT_ID = 1;
    private static Agent AGENT_DEMO = new Agent(AGENT_ID, "Anthony", "anthony.mineo.pro@gmail.com", "none");

    private static Vicinity vic1 = new Vicinity("10 rue du test", null, "Android", "32000", "World");
    private static Vicinity vic2 = new Vicinity("20 rue du test", null, "Android", "32000", "World");
    private static Vicinity vic3 = new Vicinity("30 rue du test", null, "Android", "32000", "World");
    private static String description = "Hello test !";
    private static Immo IMMO_1 = new Immo("Apartment",  210000, 70, 5, 1, 1, description, vic1, 20181114, AGENT_ID);
    private static Immo IMMO_2 = new Immo("Mansion",  832500, 234, 16, 2, 2, description, vic2, 20181114, AGENT_ID);
    private static Immo IMMO_3 = new Immo("Apartment",  2350000, 79, 6, 1, 2, description, vic3,  20181114, AGENT_ID);

    @Test
    public void insertAndGetUser() throws InterruptedException {
        // BEFORE : Adding a new user
        this.database.agentDao().insertAgent(AGENT_DEMO);
        // TEST
        Agent agentTest = LiveDataTestUtil.getValue(this.database.agentDao().getAgentById(AGENT_ID));
        assertTrue(agentTest.getUserName().equals(AGENT_DEMO.getUserName()) && agentTest.getId() == AGENT_ID);
    }

    @Test
    public void getImmoWhenNoImmoInserted() throws InterruptedException {
        // TEST
        List<Immo> immoList = LiveDataTestUtil.getValue(this.database.immoDao().getImmosByAgent(AGENT_ID));
        assertTrue(immoList.isEmpty());
    }

    @Test
    public void insertAndGetImmo() throws InterruptedException {
        // BEFORE : Adding demo user & demo items
        this.database.agentDao().insertAgent(AGENT_DEMO);
        this.database.immoDao().insertImmo(IMMO_1);
        this.database.immoDao().insertImmo(IMMO_2);
        this.database.immoDao().insertImmo(IMMO_3);

        // TEST
        List<Immo> immoList = LiveDataTestUtil.getValue(this.database.immoDao().getImmosByAgent(AGENT_ID));
        assertTrue(immoList.size() == 3);
    }

    @Test
    public void insertAndUpdateItem() throws InterruptedException {
        // BEFORE : Adding demo user & demo items. Next, update item added & re-save it
        this.database.agentDao().insertAgent(AGENT_DEMO);
        this.database.immoDao().insertImmo(IMMO_1);
        Immo immoAdded = LiveDataTestUtil.getValue(this.database.immoDao().getImmosByAgent(AGENT_ID)).get(0);

        // Adding a list of picture
        List<Picture> gallery = new ArrayList<Picture>();
        Picture pic1 = new Picture("main", "file.1", "uri");
        Picture pic2 = new Picture("kitchen", "file.2", "uri");
        gallery.add(pic1);
        gallery.add(pic2);

        // Adding a list of point of interest
        List<String> pOI = new ArrayList<String>();
        pOI.add("Ecoles");
        pOI.add("SuperMarché");

        immoAdded.setPointsOfInterest(pOI);
        immoAdded.setGallery(gallery);
        this.database.immoDao().updateImmo(immoAdded);

        // TEST
        List<Immo> immoList = LiveDataTestUtil.getValue(this.database.immoDao().getImmosByAgent(AGENT_ID));
        assertTrue(immoList.size() == 1);
        assertTrue(immoList.get(0).getPointsOfInterest().get(0).contains("Ecoles") && immoList.get(0).getPointsOfInterest().get(1).contains("SuperMarché"));
        assertTrue(immoList.get(0).getGallery().get(0).getFileName().contains("file.1"));
        assertTrue(immoList.get(0).getGallery().get(1).getFileName().contains("file.2"));
    }

    @Test
    public void insertAndDeleteItem() throws InterruptedException {
        // BEFORE : Adding demo user & demo item. Next, get the item added & delete it.
        this.database.agentDao().insertAgent(AGENT_DEMO);
        this.database.immoDao().insertImmo(IMMO_1);
        Immo immoAdded = LiveDataTestUtil.getValue(this.database.immoDao().getImmosByAgent(AGENT_ID)).get(0);
        this.database.immoDao().deleteImmo(immoAdded.getId());

        // TEST
        List<Immo> immoList = LiveDataTestUtil.getValue(this.database.immoDao().getImmosByAgent(AGENT_ID));
        assertTrue(immoList.isEmpty());
    }
}
