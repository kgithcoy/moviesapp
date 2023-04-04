package android.moviesapp.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


import java.io.Serializable;

@Entity(tableName = "genre")
public class Genre implements Serializable {
    @PrimaryKey
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return this.name;
    }
}
