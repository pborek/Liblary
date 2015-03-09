package model.Generators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import biblioteka.Biblioteka;

public class BookGenerator {

	public List<String> titleList;
	public List<String> autorList;

	public BookGenerator(Biblioteka b) {
		BufferedReader book = null;

		try {

			String sCurrentLine;

			titleList = new LinkedList<String>();
			autorList = new LinkedList<String>();

			book = new BufferedReader(new FileReader("book/books.txt"));

			while ((sCurrentLine = book.readLine()) != null) {
				String actualyLine = sCurrentLine;
				Pattern p1 = Pattern.compile("(.*?)\\-");
				Pattern p2 = Pattern.compile("\\-(.*)(\\()");

				Matcher m1 = p1.matcher(actualyLine);
				Matcher m2 = p2.matcher(actualyLine);

				if (m1.find()) {
					titleList.add(m1.group(1));
					// System.out.println(m1.group(1));
				}
				if (m2.find()) {
					autorList.add(m2.group(1));
					// System.out.println(m2.group(1));
				}

			}
			// wypełnianie bazy ksiazek
			fillLiblaryRandBooks(b);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (book != null)
					book.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	public void fillLiblaryRandBooks(Biblioteka b) {
		int id1 = 1;

		ListIterator<String> titleListIterator = this.titleList.listIterator();
		ListIterator<String> autorListIterator = this.autorList.listIterator();

		for (int i = 0; i < 1000; i++) {
			if (titleListIterator.hasNext() == true
					& autorListIterator.hasNext() == true) {
				b.insertKsiazka(titleListIterator.next(),
						autorListIterator.next(), id1++, "Dostepna");

			}
		}

	}

}
