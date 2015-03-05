package models.jm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "t_player")
public class Player implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_player")
	@TableGenerator(name = "seq_t_player", table = "seq_t_player", allocationSize = 1)
	public Long id;

	@Column(name = "player_name")
	public String name;

	@ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Game> games = new ArrayList<Game>();

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

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}
}
