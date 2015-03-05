package models.jm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "t_game")
public class Game implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "set_t_game")
	@TableGenerator(name = "set_t_game", table = "seq_t_game", allocationSize = 1)
	public Long id;
	@Column(name = "t_name", nullable = false)
	public String name;
	@Column(nullable = false)
	public Date createDate;
	@ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JoinTable(name = "m_game_author", joinColumns = { @JoinColumn(name = "g_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "p_id", referencedColumnName = "id") })
	public List<Author> authors = new ArrayList<Author>();

	@ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Player> players = new ArrayList<Player>();

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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
}
