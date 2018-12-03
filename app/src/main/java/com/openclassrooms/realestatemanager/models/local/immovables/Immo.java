package com.openclassrooms.realestatemanager.models.local.immovables;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.openclassrooms.realestatemanager.models.local.Agent;

import java.util.ArrayList;
import java.util.List;


@Entity(foreignKeys = @ForeignKey(entity = Agent.class,
        parentColumns = "id",
        childColumns = "agentId"))
public class Immo {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private int price;
    private int surface;
    private int pieceNumber;
    private String description;
    private List<Picture> gallery = new ArrayList<Picture>();
    private Vicinity vicinity;
    private List<String> pointsOfInterest = new ArrayList<String>();
    private boolean status;
    private String enterDate;
    private String sellingDate; // omitted in constructor because it should be initialize during an update
    private int agentId;

    // --- DEFAULT CONSTRUCTOR ---
    public Immo() {}

    // --- PARTIAL CONSTRUCTORS ---

    // --- 1 ---
    // without "description" / "gallery" / "pointsOfInterest"
    public Immo(String type, int price, int surface, int pieceNumber, Vicinity vicinity, String enterDate, int agentId) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.pieceNumber = pieceNumber;
        this.vicinity = vicinity;
        this.status = false;
        this.enterDate = enterDate;
        this.agentId = agentId;
    }

    // --- 2 ---
    // without "gallery" / "pointsOfInterest"
    public Immo(String type, int price, int surface, int pieceNumber, String description, Vicinity vicinity,
                String enterDate, int agentId) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.pieceNumber = pieceNumber;
        this.description = description;
        this.vicinity = vicinity;
        this.status = false;
        this.enterDate = enterDate;
        this.agentId = agentId;
    }

    // --- 3 ---
    // without "description" / "pointsOfInterest"
    public Immo(String type, int price, int surface, int pieceNumber, List<Picture> gallery, Vicinity vicinity,
                String enterDate, int agentId) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.pieceNumber = pieceNumber;
        this.gallery = gallery;
        this.vicinity = vicinity;
        this.status = false;
        this.enterDate = enterDate;
        this.agentId = agentId;
    }

    // --- 4 ---
    // without "description" / "gallery"
    public Immo(String type, int price, int surface, int pieceNumber, Vicinity vicinity, List<String> pointsOfInterest,
                String enterDate, int agentId) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.pieceNumber = pieceNumber;
        this.vicinity = vicinity;
        this.pointsOfInterest = pointsOfInterest;
        this.status = false;
        this.enterDate = enterDate;
        this.agentId = agentId;
    }

    // --- 5 ---
    // without "pointsOfInterest"
    public Immo(String type, int price, int surface, int pieceNumber, String description, List<Picture> gallery,
                Vicinity vicinity, String enterDate, int agentId) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.pieceNumber = pieceNumber;
        this.description = description;
        this.gallery = gallery;
        this.vicinity = vicinity;
        this.status = false;
        this.enterDate = enterDate;
        this.agentId = agentId;
    }

    // --- 6 ---
    // without "gallery"
    public Immo(String type, int price, int surface, int pieceNumber, String description,
                Vicinity vicinity, List<String> pointsOfInterest, String enterDate, int agentId) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.pieceNumber = pieceNumber;
        this.description = description;
        this.vicinity = vicinity;
        this.pointsOfInterest = pointsOfInterest;
        this.status = false;
        this.enterDate = enterDate;
        this.agentId = agentId;
    }

    // --- 7 ---
    // without "description"
    public Immo(String type, int price, int surface, int pieceNumber, List<Picture> gallery,
                Vicinity vicinity, List<String> pointsOfInterest, String enterDate, int agentId) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.pieceNumber = pieceNumber;
        this.gallery = gallery;
        this.vicinity = vicinity;
        this.pointsOfInterest = pointsOfInterest;
        this.status = false;
        this.enterDate = enterDate;
        this.agentId = agentId;
    }

    // --- COMPLETE CONSTRUCTOR ---
    public Immo(String type, int price, int surface, int pieceNumber, String description, List<Picture> gallery,
                Vicinity vicinity, List<String> pointsOfInterest, String enterDate, int agentId) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.pieceNumber = pieceNumber;
        this.description = description;
        this.gallery = gallery;
        this.vicinity = vicinity;
        this.pointsOfInterest = pointsOfInterest;
        this.status = false;
        this.enterDate = enterDate;
        this.agentId = agentId;
    }

    // --- GETTER ---
    public int getId() { return id; }
    public String getType() { return type; }
    public int getPrice() { return price; }
    public int getSurface() { return surface; }
    public int getPieceNumber() { return pieceNumber; }
    public String getDescription() { return description; }
    public List<Picture> getGallery() { return gallery; }
    public Vicinity getVicinity() { return vicinity; }
    public List<String> getPointsOfInterest() { return pointsOfInterest; }
    public boolean isStatus() { return status; }
    public String getEnterDate() { return enterDate; }
    public String getSellingDate() { return sellingDate; }
    public int getAgentId() { return agentId; }

    // --- SETTER ---
    public void setId(int id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setPrice(int price) { this.price = price; }
    public void setSurface(int surface) { this.surface = surface; }
    public void setPieceNumber(int pieceNumber) { this.pieceNumber = pieceNumber; }
    public void setDescription(String description) { this.description = description; }
    public void setGallery(List<Picture> gallery) { this.gallery = gallery; }
    public void setVicinity(Vicinity vicinity) { this.vicinity = vicinity; }
    public void setPointsOfInterest(List<String> pointsOfInterest) { this.pointsOfInterest = pointsOfInterest; }
    public void setStatus(boolean status) { this.status = status; }
    public void setEnterDate(String enterDate) { this.enterDate = enterDate; }
    public void setSellingDate(String sellingDate) { this.sellingDate = sellingDate; }
    public void setAgentId(int agentId) { this.agentId = agentId; }

    // --- UTILS ---

    /*
    To do : method that transform ContentValues to Immo object in order to work with our futur ContentProvider
     */
}