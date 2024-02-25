package com.example.pedantixsolver;

import org.json.JSONException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PedantixSolver {

	private PedantixScoreService scoreService;
	private LexicalFieldService lexicalFieldService;

	public static final double TARGET_SCORE = 1000d;

	private static final List<String> ROOT_WORDS = Arrays.asList(
			"le", "temps", "vie", "espace", "personne", "endroit", "chose", "jour", "nuit", "année", "mois",
			"travail", "étude", "famille", "maison", "ville", "pays", "route", "nature", "air", "eau",
			"terre", "soleil", "lune", "étoile", "mer", "montagne", "forêt", "animal", "plante", "fleur",
			"fruit", "légume", "nourriture", "boisson", "corps", "tête", "main", "pied", "œil", "oreille",
			"bouche", "nez", "cœur", "esprit", "âme", "émotion", "pensée", "santé", "maladie", "médecine",
			"science", "technologie", "art", "musique", "danse", "théâtre", "cinéma", "peinture", "sculpture",
			"architecture", "livre", "poésie", "histoire", "géographie", "mathématiques", "physique", "chimie", "biologie",
			"informatique", "langue", "mot", "phrase", "lettre", "nombre", "couleur", "forme", "idée", "question",
			"problème", "solution", "projet", "trajet", "moyen", "transport", "voiture", "train", "avion", "bateau",
			"vélo", "marche", "course", "jeu", "sport", "loisir", "amour", "amitié", "relation", "mariage",
			"enfant", "parent", "grand-parent", "frère", "sœur", "oncle", "tante", "cousin", "cousine", "ami",
			"voisin", "collègue", "chef", "employé", "client", "argent", "trésor", "achat", "vente", "commerce",
			"entreprise", "travailleur", "école", "étudiant", "professeur", "cours", "examen", "note", "diplôme", "vacances",
			"voyage", "tourisme", "découverte", "aventure", "culture", "religion", "spiritualité", "croyance", "pratique", "rituel",
			"fête", "célébration", "événement", "souvenir", "histoire", "actualité", "information", "communication", "média", "journal",
			"radio", "télévision", "internet", "réseaux sociaux", "technologie", "invention", "découverte", "progrès", "changement", "tradition",
			"mode", "tendance", "style", "beauté", "santé", "bien-être", "alimentation", "exercice", "repos", "sommeil",
			"rêve", "cauchemar", "humour", "rire", "pleur", "émotion", "sentiment", "joie", "peine", "colère",
			"peur", "espoir", "confiance", "doute", "courage", "aventure", "risque", "sécurité", "paix", "guerre",
			"conflit", "solution", "problème", "décision", "choix", "avenir", "passé", "présent", "futur", "sous",
			"et", "ou", "mais", "car", "donc", "ni", "de", "à", "avec", "sans",
			"le", "la", "les", "un", "une", "des", "ce", "cette", "ces", "mon",
			"ma", "mes", "ton", "ta", "tes", "son", "sa", "ses", "notre", "votre",
			"leur", "son", "mais", "et", "ou", "donc", "or", "ni", "car", "que",
			"qui", "quoi", "où", "quand", "comment", "pourquoi", "lequel", "laquelle", "lesquels", "lesquelles",
			"ceux", "celui", "celle", "celles", "ceci", "cela", "ça", "me", "te", "se",
			"nous", "vous", "ils", "elles", "tout", "tous", "toute", "toutes", "quel", "quelle",
			"quels", "quelles", "ce", "cet", "cette", "ces", "aucun", "aucune", "chaque", "plusieurs", "certains",
			"certaines", "autre", "autres", "même", "pareil", "tel", "telle", "tels", "telles", "autant",
			"toutefois", "néanmoins", "cependant", "malgré", "aussi", "donc", "parce que", "puisque", "car", "en effet", "par", "depuis", "être", "avoir"
	);

	public PedantixSolver() {
		this.scoreService = new PedantixScoreService();
		this.lexicalFieldService = new LexicalFieldService();
	}

	public String solve(String rank) throws JSONException {

		double bestScore = Double.NEGATIVE_INFINITY;

		List<PedantixWord> sample = ROOT_WORDS.stream().map(PedantixWord::new).collect(Collectors.toList());

		String closestWord = null;

		Iterator<String> it = ROOT_WORDS.iterator();

		while (bestScore != TARGET_SCORE && !sample.isEmpty()) {

			if (scoreService.getRankWord(rank) != null) {
				return scoreService.getRankWord(rank);
			}

			closestWord = getClosestWord(sample, rank);
			if (closestWord == null) {
				closestWord = it.next();
			}
			PedantixWord pedantixWord = new PedantixWord(closestWord);
			double score = scoreService.getScore(pedantixWord, rank);
			if (score == TARGET_SCORE) {
				return pedantixWord.getWord();
			}
			if (score > bestScore) {
				bestScore = score;
			}
			List<String> newSample = lexicalFieldService.getSimilarWords(closestWord);
			if (newSample.isEmpty()) {
				sample.remove(new PedantixWord(closestWord));
			} else {
				sample = newSample.stream().map(PedantixWord::new).collect(Collectors.toList());
			}
		}

		return "NOT_FOUND";

	}

	public String getClosestWord(List<PedantixWord> sample, String targetKey) throws JSONException {

		Double maxScore = 0d;
		String bestWord = null;

		for (PedantixWord pedantixWord : sample) {
			double score = scoreService.getScore(pedantixWord, targetKey);
			if (score > maxScore) {
				maxScore = score;
				bestWord = pedantixWord.getWord();
			}
			if (score == TARGET_SCORE) {
				break;
			}
		}

		return bestWord;
	}

}