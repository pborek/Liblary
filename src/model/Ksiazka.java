package model;

import java.util.concurrent.atomic.AtomicBoolean;

public class Ksiazka {

	private int id;
	private String tytul;
	private String autor;
	String statWyp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return statWyp;
	}

	public void setStatus(String statWyp) {
		this.statWyp = statWyp;
	}

	public String getTytul() {
		return tytul;
	}

	public void setTytul(String tytul) {
		this.tytul = tytul;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Ksiazka() {
	};

	public Ksiazka(int id, String tytul, String autor, String statWyp) {
		this.id = id;
		this.tytul = tytul;
		this.autor = autor;
		this.statWyp = statWyp;
	}

	@Override
	public String toString() {
		return "[" + id + "] - " + tytul + " - " + autor+" - " +statWyp;
	}

}
