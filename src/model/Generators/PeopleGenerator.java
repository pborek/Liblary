package model.Generators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import biblioteka.Biblioteka;

public class PeopleGenerator {
	public List<String> nameList;
	public List<String> lastNameList;

	public PeopleGenerator(Biblioteka b) {
		BufferedReader nam = null;
		BufferedReader lnam = null;

		try {

			String sCurrentLine;
			String sCurrentLine1;

			nameList = new LinkedList<String>();
			lastNameList = new LinkedList<String>();

			nam = new BufferedReader(new FileReader("people/imiona.txt"));
			lnam = new BufferedReader(new FileReader("people/nazwiska.txt"));

			while ((sCurrentLine = nam.readLine()) != null) {
				nameList.add(sCurrentLine);
			}
			while ((sCurrentLine1 = lnam.readLine()) != null) {
				lastNameList.add(sCurrentLine1);
			}

			// wypełnianie bazy ludzmi
			fillLiblaryRandPeople(b);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (nam != null)
					nam.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	public void fillLiblaryRandPeople(Biblioteka b) {
		int id1 = 1;

		ListIterator<String> namelistIterator = this.nameList.listIterator();
		ListIterator<String> lastNamelistIterator = this.lastNameList
				.listIterator();

		for (int i = 0; i < 1000; i++) {
			if (namelistIterator.hasNext() == true
					& lastNamelistIterator.hasNext() == true) {

				Random rn = new Random();
				long n = 10000000000L;
				long r = rn.nextInt() % n;
				long randPesel = Math.abs((899999L + r));
				b.insertCzytelnik(namelistIterator.next(),
						lastNamelistIterator.next(), String.valueOf(randPesel),
						id1++);
			}
		}

	}
}
