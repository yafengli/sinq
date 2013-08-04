package models.jm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "t_author")
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_author")
    @TableGenerator(name = "seq_t_author", table = "seq_t_author", allocationSize = 1)
    public Long id;

    @Column(name = "author_name", nullable = false)
    public String name;

    @ManyToMany(mappedBy = "authors")
    private List<Game> games = new ArrayList<Game>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
