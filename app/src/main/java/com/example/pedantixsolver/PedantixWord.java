package com.example.pedantixsolver;

import java.util.Objects;

public class PedantixWord {

	private String word;
	
	public PedantixWord(String word) {
		this.word = word;
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}

	@Override
	public int hashCode() {
		return Objects.hash(word);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PedantixWord other = (PedantixWord) obj;
		return Objects.equals(word, other.word);
	}
	
	@Override
	public String toString() {
		return word.toString();
	}
	
}
