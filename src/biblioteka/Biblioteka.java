package biblioteka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import biblioteka.helpClasses.AutorTitleInfo;
import model.Czytelnik;
import model.Ksiazka;

public class Biblioteka {

	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/biblioteka?useUnicode=yes&amp;characterEncoding=UTF-8";

	private Connection conn;
	private Statement stat;

	public Biblioteka() {
		try {
			Class.forName(Biblioteka.DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("Brak starownika JDCB");
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(DB_URL, "root", "");
			stat = conn.createStatement();
		} catch (SQLException e) {
			System.err.println("Problem z otwarciem polaczania");
			e.printStackTrace();
		}

		crateTables();
	}

	public boolean crateTables() {
		String createCzytelnicy = "CREATE TABLE IF NOT EXISTS czytelnicy (id_czytelnika Integer PRIMARY KEY NOT NULL AUTO_INCREMENT, imie varchar(255), nazwisko varchar(255), pesel int);";
		String createKsiazki = "CREATE TABLE IF NOT EXISTS ksiazki (id_ksiazki Integer PRIMARY KEY NOT NULL AUTO_INCREMENT , tytul varchar(255), autor varchar(255), statWyp varchar(255));";
		String createWypozyczenia = "CREATE TABLE IF NOT EXISTS wypozyczenia (id_wypozycz Integer PRIMARY KEY NOT NULL AUTO_INCREMENT , id_czytelnika int, id_ksiazki int);";
		try {
			stat.execute(createCzytelnicy);
			stat.execute(createKsiazki);
			stat.execute(createWypozyczenia);
		} catch (SQLException e) {
			System.err.println("Blad przy tworzeniu tabeli");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean insertCzytelnik(String imie, String nazwisko, String pesel,
			int a) {
		try {
			PreparedStatement prepStmt = conn
					.prepareStatement("insert into czytelnicy values ( " + a
							+ ", ?, ?, ?);");
			prepStmt.setString(1, imie);
			prepStmt.setString(2, nazwisko);
			prepStmt.setString(3, pesel);
			prepStmt.execute();
		} catch (SQLException e) {
			System.err.println("Blad przy wstawianiu czytelnika");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean insertKsiazka(String tytul, String autor, int b,
			String statWyp) {
		try {

			PreparedStatement prepStmt = conn
					.prepareStatement("insert into ksiazki values (" + b
							+ ", ?, ?, ? );");
			prepStmt.setString(1, tytul);
			prepStmt.setString(2, autor);
			prepStmt.setString(3, statWyp);
			prepStmt.execute();
		} catch (SQLException e) {
			System.err.println("Blad przy wstawianiu ksiazki");
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public boolean insertWypożycz(int idCzytelnik, int idKsiazka) {
		try {
			String readStatWyp = null;
			String infoAutor = null;
			String infoTitle = null;

			// Zapytanie o sprawczenie czy ksizka jest juz wypozyczona
			String sqlQueryStatWyp = "SELECT statWyp FROM `biblioteka`.`ksiazki` WHERE id_ksiazki="
					+ idKsiazka + " AND statWyp ='Wypozyczona';";
			// Zapytanie o Tytul i autora ksiazki po indexie ksiazki
			String sqlQueryAutorTitle = "SELECT tytul, autor FROM `biblioteka`.`ksiazki` WHERE id_ksiazki="
					+ idKsiazka + ";";

			AutorTitleInfo aTInfo = new AutorTitleInfo(idCzytelnik, conn,
					sqlQueryAutorTitle);

			PreparedStatement pSStatWyp = conn
					.prepareStatement(sqlQueryStatWyp);

			ResultSet rs = pSStatWyp.executeQuery();
			// ResultSet rs1 = pSAutorTitle.executeQuery();

			PreparedStatement prepStmt = conn
					.prepareStatement("insert into wypozyczenia values (NULL, ?, ?);");

			while (rs.next()) {
				readStatWyp = rs.getString("statWyp");
				// System.out.println(userid);
			}

			if (readStatWyp == null) {
				prepStmt.setInt(1, idCzytelnik);
				prepStmt.setInt(2, idKsiazka);
				// zmiana staWyp z Dostepna na Wypozyczona przy wypozyczeniu
				prepStmt.executeUpdate("UPDATE ksiazki SET statWyp='Wypozyczona' WHERE id_ksiazki="
						+ idKsiazka + ";");
				prepStmt.execute();
				System.out.println("Wypozyczyles ksiązke: "
						+ aTInfo.getinfoTitle() + " autorstwa: "
						+ aTInfo.getinfoAutor() + "");
			} else {
				System.out.println("Nie możesz wypozyczyc ksiąki: "
						+ aTInfo.getinfoTitle()
						+ " ksiazka jest juz wypozyczona");
			}
		} catch (SQLException e) {
			System.err.println("Blad przy wypozyczaniu");
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public boolean returnWypożycz(int idCzytelnik, int idKsiazka) {
		try {
			String sqlQueryReturnBook = "UPDATE ksiazki SET statWyp='Dostepna' WHERE id_ksiazki="
					+ idKsiazka + ";";
			String sqlQueryDestroyRent = "DELETE FROM `biblioteka`.`wypozyczenia` WHERE id_czytelnika="
					+ idCzytelnik + " ;";

			PreparedStatement pSDestroyRent = conn
					.prepareStatement(sqlQueryDestroyRent);
			PreparedStatement pSReturnBook = conn
					.prepareStatement(sqlQueryReturnBook);
			pSReturnBook.execute();
			pSDestroyRent.execute();
		} catch (SQLException e) {
			System.err.println("Blad przy wypozyczaniu");
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public List<Czytelnik> selectCzytelnik() {
		List<Czytelnik> czytelnicy = new LinkedList<Czytelnik>();
		try {
			ResultSet result = stat.executeQuery("SELECT * FROM czytelnicy");
			int id;
			String imie, nazwisko, pesel;
			while (result.next()) {
				id = result.getInt("id_czytelnika");
				imie = result.getString("imie");
				nazwisko = result.getString("nazwisko");
				pesel = result.getString("pesel");
				czytelnicy.add(new Czytelnik(id, imie, nazwisko, pesel));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return czytelnicy;

	}

	public List<Ksiazka> selectKsiazki() {
		List<Ksiazka> ksiazki = new LinkedList<Ksiazka>();
		try {
			ResultSet result = stat.executeQuery("SELECT * FROM ksiazki");
			int id;
			String statWyp;
			String tytul, autor;
			while (result.next()) {
				id = result.getInt("id_ksiazki");
				tytul = result.getString("tytul");
				autor = result.getString("autor");
				statWyp = result.getString("statWyp");
				ksiazki.add(new Ksiazka(id, tytul, autor, statWyp));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return ksiazki;
	}

	public void pokazMojeWypozyczoneKsiazki(int idCzytelnik) {
		String infoAutor = null;
		String infoTitle = null;
		int rentBooks;
		// Zapytanie o wypozyczone ksiazki przez czytelnia o podanym indexie
		String sqlQueryMyRentBook = "SELECT * FROM `biblioteka`.`wypozyczenia` WHERE id_czytelnika="
				+ idCzytelnik + ";";
		try {
			PreparedStatement pSMyRentBook = conn
					.prepareStatement(sqlQueryMyRentBook);

			ResultSet rs = pSMyRentBook.executeQuery();

			while (rs.next()) {
				rentBooks = rs.getInt("id_ksiazki");
				// System.out.println(rentBooks);

				String sqlQueryAutorTitle = "SELECT tytul, autor FROM `biblioteka`.`ksiazki` WHERE id_ksiazki="
						+ rentBooks + ";";
				//
				// Zapytanie o Tytul i autora ksiazki po indexie ksiazki
				AutorTitleInfo aTInfo = new AutorTitleInfo(idCzytelnik, conn,
						sqlQueryAutorTitle);
				System.out.println(aTInfo.getinfoTitle() + " - "
						+ aTInfo.getinfoAutor());

			}

		} catch (SQLException e) {
			System.err.println("Blad przy sprawdzaniu wypozyczonych ksiażek");
			e.printStackTrace();
		}

	}

	public void destryTables() {
		try {
			stat.execute("DELETE FROM `biblioteka`.`czytelnicy` WHERE `id_czytelnika`>='0';");
			stat.execute("DELETE FROM `biblioteka`.`ksiazki` WHERE `id_ksiazki`>='0';");
		} catch (SQLException e) {
			System.err.println("Blad przy usuwaniu tabeli");
			e.printStackTrace();

		}

	}

	public void destryCzytelnicy() {
		try {
			stat.execute("DELETE FROM `biblioteka`.`czytelnicy` WHERE `id_czytelnika`>='0';");
		} catch (SQLException e) {
			System.err.println("Blad przy usuwaniu czytalnikow");
			e.printStackTrace();

		}

	}

	public void destryKsiazki() {
		try {
			stat.execute("DELETE FROM `biblioteka`.`ksiazki` WHERE `id_ksiazki`>='0';");
		} catch (SQLException e) {
			System.err.println("Blad przy usuwaniu ksiazek");
			e.printStackTrace();

		}

	}

	public void destryWypozycz() {
		try {
			stat.execute("DELETE FROM `biblioteka`.`wypozyczenia` WHERE `id_wypozycz`>='0';");
		} catch (SQLException e) {
			System.err.println("Blad przy usuwaniu ksiazek");
			e.printStackTrace();

		}

	}

	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.err.println("Problem z zamknieciem polaczenia");
			e.printStackTrace();
		}
	}
}