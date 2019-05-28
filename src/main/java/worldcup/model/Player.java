package worldcup.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Player {

    @Id
    @JsonAlias("pid")
    private Long id;
    @JsonAlias("name")
    private String name;
    @JsonAlias("country")
    private String team;

    protected Player(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
