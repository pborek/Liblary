package biblioteka.helpClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutorTitleInfo {
	private String infoAutor = null;
	private String infoTitle = null;

	public AutorTitleInfo(Connection conn, String sqlQueryAutorTitle) {

		PreparedStatement pSAutorTitle;
		try {
			pSAutorTitle = conn.prepareStatement(sqlQueryAutorTitle);
			ResultSet rs1 = pSAutorTitle.executeQuery();

			while (rs1.next()) {
				infoTitle = rs1.getString("tytul");
				infoAutor = rs1.getString("autor");
				// System.out.println(infoTitle + " - " + infoAutor);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public String getinfoAutor() {
		return infoAutor;
	}

	public String getinfoTitle() {
		return infoTitle;
	}
}